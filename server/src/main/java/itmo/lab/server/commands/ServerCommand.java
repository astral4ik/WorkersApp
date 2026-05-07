package itmo.lab.server.commands;

import itmo.lab.common.Response;

import java.io.Serializable;

/**
 * Контракт серверной команды: выполнение логики и признак модификации коллекции.
 */
public interface ServerCommand {

    /**
     * Выполняет команду с переданными аргументами от имени указанного пользователя.
     *
     * @param args       аргументы команды (тип зависит от конкретной реализации)
     * @param ownerLogin Login пользователя, выполняющего команду
     * @return ответ с результатом выполнения
     */
    Response execute(Serializable args, String ownerLogin);

    /**
     * Возвращает {@code true}, если команда изменяет состояние коллекции.
     */
    boolean isModifying();
}
