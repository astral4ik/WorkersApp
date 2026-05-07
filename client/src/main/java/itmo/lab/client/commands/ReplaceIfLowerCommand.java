package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.common.Request;
import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;

/**
 * Команда замены работника новым, если зарплата нового ниже текущей.
 */
public class ReplaceIfLowerCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public ReplaceIfLowerCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    /**
     * Запрашивает данные нового работника и отправляет серверу запрос на замену,
     * если зарплата нового работника меньше зарплаты существующего.
     *
     * @param args строка аргументов, содержащая идентификатор работника
     */
    @Override
    public void execute(String args) {
        if (args.isEmpty()) {
            console.printLine("Использование: replace_if_lower <id>");
            return;
        }
        int id = Integer.parseInt(args.split("\\s+")[0]);
        console.printLine("Введите новые данные:");
        Worker worker = console.buildWorker(null);
        Object[] arr = new Object[]{new IdArgument(id), new WorkerArgument(worker)};
        Response response = client.sendRequest(new Request("replace_if_lower", (java.io.Serializable) arr));
        console.printLine(response.getMessage());
    }
}
