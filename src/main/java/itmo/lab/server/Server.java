package itmo.lab.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.server.storage.CollectionStorage;
import itmo.lab.server.storage.DataAccessException;
import itmo.lab.server.storage.XmlFileStorage;
import itmo.lab.WorkersCollection;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * Сервер для обработки запросов клиентов.
 *
 * Использует ServerSocketChannel с неблокирующим режимом и Selector
 * для обработки нескольких клиентов в одном потоке.
 */
public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private static final int PORT = 5555;
    private static final int BUFFER_SIZE = 65536;

    private WorkersCollection collection;
    private CollectionStorage storage;
    private RequestHandler requestHandler;
    private String fileName;

    private Selector selector;
    private ServerSocketChannel serverChannel;

    public Server() {
        this("workers.xml");
    }

    public Server(String filePath) {
        this.fileName = filePath;
        this.storage = new XmlFileStorage(filePath);
    }
    public void start() {
        logger.info("Загрузка коллекции из файла workers.xml...");

        try {
            collection = storage.load();
            logger.info("Загружено " + collection.size() + " работников из файла");
        } catch (DataAccessException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                logger.info("Файл не найден, создана новая пустая коллекция");
            } else {
                logger.error("Ошибка чтения файла: " + e.getMessage());
                logger.info("Создана новая пустая коллекция");
            }
            collection = new WorkersCollection(fileName);
        }

        requestHandler = new RequestHandler(collection, storage);

        try {
            selector = SelectorProvider.provider().openSelector();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new java.net.InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Сервер запущен на порту " + PORT);

            runServerLoop();

        } catch (IOException e) {
            logger.error("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void runServerLoop() throws IOException {
        while (true) {
            int readyCount = selector.select();
            if (readyCount == 0) continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (!key.isValid()) continue;

                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                } else if (key.isWritable()) {
                    handleWrite(key);
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();

        if (client == null) return;

        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        key.attach(buffer);

        logger.info("Подключился новый клиент: " + client.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        if (buffer == null) {
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            key.attach(buffer);
        }

        buffer.clear();

        int read;
        try {
            read = client.read(buffer);
        } catch (IOException e) {
            logger.error("Ошибка чтения от клиента: " + e.getMessage());
            closeClient(key);
            return;
        }

        if (read == -1) {
            logger.info("Клиент отключился");
            closeClient(key);
            return;
        }

        if (read > 0) {
            buffer.flip();
            logger.info("Получено " + buffer.remaining() + " байт от клиента");
            Request request;
            try {
                request = (Request) deserialize(buffer);
            } catch (Exception e) {
                logger.error("Ошибка десериализации: буфер=" + buffer.remaining() + " байт, исключение: " + e.getMessage());
                e.printStackTrace();
                closeClient(key);
                return;
            }
            logger.info("Получен запрос: " + request.getCommandName());

            if (request.getCommandName().equals("exit")) {
                logger.info("Клиент запросил выход");
                closeClient(key);
                return;
            }

            Response response;
            try {
                response = requestHandler.handle(request);
                logger.info("Команда выполнена: " + request.getCommandName());
            } catch (Exception e) {
                logger.error("Ошибка выполнения команды: " + e.getMessage());
                response = new Response(false, "Ошибка: " + e.getMessage(), null);
            }

            try {
                ByteBuffer responseBuffer = serialize(response);
                logger.info("Ответ сериализован, размер: " + responseBuffer.remaining() + " байт");
                key.attach(responseBuffer);
                key.interestOps(SelectionKey.OP_WRITE);
            } catch (Exception e) {
                logger.error("Ошибка сериализации ответа: " + e.getMessage());
            }
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        if (buffer == null) {
            logger.error("Буфер для отправки отсутствует");
            return;
        }

        int totalWritten = 0;
        while (buffer.hasRemaining()) {
            int bytesWritten = client.write(buffer);
            totalWritten += bytesWritten;
            if (bytesWritten == 0) {
                break;
            }
        }

        logger.info("Отправлено " + totalWritten + " байт клиенту");

        if (!buffer.hasRemaining()) {
            ByteBuffer newBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            key.attach(newBuffer);
            key.interestOps(SelectionKey.OP_READ);
        } else {
            buffer.compact();
            key.attach(buffer);
        }
    }

    private void closeClient(SelectionKey key) {
        try {
            key.cancel();
            key.channel().close();
            logger.info("Соединение с клиентом закрыто");
        } catch (IOException e) {
            logger.error("Ошибка при закрытии: " + e.getMessage());
        }
    }

    private ByteBuffer serialize(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(obj);
            byte[] bytes = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return buffer;
        }
    }

    private Object deserialize(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            return ois.readObject();
        }
    }

public static void main(String[] args) {
        String filePath;
        if (args.length > 0 && args[0].equals("server")) {
            filePath = args.length > 1 ? args[1] : "workers.xml";
            new Server(filePath).start();
        } else {
            new Client().run();
        }
    }
}