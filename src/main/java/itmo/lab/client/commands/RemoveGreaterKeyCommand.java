package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;

/**
 * Команда удаления работников с ID больше указанного.
 */
public class RemoveGreaterKeyCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public RemoveGreaterKeyCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: remove_greater_key <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);
        Response response = client.sendRequest(new Request("remove_greater_key", new IdArgument(id)));
        console.printLine(response.getMessage());
    }
}