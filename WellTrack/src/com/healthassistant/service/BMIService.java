package com.healthassistant.service;

import com.healthassistant.model.UserHealth;
import com.healthassistant.utils.FileManager;
import java.util.List;
import java.util.ArrayList;

public class BMIService {
    private static final String HEALTH_FILE = "data/health_history.csv";

    public static UserHealth record(double heightMeters, double weightKg, int age, String gender) {
        UserHealth uh = new UserHealth(heightMeters, weightKg, age, gender);
        FileManager.append(HEALTH_FILE, uh.toCSV());
        return uh;
    }

    public static List<String[]> readAll() {
        List<String> lines = FileManager.readAllLines(HEALTH_FILE);
        List<String[]> rows = new ArrayList<>();
        for (String l : lines) {
            // timestamp,height,weight,age,gender,bmi,status
            String[] parts = l.split(",");
            if (parts.length >= 7) rows.add(parts);
        }
        return rows;
    }
}
