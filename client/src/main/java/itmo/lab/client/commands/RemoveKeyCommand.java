package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;

/**
 * Команда удаления работника из коллекции по его идентификатору.
 */
public class RemoveKeyCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public RemoveKeyCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Отправляет серверу запрос на удаление работника с указанным идентификатором.
     *
     * @param args строка аргументов, содержащая идентификатор работника
     */
    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: remove_key <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);
        Response response = client.sendRequest(new Request(
            "remove_key", new IdArgument(id), client.getCurrentLogin(), client.getCurrentPassword()
        ));
        console.printLine(response.getMessage());
    }
}
