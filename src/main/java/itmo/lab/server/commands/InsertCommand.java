package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда добавления нового работника в коллекцию.
 * Если id не указан, автоматически генерируется уникальный ID.
 */
public class InsertCommand implements ServerCommand {

    private final WorkersCollection collection;

    public InsertCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        WorkerArgument arg = (WorkerArgument) args;
        Worker worker = arg.getWorker();

        try {
            collection.insert(worker);
            return new Response(true, "Работник добавлен", null);
        } catch (IllegalArgumentException e) {
            return new Response(false, e.getMessage(), null);
        }
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}