package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    private final BiFunction<Base, Base, Boolean> versionCheck = (existing, newModel) ->
            existing.version() == newModel.version();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        Base existingModel = memory.get(model.id());

        if (existingModel == null || !versionCheck.apply(existingModel, model)) {
            throw new OptimisticException("Объект с id " + model.id() + " не может быть обновлен, версия не совпадает.");
        }

        model = new Base(model.id(), model.name(), model.version() + 1);
        memory.put(model.id(), model);
        return true;
    }

    public void delete(int id) {
        memory.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
