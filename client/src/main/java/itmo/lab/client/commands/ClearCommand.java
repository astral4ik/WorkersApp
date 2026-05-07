package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Команда очистки коллекции работников, принадлежащих текущему пользователю.
 */
public class ClearCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public ClearCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Отправляет серверу запрос на удаление всех работников текущего пользователя.
     */
    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request(
            "clear", null, client.getCurrentLogin(), client.getCurrentPassword()
        ));
        console.printLine(response.getMessage());
    }
}
