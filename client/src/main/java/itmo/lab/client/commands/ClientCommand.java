package itmo.lab.client.commands;

import java.io.IOException;

/**
 * Интерфейс команды клиента.
 */
public interface ClientCommand {

    /**
     * Выполнить команду.
     * @param args аргументы команды
     * @throws IOException ошибка ввода-вывода
     */
    void execute(String args) throws IOException;
}