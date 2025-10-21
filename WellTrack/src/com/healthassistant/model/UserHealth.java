package com.healthassistant.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserHealth {
    private double heightMeters;
    private double weightKg;
    private int age;
    private String gender;
    private double bmi;
    private String status;
    private String timestamp;

    public UserHealth(double heightMeters, double weightKg, int age, String gender) {
        this.heightMeters = heightMeters;
        this.weightKg = weightKg;
        this.age = age;
        this.gender = gender;
        this.bmi = computeBMI();
        this.status = categorize();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private double computeBMI() {
        if (heightMeters <= 0) return 0;
        return Math.round((weightKg / (heightMeters * heightMeters)) * 10.0) / 10.0;
    }

    private String categorize() {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 24.9) return "Normal";
        if (bmi < 29.9) return "Overweight";
        return "Obese";
    }

    public double getHeightMeters() { return heightMeters; }
    public double getWeightKg() { return weightKg; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getBmi() { return bmi; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }

    public String toCSV() {
        return String.format("%s,%.2f,%.2f,%d,%s,%.1f,%s%n",
            timestamp, heightMeters, weightKg, age, gender, bmi, status);
    }
}
