package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда удаления всех работников текущего пользователя из коллекции.
 */
public class ClearCommand implements ServerCommand {

    private final WorkersCollection collection;

    public ClearCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        int count = collection.clear(ownerLogin);
        return new Response(true, "Удалено " + count + " работников пользователя " + ownerLogin, null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}
