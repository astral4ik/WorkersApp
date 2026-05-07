package itmo.lab.common;

import java.io.Serializable;

/**
 * Ответ сервера клиенту: статус выполнения, сообщение и необязательные данные.
 */
public class Response implements Serializable {
    private boolean success;
    private String message;
    private Object data;
    private String currentLogin;

    /**
     * Создаёт ответ без информации о текущем пользователе.
     *
     * @param success признак успешного выполнения команды
     * @param message текстовое сообщение для пользователя
     * @param data    произвольные данные (может быть null)
     */
    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Создаёт ответ с указанием текущего авторизованного пользователя.
     *
     * @param success      признак успешного выполнения команды
     * @param message      текстовое сообщение для пользователя
     * @param data         произвольные данные (может быть null)
     * @param currentLogin логин пользователя, выполнившего команду
     */
    public Response(boolean success, String message, Object data, String currentLogin) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.currentLogin = currentLogin;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
    public String getCurrentLogin() { return currentLogin; }
}
