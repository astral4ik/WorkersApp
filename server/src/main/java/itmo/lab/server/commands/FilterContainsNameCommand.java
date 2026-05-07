package itmo.lab.server.commands;

import itmo.lab.common.Response;
import itmo.lab.common.StringArgument;
import itmo.lab.data.Worker;
import itmo.lab.WorkersCollection;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда фильтрации работников по подстроке в имени (без учёта регистра).
 */
public class FilterContainsNameCommand implements ServerCommand {

    private final WorkersCollection collection;

    public FilterContainsNameCommand(WorkersCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Serializable args, String ownerLogin) {
        String name = ((StringArgument) args).getValue().toLowerCase();
        List<Worker> filtered = collection.getAll().stream()
                .filter(w -> w.getName().toLowerCase().contains(name))
                .collect(Collectors.toList());
        return new Response(true, "Найдено " + filtered.size() + " работников", filtered);
    }

    @Override
    public boolean isModifying() {
        return false;
    }
}
