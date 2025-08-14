package ru.job4j.pool;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParallelSearchTest {

    @Test
    void testSearchWithIntegerArray() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 7);
        assertEquals(6, search.search(), "Индекс найденного элемента должен быть 6.");
    }

    @Test
    void testSearchWithStringArray() {
        String[] array = {"apple", "banana", "cherry", "date", "fig", "grape"};
        ParallelSearch<String> search = new ParallelSearch<>(array, "fig");
        assertEquals(4, search.search(), "Индекс найденного элемента должен быть 4.");
    }

    @Test
    void testSearchWithCustomObjects() {
        User[] users = {
                new User("Alice", "alice@example.com"),
                new User("Bob", "bob@example.com"),
                new User("Charlie", "charlie@example.com")
        };
        ParallelSearch<User> search = new ParallelSearch<>(users, new User("Bob", "bob@example.com"));
        assertEquals(1, search.search(), "Индекс найденного пользователя должен быть 1.");
    }

    @Test
    void testSearchWithSmallArrayLinearSearch() {
        Integer[] array = {1, 2, 3};
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 2);
        assertEquals(1, search.search(), "Индекс найденного элемента должен быть 1.");
    }

    @Test
    void testSearchWithLargeArrayRecursiveSearch() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < 100; i++) {
            array[i] = i;
        }
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 50);
        assertEquals(50, search.search(), "Индекс найденного элемента должен быть 50.");
    }

    @Test
    void testElementNotFoundInIntegerArray() {
        Integer[] array = {1, 2, 3, 4, 5};
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 6);
        assertEquals(-1, search.search(), "Элемент не найден, возвращается -1.");
    }

    @Test
    void testElementNotFoundInStringArray() {
        String[] array = {"apple", "banana", "cherry"};
        ParallelSearch<String> search = new ParallelSearch<>(array, "orange");
        assertEquals(-1, search.search(), "Элемент не найден, возвращается -1.");
    }

    @Test
    void testElementNotFoundInCustomObjects() {
        User[] users = {
                new User("Alice", "alice@example.com"),
                new User("Bob", "bob@example.com")
        };
        ParallelSearch<User> search = new ParallelSearch<>(users, new User("Charlie", "charlie@example.com"));
        assertEquals(-1, search.search(), "Элемент не найден, возвращается -1.");
    }
}