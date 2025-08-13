package ru.job4j.pool;

import ru.job4j.synchronizers.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(11);

    public ThreadPool(int size) {
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        tasks.poll().run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
    }

    public void work(Runnable task) throws InterruptedException {
        tasks.offer(task);
    }

    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}