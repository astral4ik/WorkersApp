package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Команда вывода общей информации о коллекции (тип, размер, дата инициализации).
 */
public class InfoCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public InfoCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Запрашивает у сервера метаданные коллекции и выводит их в консоль.
     */
    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request(
            "info", null, client.getCurrentLogin(), client.getCurrentPassword()
        ));
        console.printLine(response.getMessage());
    }
}
