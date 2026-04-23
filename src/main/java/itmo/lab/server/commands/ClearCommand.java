package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда очистки коллекции.
 */
public class ClearCommand implements ServerCommand {

    private final WorkersCollection collection;

    /**
     * Конструктор команды.
     * @param collection коллекция работников
     */
    public ClearCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        collection.clear();
        return new Response(true, "Коллекция очищена", null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}