package ru.job4j.my;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyTest {
    @Test
    void test() {
        int expected = 3;
        My test = new My();
        int result = test.sum(1, 2);
        assertEquals(expected, result);
    }
}