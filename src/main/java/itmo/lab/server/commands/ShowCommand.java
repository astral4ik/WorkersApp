package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда отображения всех работников.
 * Возвращает список, отсортированный по имени.
 */
public class ShowCommand implements ServerCommand {

    private final WorkersCollection collection;

    public ShowCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args) {
        List<Worker> all = collection.getAll().stream()
                .sorted(Comparator.comparing(Worker::getName))
                .collect(Collectors.toList());
        return new Response(true, "Показано " + all.size() + " работников", all);
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}