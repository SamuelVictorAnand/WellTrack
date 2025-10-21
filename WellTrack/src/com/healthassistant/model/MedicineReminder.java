package com.healthassistant.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MedicineReminder {
    private String name;
    private String dosage;
    private LocalTime time;

    public MedicineReminder(String name, String dosage, LocalTime time) {
        this.name = name;
        this.dosage = dosage;
        this.time = time;
    }

    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public LocalTime getTime() { return time; }

    public String toCSV() {
        return String.format("%s,%s,%s%n", name.replace(",", " "), dosage.replace(",", " "), time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    public static MedicineReminder fromCSV(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) return null;
        try {
            java.time.LocalTime t = java.time.LocalTime.parse(parts[2].trim());
            return new MedicineReminder(parts[0].trim(), parts[1].trim(), t);
        } catch (Exception e) {
            return null;
        }
    }
}
