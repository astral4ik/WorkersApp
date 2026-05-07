package itmo.lab.client;

import itmo.lab.client.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр команд клиента: регистрирует все доступные команды и предоставляет доступ к ним по имени.
 */
public class CommandManager {

    private final Map<String, ClientCommand> commands = new HashMap<>();
    private final Client client;
    private final ClientConsole console;

    /**
     * Инициализирует менеджер и регистрирует все поддерживаемые команды.
     *
     * @param client  клиент для отправки запросов серверу
     * @param console консоль для взаимодействия с пользователем
     */
    public CommandManager(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;

        commands.put("insert", new InsertCommand(console, client));
        commands.put("update", new UpdateCommand(console, client));
        commands.put("remove_key", new RemoveKeyCommand(console, client));
        commands.put("remove_lower", new RemoveLowerCommand(console, client));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand(console, client));
        commands.put("clear", new ClearCommand(console, client));
        commands.put("show", new ShowCommand(console, client));
        commands.put("info", new InfoCommand(console, client));
        commands.put("filter_contains_name", new FilterContainsNameCommand(console, client));
        commands.put("count_less_than_start_date", new CountLessThanStartDateCommand(console, client));
        commands.put("print_unique_start_date", new PrintUniqueStartDateCommand(console, client));
        commands.put("replace_if_lower", new ReplaceIfLowerCommand(client, console));
        commands.put("get", new GetWorkerCommand(console, client));
        commands.put("help", new HelpCommand(console));
        commands.put("execute_script", new ExecuteScriptCommand(client, console));
    }

    /**
     * Возвращает команду по её имени или {@code null}, если команда не зарегистрирована.
     *
     * @param command имя команды
     * @return объект команды или {@code null}
     */
    public ClientCommand get(String command) {
        return commands.get(command);
    }
}
