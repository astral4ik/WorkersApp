package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Команда получения информации о коллекции.
 */
public class InfoCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public InfoCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request("info", null));
        console.printLine(response.getMessage());
    }
}