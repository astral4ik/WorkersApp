package itmo.lab;

import itmo.lab.client.Client;

/**
 * Точка входа приложения.
 *
 * Запуск сервера: java -jar app.jar server [файл]
 * Запуск клиента: java -jar app.jar
 */
public class App {

    /**
     * Точка входа в приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")) {
            new Server("workers.xml").start();
        } else {
            new Client().run();
        }
    }
}