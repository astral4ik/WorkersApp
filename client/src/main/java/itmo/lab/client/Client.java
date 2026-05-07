package itmo.lab.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import itmo.lab.client.commands.ClientCommand;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Главный класс клиентского приложения: устанавливает соединение с сервером,
 * выполняет аутентификацию и обрабатывает пользовательские команды.
 */
public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6767;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final ClientConsole console;
    private final CommandManager commandManager;

    private String currentLogin;
    private String currentPassword;

    public Client() {
        this.console = new ClientConsole();
        this.commandManager = new CommandManager(this, this.console);
    }

    /**
     * Устанавливает TCP-соединение с сервером и инициализирует потоки ввода-вывода.
     */
    public void connect() throws IOException {
        socket = new Socket(SERVER_HOST, SERVER_PORT);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        console.printLine("Подключено к серверу");
    }

    /**
     * Запускает клиент: подключается к серверу, проходит аутентификацию
     * и запускает основной цикл обработки команд.
     */
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            console.printLine("Не удалось подключиться к серверу: " + e.getMessage());
            return;
        }

        if (!handleAuth()) {
            console.printLine("Аутентификация не пройдена. Выход.");
            close();
            return;
        }

        console.printLine("Введите 'help' для списка команд");

        while (true) {
            try {
                console.print("> ");
                String input = console.ask("").trim();

                if (input.isEmpty()) continue;
                if (input.equals("exit")) {
                    sendRequest(new Request("exit", null, currentLogin, currentPassword));
                    break;
                }

                handleCommand(input);
            } catch (Exception e) {
                console.printLine("Ошибка: " + e.getMessage());
            }
        }

        close();
    }

    /**
     * Выполняет интерактивную аутентификацию или регистрацию пользователя.
     *
     * @return {@code true}, если аутентификация прошла успешно
     */
    private boolean handleAuth() {
        console.printAuth();

        String choice = console.ask("Выберите: ").trim();

        if (!"1".equals(choice) && !"2".equals(choice)) {
            console.printLine("Неверный выбор. Введите '1' для входа или '2' для регистрации.");
            return false;
        }

        String login = console.ask("Login: ").trim();

        String password = console.ask("Password: ").trim();

        if (login.isEmpty() || password.isEmpty()) {
            console.printLine("Login и password не могут быть пустыми.");
            return false;
        }

        if ("2".equals(choice)) {
            Response resp = sendRequest(new Request("register", null, login, password));
            console.printLine(resp.getMessage());
            if (!resp.isSuccess()) {
                return false;
            }
        } 
        Response resp = sendRequest(new Request("login", null, login, password));
        if (!resp.isSuccess()) {
            console.printLine(resp.getMessage());
            return false;
         }    


        

        currentLogin = login;
        currentPassword = password;
        console.setSession(login, password);
        console.printLine("Вы вошли как: " + login);

        return true;
    }

    /**
     * Разбирает строку ввода и передаёт управление соответствующей команде.
     *
     * @param input строка, введённая пользователем
     */
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
     * Публичная точка входа для обработки команды из скрипта или другого источника.
     *
     * @param input строка команды
     */
    public void processCommand(String input) {
        handleCommand(input);
    }

    /**
     * Отправляет запрос на сервер и возвращает ответ.
     *
     * @param request запрос для отправки
     * @return ответ сервера или {@code null} при ошибке связи
     */
    public Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();

            Response response = (Response) in.readObject();
            return response;

        } catch (IOException | ClassNotFoundException e) {
            console.printLine("Ошибка связи с сервером: " + e.getMessage());
            return null;
        }
    }

    /**
     * Отправляет запрос на сервер, автоматически подставляя текущие учётные данные.
     *
     * @param request исходный запрос (логин и пароль будут заменены)
     * @return ответ сервера
     */
    public Response sendRequestWithAuth(Request request) {
        return sendRequest(new Request(
            request.getCommandName(),
            request.getArgs(),
            currentLogin,
            currentPassword
        ));
    }

    /**
     * Закрывает все потоки и сокет соединения.
     */
    private void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            console.close();
            console.printLine("Отключено от сервера.");
        } catch (IOException e) {
            console.printLine("Ошибка закрытия: " + e.getMessage());
        }
    }

    public String getCurrentLogin() {
        return currentLogin;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        new Client().run();
    }
}
