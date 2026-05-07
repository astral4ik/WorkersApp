package itmo.lab.client;

import java.util.Scanner;

/**
 * Источник ввода из консоли (System.in).
 */
public class ConsoleInputSource implements InputSource {

    private final Scanner scanner;

    public ConsoleInputSource() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }
}