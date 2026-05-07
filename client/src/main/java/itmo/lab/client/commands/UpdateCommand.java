package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;

/**
 * Команда обновления данных существующего работника по его идентификатору.
 */
public class UpdateCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public UpdateCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Загружает текущие данные работника с сервера, предлагает их отредактировать
     * и отправляет обновлённую версию обратно.
     *
     * @param args строка аргументов, содержащая идентификатор работника
     */
    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: update <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);

        Response response = client.sendRequest(new Request(
            "get", new IdArgument(id), client.getCurrentLogin(), client.getCurrentPassword()
        ));

        if (!response.isSuccess()) {
            console.printLine(response.getMessage());
            return;
        }

        Worker existing = (Worker) response.getData();
        if (existing == null) {
            console.printLine("Работник не получен");
            return;
        }

        if (!client.getCurrentLogin().equals(existing.getOwnerLogin())) {
            console.printLine("Вы не можете редактировать чужого работника");
            return;
        }

        Worker edited = console.buildWorker(existing);
        edited.setId(id);

        Object[] arr = new Object[]{new IdArgument(id), new WorkerArgument(edited)};
        response = client.sendRequest(new Request(
            "update", (java.io.Serializable) arr, client.getCurrentLogin(), client.getCurrentPassword()
        ));

        console.printLine(response.getMessage());
    }
}
