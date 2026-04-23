package itmo.lab.common;

import java.io.Serializable;

/**
 * Запрос от клиента к серверу.
 *
 * Содержит имя команды и её аргументы.
 */
public class Request implements Serializable {
    private String commandName;
    private Serializable args;

    public Request(String commandName, Serializable args) {
        this.commandName = commandName;
        this.args = args;
    }

    public String getCommandName() {
        return commandName;
    }

    public Serializable getArgs() {
        return args;
    }
}