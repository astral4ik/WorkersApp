package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Команда очистки коллекции.
 */
public class ClearCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    /**
     * Конструктор команды.
     * @param client клиент
     * @param console консоль
     */
    public ClearCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request("clear", null));
        console.printLine(response.getMessage());
    }
}