package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.WorkersCollection;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 * Команда получения информации о коллекции.
 * Возвращает размер, дату создания и имя файла.
 */
public class InfoCommand implements ServerCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final WorkersCollection collection;

    public InfoCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        String fileName = collection.getFileName();
        if (fileName == null) {
            fileName = "unknown";
        }
        String creationTimeStr = "unknown";
        if (collection.getCreationTime() != null) {
            creationTimeStr = collection.getCreationTime().format(FORMATTER);
        }
        String info = String.format("Размер коллекции: %d, дата создания: %s, файл: %s",
                collection.size(),
                creationTimeStr,
                fileName);
        return new Response(true, info, null);
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}