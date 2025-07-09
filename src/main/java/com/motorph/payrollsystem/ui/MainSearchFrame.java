package com.motorph.payrollsystem.ui;

import com.motorph.payrollsystem.payrolldata.Employee;
import com.motorph.payrollsystem.csv.EmployeeCSVReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MainSearchFrame extends JFrame{
    
    private JTextField searchField;
    private JButton searchButton;
    private Map<String, Employee> employees; // To store loaded employee data

    public MainSearchFrame() {
        // Load employee data once when the search frame is initialized
        employees = EmployeeCSVReader.loadEmployees();

        setTitle("MotorPH Employee Search");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Employee ID Label
        JLabel searchLabel = new JLabel("Enter Employee ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(searchLabel, gbc);

        // Search Text Field
        searchField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(searchField, gbc);

        // Search Button
        searchButton = new JButton("Search");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });
        add(searchButton, gbc);
    }

    private void handleSearch() {
        String employeeId = searchField.getText().trim();

        // 1. Exception handling for input (empty or invalid format if applicable)
        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an Employee ID.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return; // Stop processing
        }
       

        Employee foundEmployee = employees.get(employeeId);

        if (foundEmployee != null) {
            // Employee found, open the Pay Coverage Display Frame
            PayCoverageFrame payFrame = new PayCoverageFrame(foundEmployee);
            payFrame.setVisible(true);
            // Optionally hide the search frame
            this.setVisible(false);
        } else {
            // Employee not found
            JOptionPane.showMessageDialog(this,
                    "Employee with ID '" + employeeId + "' not found.",
                    "Employee Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    
}


