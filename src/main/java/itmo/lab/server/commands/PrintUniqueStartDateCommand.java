package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.WorkersCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Команда вывода уникальных дат начала работы.
 */
public class PrintUniqueStartDateCommand implements ServerCommand {

    private final WorkersCollection collection;

    public PrintUniqueStartDateCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        List<java.time.LocalDateTime> dates = collection.getUniqueStartDates();
        return new Response(true, "Уникальных дат: " + dates.size(), dates);
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}