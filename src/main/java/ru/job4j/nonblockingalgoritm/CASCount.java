package ru.job4j.nonblockingalgoritm;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        count.incrementAndGet();
    }

    public int get() {
        return count.get();
    }

    public static void main(String[] args) {
        CASCount casCount = new CASCount();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    casCount.increment();
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Ошибка: поток был прерван во время выполнения. " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Итоговое значение счетчика: " + casCount.get());
    }
}
