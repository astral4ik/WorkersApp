package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.StringArgument;

/**
 * Команда подсчета работников по дате начала.
 */
public class CountLessThanStartDateCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public CountLessThanStartDateCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: count_less_than_start_date <дата>");
            return;
        }
        Response response = client.sendRequest(new Request("count_less_than_start_date", new StringArgument(args.trim())));
        console.printLine(response.getMessage());
    }
}