package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    public void run() {
        var process = new char[]{'—', '\'', '|', '/'};
        System.out.println("Loading ... ");

        while (!Thread.currentThread().isInterrupted()) {
            for (char c : process) {
                System.out.print("\r load: " + c);

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
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