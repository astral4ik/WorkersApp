package itmo.lab.client;

import itmo.lab.client.commands.ClientCommand;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Клиент для подключения к серверу.
 *
 * Подключается к серверу по TCP, отправляет команды и получает ответы.
 * Использует RetryBackoff для повторных попыток при временных сбоях сети.
 */
public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final int BUFFER_SIZE = 65536;

    private SocketChannel socket;
    private ClientConsole console;
    private ByteBuffer buffer;
    private CommandManager commandManager;

    /**
     * Конструктор клиента.
     */
    public Client() {
        this.console = new ClientConsole();
        this.commandManager = new CommandManager(this, this.console);
    }

    /**
     * Подключение к серверу.
     * @throws IOException ошибка подключения
     */
    public void connect() throws IOException {
        socket = SocketChannel.open(new java.net.InetSocketAddress(SERVER_HOST, SERVER_PORT));
        socket.configureBlocking(true);
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        console.printLine("Подключено к серверу");
    }

    /**
     * Запуск клиента.
     */
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            console.printLine("Не удалось подключиться к серверу: " + e.getMessage());
            return;
        }

        console.printLine("Введите 'help' для списка команд");

        while (true) {
            try {
                console.print("> ");
                String input = console.ask("").trim();

                if (input.isEmpty()) continue;
                if (input.equals("exit")) {
                    sendRequest(new Request("exit", null));
                    break;
                }

                handleCommand(input);
            } catch (Exception e) {
                console.printLine("Ошибка: " + e.getMessage());
            }
        }

        close();
    }

    private void handleCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        ClientCommand clientCommand = commandManager.get(command);
        if (clientCommand != null) {
            try {
                clientCommand.execute(args);
            } catch (IOException e) {
                console.printLine("Ошибка выполнения: " + e.getMessage());
            }
        } else {
            console.printLine("Неизвестная команда: " + command + ". Введите 'help' для списка команд.");
        }
    }

    /**
     * Обработать команду (public для скриптов).
     * @param input команда для обработки
     */
    public void processCommand(String input) {
        handleCommand(input);
    }

    /**
     * Отправка запроса на сервер.
     * @param request запрос
     * @return ответ сервера
     */
    public Response sendRequest(Request request) {
        try {
            ByteBuffer requestBuffer = serialize(request);
            socket.write(requestBuffer);

            buffer.clear();

            RetryBackoff.run(() -> {
                int bytesRead = socket.read(buffer);
                if (bytesRead == 0) {
                    throw new IOException("Нет данных");
                }
            });

            buffer.flip();

            if (buffer.remaining() == 0) {
                console.printLine("Сервер разорвал соединение");
                return null;
            }

            Response response = (Response) deserialize(buffer);
            return response;
        } catch (IOException | ClassNotFoundException e) {
            console.printLine("Ошибка: " + e.getMessage());
            return null;
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

    private void close() {
        try {
            if (socket != null) socket.close();
            console.close();
            console.printLine("Отключено от сервера.");
        } catch (IOException e) {
            console.printLine("Ошибка закрытия: " + e.getMessage());
        }
    }

    /**
     * Точка входа в клиентское приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        new Client().run();
    }
}