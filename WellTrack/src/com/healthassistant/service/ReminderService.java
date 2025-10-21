package com.healthassistant.service;

import com.healthassistant.model.MedicineReminder;
import com.healthassistant.utils.FileManager;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 Singleton service that loads reminders from data/reminders.csv
 and checks every 30 seconds for due reminders. When a reminder
 time matches the current time (HH:mm), it shows a Swing dialog
 on the EDT and beeps.
*/
public class ReminderService {
    private static final String REMINDERS_FILE = "data/reminders.csv";
    private static ReminderService instance;
    private List<MedicineReminder> reminders = new ArrayList<>();
    private ScheduledExecutorService scheduler;

    private ReminderService() {
        load();
    }

    public static synchronized ReminderService getInstance() {
        if (instance == null) instance = new ReminderService();
        return instance;
    }

    public synchronized void load() {
        reminders.clear();
        List<String> lines = FileManager.readAllLines(REMINDERS_FILE);
        for (String l : lines) {
            MedicineReminder mr = MedicineReminder.fromCSV(l);
            if (mr != null) reminders.add(mr);
        }
    }

    public synchronized List<MedicineReminder> getReminders() {
        return new ArrayList<>(reminders);
    }

    public synchronized void addReminder(MedicineReminder mr) {
        reminders.add(mr);
        FileManager.append(REMINDERS_FILE, mr.toCSV());
    }

    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) return;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::checkDue, 5, 30, TimeUnit.SECONDS);
    }

    private void checkDue() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        String nowStr = now.toString();
        List<MedicineReminder> due = new ArrayList<>();
        synchronized (this) {
            for (MedicineReminder m : reminders) {
                if (m.getTime().equals(now)) due.add(m);
            }
        }
        if (!due.isEmpty()) {
            for (MedicineReminder m : due) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Reminder: Take " + m.getName() + " - " + m.getDosage() + " (" + m.getTime().toString() + ")", "Medicine Reminder", JOptionPane.INFORMATION_MESSAGE);
                    java.awt.Toolkit.getDefaultToolkit().beep();
                });
            }
        }
    }
}
