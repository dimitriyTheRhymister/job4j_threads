package ru.job4j.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class Wget implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Wget.class);
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp4Wget.xml");
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            LOGGER.error("Invalid URL provided: {}", url, e);
            return;
        }

        try (InputStream inputStream = uri.toURL().openStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                var downloadAt = System.nanoTime();
                long startTime = System.currentTimeMillis();

                outputStream.write(buffer, 0, bytesRead);
                System.out.println("Read 1024 bytes : " + (System.nanoTime() - downloadAt) + " nano.");

                long elapsedTime = System.currentTimeMillis() - startTime;
                long sleepTime = (1000L * bytesRead) / speed - elapsedTime;

                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error during file download", e);
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println("Usage: java Wget <url> <speed>");
            return;
        }
        String url = args[0];
        int speed;

        try {
            speed = Integer.parseInt(args[1]);
            if (speed <= 0) {
                throw new NumberFormatException("Speed must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid speed: " + args[1]);
            return;
        }

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}