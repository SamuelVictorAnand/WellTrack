package com.healthassistant;

import javax.swing.SwingUtilities;
import com.healthassistant.ui.MainFrame;
import com.healthassistant.service.ReminderService;

public class Main {
    public static void main(String[] args) {
        // Start reminder background service
        ReminderService.getInstance().start();

        // Start Swing UI on EDT
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
