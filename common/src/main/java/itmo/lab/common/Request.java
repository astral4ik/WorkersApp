package itmo.lab.common;

import java.io.Serializable;

/**
 * Запрос клиента к серверу с именем команды, аргументами и учётными данными.
 */
public class Request implements Serializable {
    private String commandName;
    private Serializable args;
    private String login;
    private String password;

    /**
     * Создаёт запрос без учётных данных.
     *
     * @param commandName имя команды
     * @param args        аргументы команды (может быть null)
     */
    public Request(String commandName, Serializable args) {
        this.commandName = commandName;
        this.args = args;
    }

    /**
     * Создаёт запрос с учётными данными пользователя.
     *
     * @param commandName имя команды
     * @param args        аргументы команды (может быть null)
     * @param login       логин пользователя
     * @param password    пароль пользователя
     */
    public Request(String commandName, Serializable args, String login, String password) {
        this.commandName = commandName;
        this.args = args;
        this.login = login;
        this.password = password;
    }

    public String getCommandName() {
        return commandName;
    }

    public Serializable getArgs() {
        return args;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
