package ru.job4j.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Wget {
    private static final Logger LOGGER = LogManager.getLogger(ru.job4j.concurrent.Wget.class);

    public static void main(String[] args) {
        Thread thread = new Thread(
                () -> {
                    try {
                        System.out.println("Start loading ... ");
                        for (int i = 0; i <= 100; i++) {
                            Thread.sleep(10);
                            System.out.print("\rLoading : " + i  + "%");
                        }
                        System.out.println();
                        System.out.println("Loaded.");
                    } catch (InterruptedException e) {
                        LOGGER.error("Thread was interrupted: ", e);
                    }
                }
        );
        thread.start();
    }
}
