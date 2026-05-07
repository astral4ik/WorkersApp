package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;

/**
 * Команда вывода уникальных дат начала работы из всей коллекции.
 */
public class PrintUniqueStartDateCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public PrintUniqueStartDateCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Запрашивает у сервера и выводит множество уникальных дат начала работы.
     */
    @Override
    public void execute(String args) {
        Response response = client.sendRequest(new Request(
            "print_unique_start_date", null,
            client.getCurrentLogin(), client.getCurrentPassword()
        ));
        console.printLine(response.getMessage());
    }
}
