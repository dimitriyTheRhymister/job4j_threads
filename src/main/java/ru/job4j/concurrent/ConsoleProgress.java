package ru.job4j.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleProgress implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ConsoleProgress.class);

    public void run() {
        var process = new char[]{'—', '\'', '|', '/'};
        System.out.println("Loading ... ");

        while (!Thread.currentThread().isInterrupted()) {
            for (char c : process) {
                System.out.print("\r load: " + c);

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    LOGGER.error("Thread was interrupted: ", e);
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        progress.interrupt();
    }
}