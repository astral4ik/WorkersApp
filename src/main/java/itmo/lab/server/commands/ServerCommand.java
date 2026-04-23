package itmo.lab.server.commands;

import itmo.lab.common.Response;

import java.io.Serializable;

/**
 * Интерфейс команды сервера.
 */
public interface ServerCommand {

    /**
     * Выполнить команду.
     *
     * @param args аргументы команды
     * @return ответ сервера
     */
    Response execute(Serializable args);

    /**
     * Проверяет, модифицирует ли команда коллекцию.
     *
     * @return true, если команда модифицирует коллекцию
     */
    boolean isModifying();
}