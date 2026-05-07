package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.StringArgument;

/**
 * Команда подсчёта работников с датой начала работы, меньше указанной.
 */
public class CountLessThanStartDateCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public CountLessThanStartDateCommand(ClientConsole console, Client client) {
        this.console = console;
        this.client = client;
    }

    /**
     * Отправляет серверу запрос и выводит количество работников,
     * чья дата начала работы строго меньше переданной.
     *
     * @param args строка аргументов, содержащая дату в формате yyyy-MM-dd HH:mm
     */
    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: count_less_than_start_date <дата>");
            return;
        }
        Response response = client.sendRequest(new Request(
            "count_less_than_start_date", new StringArgument(args.trim()),
            client.getCurrentLogin(), client.getCurrentPassword()
        ));
        console.printLine(response.getMessage());
    }
}
