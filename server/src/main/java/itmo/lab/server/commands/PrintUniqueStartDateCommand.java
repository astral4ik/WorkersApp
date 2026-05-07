package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.WorkersCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Команда вывода уникальных дат начала работы среди всех работников коллекции.
 */
public class PrintUniqueStartDateCommand implements ServerCommand {

    private final WorkersCollection collection;

    public PrintUniqueStartDateCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        List<?> dates = collection.getUniqueStartDates();
        return new Response(true, "Уникальных дат начала: " + dates.size(), dates);
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}
