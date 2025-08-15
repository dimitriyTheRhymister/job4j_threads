package ru.job4j.pool;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RolColSumTest {

    @Test
    void whenMatrixIs2x2ThenSumIsCalculatedCorrectly() {
        int[][] matrix = {
                {1, 2},
                {3, 4}
        };

        RolColSum.Sums[] result = RolColSum.sum(matrix);

        assertEquals(3, result[0].getRowSum());
        assertEquals(7, result[1].getRowSum());

        assertEquals(4, result[0].getColSum());
        assertEquals(6, result[1].getColSum());
    }

    @Test
    void whenMatrixIs3x3ThenSumIsCalculatedCorrectly() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        RolColSum.Sums[] result = RolColSum.sum(matrix);

        assertEquals(6, result[0].getRowSum());
        assertEquals(15, result[1].getRowSum());
        assertEquals(24, result[2].getRowSum());

        assertEquals(12, result[0].getColSum());
        assertEquals(15, result[1].getColSum());
        assertEquals(18, result[2].getColSum());
    }

    @Test
    void whenAsyncSumCalledThenSumsAreCalculatedCorrectly() {
        int[][] matrix = {
                {1, 1, 1},
                {2, 2, 2},
                {3, 3, 3}
        };

        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);

        assertEquals(3, result[0].getRowSum());
        assertEquals(6, result[1].getRowSum());
        assertEquals(9, result[2].getRowSum());

        assertEquals(6, result[0].getColSum());
        assertEquals(6, result[1].getColSum());
        assertEquals(6, result[2].getColSum());
    }

    @Test
    void whenMatrixIs1x1ThenSumIsCalculatedCorrectly() {
        int[][] matrix = {{5}};
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertEquals(5, result[0].getRowSum());
        assertEquals(5, result[0].getColSum());
    }

    @Test
    void whenAsyncSumCalledOnEmptyMatrixThenThrowException() {
        int[][] matrix = {};
        assertThrows(IllegalArgumentException.class, () -> RolColSum.asyncSum(matrix));
    }

    @Test
    void whenMatrixHasNegativeNumbersThenSumIsCalculatedCorrectly() {
        int[][] matrix = {
                {-1, -2},
                {-3, -4}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertEquals(-3, result[0].getRowSum());
        assertEquals(-7, result[1].getRowSum());
        assertEquals(-4, result[0].getColSum());
        assertEquals(-6, result[1].getColSum());
    }
}