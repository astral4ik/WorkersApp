package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;

/**
 * Команда добавления нового работника.
 */
public class InsertCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public InsertCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        Worker worker = console.inputWorker();
        Response response = client.sendRequest(new Request("insert", new WorkerArgument(worker)));
        console.printLine(response.getMessage());
    }
}