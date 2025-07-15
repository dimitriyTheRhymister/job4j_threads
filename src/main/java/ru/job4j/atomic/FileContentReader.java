package ru.job4j.atomic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

public final class FileContentReader {
    private final File file;

    public FileContentReader(File file) {
        this.file = file;
    }

    public String read(Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            int data;
            while ((data = reader.read()) != -1) {
                char c = (char) data;
                if (filter.test(c)) {
                    output.append(c);
                }
            }
        }
        return output.toString();
    }
}