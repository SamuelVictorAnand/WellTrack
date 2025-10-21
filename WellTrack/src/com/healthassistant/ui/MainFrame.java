package com.healthassistant.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.healthassistant.service.BMIService;
import com.healthassistant.service.ReminderService;
import com.healthassistant.model.MedicineReminder;
import java.time.LocalTime;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/*
 A professional-looking Swing UI with tabs:
 - Dashboard (BMI input & quick stats)
 - History (table of BMI records)
 - Reminders (list and add reminder)
*/
public class MainFrame extends JFrame {
    private JTextField heightField, weightField, ageField;
    private JComboBox<String> genderBox;
    private JLabel bmiLabel, statusLabel;
    private DefaultTableModel historyModel;
    private DefaultTableModel remindersModel;

    public MainFrame() {
        setTitle("Personal Health & Wellness Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // Top header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 62, 80));
        header.setPreferredSize(new Dimension(800, 70));
        JLabel title = new JLabel("Health & Wellness Assistant");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", createDashboardPanel());
        tabs.addTab("History", createHistoryPanel());
        tabs.addTab("Reminders", createRemindersPanel());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel hLabel = new JLabel("Height (meters):");
        heightField = new JTextField(10);
        JLabel wLabel = new JLabel("Weight (kg):");
        weightField = new JTextField(10);
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(5);
        JLabel gLabel = new JLabel("Gender:");
        genderBox = new JComboBox<>(new String[] {"Male","Female","Other"});

        gbc.gridx = 0; gbc.gridy = 0; form.add(hLabel, gbc);
        gbc.gridx = 1; form.add(heightField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(wLabel, gbc);
        gbc.gridx = 1; form.add(weightField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(ageLabel, gbc);
        gbc.gridx = 1; form.add(ageField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(gLabel, gbc);
        gbc.gridx = 1; form.add(genderBox, gbc);

        JButton calcBtn = new JButton("Calculate BMI & Save");
        calcBtn.addActionListener(e -> onCalculate());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; form.add(calcBtn, gbc);

        JPanel result = new JPanel(new GridLayout(2,1));
        bmiLabel = new JLabel("BMI: -", SwingConstants.CENTER);
        bmiLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusLabel = new JLabel("Status: -", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        result.add(bmiLabel);
        result.add(statusLabel);

        p.add(form, BorderLayout.WEST);
        p.add(result, BorderLayout.CENTER);
        return p;
    }

    private JPanel createHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout());
        historyModel = new DefaultTableModel(new String[] {"Timestamp","Height(m)","Weight(kg)","Age","Gender","BMI","Status"}, 0);
        JTable table = new JTable(historyModel);
        table.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(table);
        p.add(sp, BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadHistory());
        p.add(refresh, BorderLayout.SOUTH);
        loadHistory();
        return p;
    }

    private JPanel createRemindersPanel() {
        JPanel p = new JPanel(new BorderLayout());
        remindersModel = new DefaultTableModel(new String[] {"Medicine","Dosage","Time (HH:mm)"}, 0);
        JTable table = new JTable(remindersModel);
        table.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(table);
        p.add(sp, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton addBtn = new JButton("Add Reminder");
        addBtn.addActionListener(e -> showAddReminderDialog());
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadReminders());
        controls.add(addBtn);
        controls.add(refresh);
        p.add(controls, BorderLayout.SOUTH);

        loadReminders();
        return p;
    }

    private void onCalculate() {
        try {
            double h = Double.parseDouble(heightField.getText().trim());
            double w = Double.parseDouble(weightField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = (String)genderBox.getSelectedItem();
            var uh = BMIService.record(h, w, age, gender);
            bmiLabel.setText("BMI: " + uh.getBmi());
            statusLabel.setText("Status: " + uh.getStatus());
            JOptionPane.showMessageDialog(this, "Saved BMI record ("+uh.getTimestamp()+")", "Saved", JOptionPane.INFORMATION_MESSAGE);
            loadHistory();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHistory() {
        historyModel.setRowCount(0);
        var rows = BMIService.readAll();
        for (var r : rows) {
            historyModel.addRow(new Object[] { r[0], r[1], r[2], r[3], r[4], r[5], r[6] });
        }
    }

    private void loadReminders() {
        remindersModel.setRowCount(0);
        List<com.healthassistant.model.MedicineReminder> list = ReminderService.getInstance().getReminders();
        for (var m : list) {
            remindersModel.addRow(new Object[] { m.getName(), m.getDosage(), m.getTime().toString() });
        }
    }

    private void showAddReminderDialog() {
        JTextField nameF = new JTextField(15);
        JTextField dosageF = new JTextField(10);
        JTextField timeF = new JTextField(5);
        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.add(new JLabel("Medicine Name:")); panel.add(nameF);
        panel.add(new JLabel("Dosage:")); panel.add(dosageF);
        panel.add(new JLabel("Time (HH:mm):")); panel.add(timeF);
        int res = JOptionPane.showConfirmDialog(this, panel, "Add Reminder", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String name = nameF.getText().trim();
                String dosage = dosageF.getText().trim();
                java.time.LocalTime t = java.time.LocalTime.parse(timeF.getText().trim());
                MedicineReminder mr = new MedicineReminder(name, dosage, t);
                ReminderService.getInstance().addReminder(mr);
                JOptionPane.showMessageDialog(this, "Reminder added for " + t.toString(), "Added", JOptionPane.INFORMATION_MESSAGE);
                loadReminders();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Use HH:mm format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
