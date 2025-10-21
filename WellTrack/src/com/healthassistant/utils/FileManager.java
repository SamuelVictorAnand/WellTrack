package com.healthassistant.utils;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class FileManager {
    public static void append(String pathStr, String content) {
        try {
            Path p = Paths.get(pathStr);
            if (!Files.exists(p.getParent())) Files.createDirectories(p.getParent());
            if (!Files.exists(p)) Files.createFile(p);
            Files.write(p, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readAllLines(String pathStr) {
        try {
            Path p = Paths.get(pathStr);
            if (!Files.exists(p)) return new ArrayList<>();
            return Files.readAllLines(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
