package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда удаления работников с ID меньше указанного.
 */
public class RemoveLowerCommand implements ServerCommand {

    private final WorkersCollection collection;

    public RemoveLowerCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        int id = ((IdArgument) args).getId();
        int removed = collection.removeLower(id);
        return new Response(true, "Удалено " + removed + " работников", null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}