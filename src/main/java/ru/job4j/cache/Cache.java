package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) {
        BiFunction<Integer, Base, Base> updateLogic = (id, existing) -> {
            if (existing.version() != model.version()) {
                throw new OptimisticException(
                        "Объект с id " + id + " не может быть обновлен, версия не совпадает.");
            }
            return new Base(id, model.name(), model.version() + 1);
        };
        return memory.computeIfPresent(model.id(), updateLogic) != null;
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