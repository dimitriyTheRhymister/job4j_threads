package ru.job4j.atomic;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class FileContentWriter {
    private final File file;

    public FileContentWriter(File file) {
        this.file = file;
    }

    public void write(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
}
