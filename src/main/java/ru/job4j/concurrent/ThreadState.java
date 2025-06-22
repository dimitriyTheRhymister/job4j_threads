package ru.job4j.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadState {
    private static final Logger LOGGER = LogManager.getLogger(ru.job4j.concurrent.ThreadState.class);

    public static void main(String[] args) {
        Thread first = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        first.setName("First");

        Thread second = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        second.setName("Second");

        System.out.println("State of first thread before start: " + first.getState());
        first.start();
        System.out.println("State of first thread after start: " + first.getState());

        System.out.println("State of second thread before start: " + second.getState());
        second.start();
        System.out.println("State of second thread after start: " + second.getState());

        while (first.isAlive() || second.isAlive()) {
            System.out.println("State of first thread: " + first.getState());
            System.out.println("State of second thread: " + second.getState());
            try {
                first.join();
                second.join();
            } catch (InterruptedException e) {
                LOGGER.error("Thread was interrupted: ", e);
            }
        }

        System.out.println("Final state of first thread: " + first.getState());
        System.out.println("Final state of second thread: " + second.getState());
        System.out.println("The job is done");
    }
}