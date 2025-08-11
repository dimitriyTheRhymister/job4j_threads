package ru.job4j.buffer;

import ru.job4j.synchronizers.SimpleBlockingQueue;

public class ParallelSearch {

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

        Thread consumer = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer value = queue.poll();
                    if (value == null) {
                        System.out.println("Потребитель: получен сигнал завершения. Завершаюсь.");
                        break;
                    }
                    System.out.println("Потребитель: обработано " + value);
                }
            } catch (InterruptedException e) {
                System.out.println("Потребитель: был прерван.");
                Thread.currentThread().interrupt();
            }
        });

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i != 3; i++) {
                    System.out.println("Производитель: отправляю " + i);
                    Thread.sleep(500);
                    queue.offer(i);
                    Thread.sleep(500);
                }

                queue.offer(null);
                System.out.println("Производитель: отправлен сигнал завершения (null).");
            } catch (InterruptedException e) {
                System.out.println("Производитель: был прерван.");
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        producer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Основной поток прерван.");
        }

        System.out.println("Программа завершена.");
    }
}