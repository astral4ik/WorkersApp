package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда удаления работников с ID больше указанного.
 */
public class RemoveGreaterKeyCommand implements ServerCommand {

    private final WorkersCollection collection;

    public RemoveGreaterKeyCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        int id = ((IdArgument) args).getId();
        int removed = collection.removeGreaterKey(id);
        return new Response(true, "Удалено " + removed + " работников", null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}