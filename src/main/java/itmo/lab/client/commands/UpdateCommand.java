package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;

/**
 * Команда обновления данных работника.
 */
public class UpdateCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public UpdateCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: update <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);

        Response response = client.sendRequest(new Request("get", new IdArgument(id)));

        if (!response.isSuccess()) {
            console.printLine(response.getMessage());
            return;
        }

        Worker existing = (Worker) response.getData();
        if (existing == null) {
            console.printLine("Работник не получен");
            return;
        }

        Worker edited = console.editWorker(existing);
        edited.setId(id);

        Object[] arr = new Object[]{new IdArgument(id), new WorkerArgument(edited)};
        response = client.sendRequest(new Request("update", (java.io.Serializable) arr));

        console.printLine(response.getMessage());
    }
}