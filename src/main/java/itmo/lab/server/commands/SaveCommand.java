package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.server.storage.CollectionStorage;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда сохранения коллекции.
 */
public class SaveCommand implements ServerCommand {

    private final WorkersCollection collection;
    private final CollectionStorage storage;

    public SaveCommand(WorkersCollection collection, CollectionStorage storage) {
        this.collection = collection;
        this.storage = storage;
    }

    @Override
    public Response execute(Serializable args) {
        try {
            storage.save(collection);
            return new Response(true, "Коллекция сохранена", null);
        } catch (Exception e) {
            return new Response(false, "Ошибка сохранения: " + e.getMessage(), null);
        }
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}