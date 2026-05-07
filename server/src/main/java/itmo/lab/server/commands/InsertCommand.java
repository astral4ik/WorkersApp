package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.WorkerArgument;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;

/**
 * Команда добавления нового работника в коллекцию.
 */
public class InsertCommand implements ServerCommand {

    private final WorkersCollection collection;

    public InsertCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        WorkerArgument arg = (WorkerArgument) args;
        Worker worker = (Worker) arg.getWorker();

        try {
            long id = collection.insert(worker, ownerLogin);
            return new Response(true, "Работник добавлен с ID " + id, null);
        } catch (IllegalArgumentException e) {
            return new Response(false, e.getMessage(), null);
        }
    }

    @Override
    public boolean isModifying() {
        return true;
    }
}
