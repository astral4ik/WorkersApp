package itmo.lab.client;

import itmo.lab.client.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер команд клиента.
 */
public class CommandManager {

    private final Map<String, ClientCommand> commands = new HashMap<>();

    /**
     * Конструктор с инициализацией всех команд.
     */
    public CommandManager(itmo.lab.client.Client client, itmo.lab.client.ClientConsole console) {
        commands.put("insert", new InsertCommand(client, console));
        commands.put("update", new UpdateCommand(client, console));
        commands.put("remove_key", new RemoveKeyCommand(client, console));
        commands.put("show", new ShowCommand(client, console));
        commands.put("info", new InfoCommand(client, console));
        commands.put("clear", new ClearCommand(client, console));
        commands.put("filter_contains_name", new FilterContainsNameCommand(client, console));
        commands.put("count_less_than_start_date", new CountLessThanStartDateCommand(client, console));
        commands.put("print_unique_start_date", new PrintUniqueStartDateCommand(client, console));
        commands.put("remove_lower", new RemoveLowerCommand(client, console));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand(client, console));
        commands.put("replace_if_lower", new ReplaceIfLowerCommand(client, console));
        commands.put("execute_script", new ExecuteScriptCommand(client, console));
        commands.put("help", new HelpCommand(console));
    }

    /**
     * Получить команду по имени.
     */
    public ClientCommand get(String commandName) {
        return commands.get(commandName.toLowerCase());
    }
}