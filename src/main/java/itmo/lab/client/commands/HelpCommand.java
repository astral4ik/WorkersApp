package itmo.lab.client.commands;

import itmo.lab.client.ClientConsole;

/**
 * Команда вывода справки.
 */
public class HelpCommand implements ClientCommand {

    private final ClientConsole console;

    public HelpCommand(ClientConsole console) {
        this.console = console;
    }

    @Override
    public void execute(String args) {
        console.printLine("Доступные команды:");
        console.printLine("insert - добавить нового работника");
        console.printLine("update <id> - обновить данные работника");
        console.printLine("remove_key <id> - удалить работника по ID");
        console.printLine("remove_lower <id> - удалить работников с ID меньше указанного");
        console.printLine("remove_greater_key <id> - удалить работников с ID больше указанного");
        console.printLine("show - показать всех работников");
        console.printLine("info - информация о коллекции");
        console.printLine("clear - очистить коллекцию");
        console.printLine("filter_contains_name <имя> - поиск по имени");
        console.printLine("count_less_than_start_date <дата> - подсчет по дате");
        console.printLine("print_unique_start_date - уникальные даты");
        console.printLine("replace_if_lower <id> - заменить если зарплата ниже");
        console.printLine("exit - выход");
    }
}