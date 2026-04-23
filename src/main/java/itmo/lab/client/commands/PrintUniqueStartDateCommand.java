package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Команда вывода уникальных дат начала работы.
 */
public class PrintUniqueStartDateCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public PrintUniqueStartDateCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request("print_unique_start_date", null));
        console.printLine(response.getMessage());
    }
}