package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.StringArgument;
import itmo.lab.data.Worker;

import java.util.List;

/**
 * Команда фильтрации работников по подстроке в имени.
 */
public class FilterContainsNameCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public FilterContainsNameCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Отправляет серверу запрос на поиск работников, чьё имя содержит переданную подстроку,
     * и выводит найденных в консоль.
     *
     * @param args строка аргументов, содержащая искомую подстроку
     */
    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: filter_contains_name <имя>");
            return;
        }
        Response response = client.sendRequest(new Request(
            "filter_contains_name", new StringArgument(args.trim()),
            client.getCurrentLogin(), client.getCurrentPassword()
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
