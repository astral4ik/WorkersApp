package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.data.Worker;

/**
 * Команда получения и отображения данных одного работника по его идентификатору.
 */
public class GetWorkerCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public GetWorkerCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Запрашивает у сервера работника с указанным ID и выводит его данные.
     *
     * @param args строка аргументов, содержащая идентификатор работника
     */
    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: get <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);
        Response response = client.sendRequest(new Request(
            "get", new IdArgument(id), client.getCurrentLogin(), client.getCurrentPassword()
        ));
        if (response.isSuccess() && response.getData() instanceof Worker) {
            console.printWorker((Worker) response.getData());
        } else {
            console.printLine(response.getMessage());
        }
    }
}
