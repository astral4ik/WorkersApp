package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда удаления всех работников пользователя с ID больше заданного.
 */
public class RemoveGreaterKeyCommand implements ServerCommand {

    private final WorkersCollection collection;

    public RemoveGreaterKeyCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        int id = ((IdArgument) args).getId();
        int removed = collection.removeGreaterKey(id, ownerLogin);
        return new Response(true, "Удалено " + removed + " работников", null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}
