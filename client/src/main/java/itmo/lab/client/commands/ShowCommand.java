package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.data.Worker;

import java.util.List;

/**
 * Команда отображения всех работников коллекции.
 */
public class ShowCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public ShowCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Запрашивает у сервера список всех работников и выводит его в консоль.
     */
    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request(
            "show", null, client.getCurrentLogin(), client.getCurrentPassword()
        ));
        if (response.isSuccess() && response.getData() instanceof List) {
            @SuppressWarnings("unchecked")
            List<Worker> workers = (List<Worker>) response.getData();
            console.printWorkersList(workers);
        } else {
            console.printLine(response.getMessage());
        }
    }
}
