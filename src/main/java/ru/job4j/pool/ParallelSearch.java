package ru.job4j.pool;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelSearch<T> {
    private final T[] array;
    private final T target;

    public ParallelSearch(T[] array, T target) {
        this.array = array;
        this.target = target;
    }

    public int search() {
        if (array.length <= 10) {
            return linearSearch(0, array.length);
        } else {
            try (ForkJoinPool pool = new ForkJoinPool()) {
                return pool.invoke(new SearchTask(0, array.length));
            }
        }
    }

    private int linearSearch(int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    private class SearchTask extends RecursiveTask<Integer> {
        private final int start;
        private final int end;

        public SearchTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int length = end - start;
            if (length <= 10) {
                return linearSearch(start, end);
            }

            int mid = start + length / 2;
            SearchTask leftTask = new SearchTask(start, mid);
            SearchTask rightTask = new SearchTask(mid, end);

            leftTask.fork();
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();

            return leftResult != -1 ? leftResult : rightResult;
        }
    }
}
