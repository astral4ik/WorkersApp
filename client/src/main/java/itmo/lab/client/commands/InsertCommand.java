package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;

/**
 * Команда добавления нового работника в коллекцию на сервере.
 */
public class InsertCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public InsertCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Запрашивает данные нового работника у пользователя и отправляет запрос на сервер.
     */
    @Override
    public void execute(String args) {
        Worker worker = console.buildWorker(null);
        Response response = client.sendRequest(new Request(
            "insert",
            new WorkerArgument(worker),
            client.getCurrentLogin(),
            client.getCurrentPassword()
        ));
        console.printLine(response.getMessage());
    }
}
