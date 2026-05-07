package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.IdArgument;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда удаления работника из коллекции по его ID.
 */
public class RemoveKeyCommand implements ServerCommand {

    private final WorkersCollection collection;

    public RemoveKeyCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        int id = ((IdArgument) args).getId();

        if (!collection.isOwner(id, ownerLogin)) {
            return new Response(false, "ERROR: Worker не найден или у вас нет прав на его удаление", null);
        }

        collection.remove(id, ownerLogin);
        return new Response(true, "Worker успешно удалён", null);
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}
