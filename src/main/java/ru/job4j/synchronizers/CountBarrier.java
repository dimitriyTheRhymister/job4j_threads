package ru.job4j.synchronizers;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            if (count >= total) {
                monitor.notifyAll();
            }
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        CountBarrier barrier = new CountBarrier(3);

        Thread waiter = new Thread(() -> {
            System.out.println("Ждущий поток: начинаю ожидание...");
            barrier.await();
            System.out.println("Ждущий поток: условие выполнено, продолжаю!");
        });

        Thread worker1 = new Thread(() -> {
            System.out.println("Работник 1: вызываю count()");
            barrier.count();
        });

        Thread worker2 = new Thread(() -> {
            System.out.println("Работник 2: вызываю count()");
            barrier.count();
        });

        Thread worker3 = new Thread(() -> {
            System.out.println("Работник 3: вызываю count()");
            barrier.count();
        });

        waiter.start();
        worker1.start();
        worker2.start();
        worker3.start();
    }
}
