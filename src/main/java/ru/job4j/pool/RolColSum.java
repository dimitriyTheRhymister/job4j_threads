package ru.job4j.pool;

import java.util.concurrent.CompletableFuture;

public class RolColSum {

    public static class Sums {
        private int rowSum;
        private int colSum;

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }
    }

    public static Sums[] initializeSums(int size) {
        Sums[] sums = new Sums[size];
        for (int i = 0; i < size; i++) {
            sums[i] = new Sums();
        }
        return sums;
    }

    public static void calculateSums(int[][] matrix, Sums[] sums, boolean sumRows) {
        int n = matrix.length;
        if (sumRows) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sums[i].rowSum += matrix[i][j];
                }
            }
        } else {
            for (int j = 0; j < n; j++) {
                for (int[] el : matrix) {
                    sums[j].colSum += el[j];
                }
            }
        }
    }

    public static Sums[] sum(int[][] matrix) {
        if (matrix.length == 0 || matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrix must be square");
        }
        Sums[] sums = initializeSums(matrix.length);

        calculateSums(matrix, sums, true);
        calculateSums(matrix, sums, false);

        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        if (matrix.length == 0 || matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrix must be square");
        }
        Sums[] sums = initializeSums(matrix.length);

        CompletableFuture<Void> rowFuture = CompletableFuture.runAsync(
                () -> calculateSums(matrix, sums, true)
        );
        CompletableFuture<Void> colFuture = CompletableFuture.runAsync(
                () -> calculateSums(matrix, sums, false)
        );

        CompletableFuture.allOf(rowFuture, colFuture).join();
        return sums;
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        System.out.println("Synchronous sums:");
        Sums[] syncSums = sum(matrix);
        for (Sums sum : syncSums) {
            System.out.println("Row sum: " + sum.getRowSum() + ", Col sum: " + sum.getColSum());
        }

        System.out.println("\nAsynchronous sums:");
        Sums[] asyncSums = asyncSum(matrix);
        for (Sums sum : asyncSums) {
            System.out.println("Row sum: " + sum.getRowSum() + ", Col sum: " + sum.getColSum());
        }
    }
}