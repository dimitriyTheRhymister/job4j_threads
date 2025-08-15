package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailNotification {
    private static final Logger LOGGER = Logger.getLogger(EmailNotification.class.getName());
    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        String subject = String.format("Notification %s to email %s", user.getUsername(), user.getEmail());
        String body = String.format("Add a new event to %s", user.getUsername());

        pool.submit(() -> send(subject, body, user.getEmail()));
    }

    public void send(String subject, String body, String email) {
        System.out.println("Sending email to " + email + " with subject: " + subject + " and body: " + body);
    }

    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Thread was interrupted while waiting for tasks to finish.", e);
            pool.shutdownNow();
        }
    }
}













