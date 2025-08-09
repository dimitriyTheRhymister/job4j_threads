package ru.job4j.synchronizers;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    private final int capacity;

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Ёмкость должна быть > 0");
        }
        this.capacity = capacity;
    }

    public void offer(T value) throws InterruptedException {
        synchronized (this) {
            while (queue.size() >= capacity) {
                this.wait();
            }
            queue.offer(value);
            this.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait();
            }
            T value = queue.poll();
            this.notifyAll();
            return value;
        }
    }

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

        Thread producer = new Thread(() -> {
            try {
                System.out.println("Производитель: добавляю 1");
                queue.offer(1);

                System.out.println("Производитель: добавляю 2");
                queue.offer(2);

                System.out.println("Очередь полна. Следующий offer будет ждать...");
                queue.offer(3);
                Thread.sleep(3000);
                System.out.println("Производитель: добавил 3");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Потребитель: забираю элемент — " + queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}

