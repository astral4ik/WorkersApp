package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;

/**
 * Команда замены работника если зарплата ниже.
 */
public class ReplaceIfLowerCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public ReplaceIfLowerCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: replace_if_lower <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);
        console.printLine("Введите новые данные:");
        Worker worker = console.inputWorker();
        Object[] arr = new Object[]{new IdArgument(id), new WorkerArgument(worker)};
        Response response = client.sendRequest(new Request("replace_if_lower", (java.io.Serializable) arr));
        console.printLine(response.getMessage());
    }
}