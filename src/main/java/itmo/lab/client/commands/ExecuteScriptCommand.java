package itmo.lab.client.commands;

import itmo.lab.client.Client;
import itmo.lab.client.ClientConsole;
import itmo.lab.client.FileInputSource;
import itmo.lab.client.InputSource;

import java.io.IOException;

/**
 * Команда выполнения скрипта из файла.
 */
public class ExecuteScriptCommand implements ClientCommand {

    private final Client client;
    private final ClientConsole console;

    public ExecuteScriptCommand(Client client, ClientConsole console) {
        this.client = client;
        this.console = console;
    }

    @Override
    public void execute(String args) throws IOException {
        String fileName = args.trim();
        
        if (fileName.isEmpty()) {
            console.printLine("Использование: execute_script <имя_файла>");
            return;
        }
        
        if (console.isFromFile()) {
            console.printLine("Ошибка: вложенный execute_script запрещён");
            return;
        }
        
        InputSource oldSource = console.getInputSource();
        
        try {
            FileInputSource fileSource = new FileInputSource(fileName);
            console.setInputSource(fileSource);
            console.setFromFile(true);
            
            console.printLine("Выполнение скрипта: " + fileName);
            
            while (true) {
                String line = console.getInputSource().readLine();
                
                if (line == null) {
                    break;
                }
                
                line = line.trim();
                
                if (line.isEmpty()) {
                    continue;
                }
                
                if (line.startsWith("execute_script")) {
                    console.printLine("Ошибка: вложенный execute_script запрещён");
                    break;
                }
                
                client.processCommand(line);
            }
            
            console.printLine("Скрипт завершён");
            
        } catch (IOException e) {
            console.printLine("Ошибка чтения файла: " + e.getMessage());
        } finally {
            console.setFromFile(false);
            console.setInputSource(oldSource);
        }
    }
}