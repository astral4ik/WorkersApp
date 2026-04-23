package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.StringArgument;

/**
 * Команда фильтрации работников по имени.
 */
public class FilterContainsNameCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public FilterContainsNameCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: filter_contains_name <имя>");
            return;
        }
        Response response = client.sendRequest(new Request("filter_contains_name", new StringArgument(args.trim())));
        console.printLine(response.getMessage());
    }
}