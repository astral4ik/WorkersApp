package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда удаления работника по ID.
 */
public class RemoveKeyCommand implements ServerCommand {

    private final WorkersCollection collection;

    public RemoveKeyCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        int id = ((IdArgument) args).getId();
        if (collection.remove(id)) {
            return new Response(true, "Работник удален", null);
        } else {
            return new Response(false, "Работник с ID " + id + " не найден", null);
        }
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}