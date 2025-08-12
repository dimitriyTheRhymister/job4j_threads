package ru.job4j.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache cache;

    @BeforeEach
    void setUp() {
        cache = new Cache();
    }
    @Test
    public void whenAddFind() {
        var base = new Base(1,  "Base", 1);
        cache.add(base);

        Optional<Base> find = cache.findById(base.id());
        assertTrue(find.isPresent(), "Model should be present after update");

        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));

        Optional<Base> find = cache.findById(base.id());
        assertTrue(find.isPresent(), "Model should be present after update");

        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenAddDeleteFind() {
        var base = new Base(1,   "Base", 1);
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    void whenAddNewModelThenReturnsTrue() {
        Base model = new Base(1, "Model1", 1);
        assertTrue(cache.add(model));
    }

    @Test
    void whenAddExistingModelThenReturnsFalse() {
        Base model = new Base(1, "Model1", 1);
        cache.add(model);
        assertFalse(cache.add(model));
    }

    @Test
    void whenUpdateExistingModelWithSameVersionThenReturnsTrue() throws OptimisticException {
        Base model = new Base(1, "Model1", 1);
        cache.add(model);

        Base updatedModel = new Base(1, "Model1", 1);
        assertTrue(cache.update(updatedModel));

        Optional<Base> foundModel = cache.findById(1);
        assertTrue(foundModel.isPresent(), "Model should be present after update");

        assertEquals(2, foundModel.get().version());
    }

    @Test
    void whenUpdateExistingModelWithDifferentVersionThenThrowsException() {
        Base model = new Base(1, "Model1", 1);
        cache.add(model);

        Base updatedModel = new Base(1, "Model1", 2);
        OptimisticException exception = assertThrows(OptimisticException.class,
                () -> cache.update(updatedModel));

        assertEquals("Объект с id 1 не может быть обновлен, версия не совпадает.", exception.getMessage());
    }

    @Test
    void whenDeleteExistingModelThenModelIsNotFound() {
        Base model = new Base(1, "Model1", 1);
        cache.add(model);
        cache.delete(1);

        assertFalse(cache.findById(1).isPresent());
    }

    @Test
    void whenFindByIdNonExistentModelThenReturnsEmptyOptional() {
        assertFalse(cache.findById(1).isPresent());
    }
}