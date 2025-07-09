package com.motorph.payrollsystem.ui;

import com.motorph.payrollsystem.csv.EmployeeCSVReader;
import com.motorph.payrollsystem.payrolldata.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.text.DecimalFormat;



public class NewEmployeeFrame extends JFrame {

    private EmployeeListFrame parentFrame; 

    // Input fields for new employee datA
    private JTextField employeeIdField; // Auto-generated and uneditable
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField sssNoField;
    private JTextField philhealthNoField;
    private JTextField tinNoField;
    private JTextField pagibigNoField;

    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");


    public NewEmployeeFrame(EmployeeListFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Add New Employee");
        setSize(500, 400); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        
        //required fields with an asterisk (*)
        employeeIdField = addFormField(formPanel, gbc, row++, "Employee Number:", false);
        employeeIdField.setText(generateNewEmployeeId()); // Populate with new ID on frame creation

        // Required fields (marked with an asterisk *)
        lastNameField = addFormField(formPanel, gbc, row++, "Last Name: *", true);
        firstNameField = addFormField(formPanel, gbc, row++, "First Name: *", true);
        sssNoField = addFormField(formPanel, gbc, row++, "SSS #: *", true);
        philhealthNoField = addFormField(formPanel, gbc, row++, "Philhealth #: *", true);
        tinNoField = addFormField(formPanel, gbc, row++, "TIN #: *", true);
        pagibigNoField = addFormField(formPanel, gbc, row++, "Pag-IBIG #: *", true);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        //buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save New Employee");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        //action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveNewEmployee(); 
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
    }

    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, boolean editable) {
        gbc.gridx = 0; 
        gbc.gridy = row;
        gbc.weightx = 0; 
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1; 
        gbc.gridy = row;
        gbc.weightx = 1.0;
        JTextField field = new JTextField(25);
        field.setEditable(editable);
        panel.add(field, gbc);
        return field;
    }

    private String generateNewEmployeeId() {
        Map<String, Employee> employees = EmployeeCSVReader.loadEmployees();
        int maxId = 10000;

        if (employees != null && !employees.isEmpty()) {
            for (String idString : employees.keySet()) {
                try {
                    int id = Integer.parseInt(idString.trim());
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Non-numeric Employee ID found during ID generation: " + idString);
                }
            }
        }
        return String.valueOf(maxId + 1); 
    }
    
    private void saveNewEmployee() {
        String employeeId = employeeIdField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String sssNo = sssNoField.getText().trim();
        String philhealthNo = philhealthNoField.getText().trim();
        String tinNo = tinNoField.getText().trim();
        String pagibigNo = pagibigNoField.getText().trim();

        //check all required fields
        if (lastName.isEmpty() || firstName.isEmpty() ||
            sssNo.isEmpty() || philhealthNo.isEmpty() || tinNo.isEmpty() || pagibigNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (marked with *).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Assign default values for fields not present in the simplified UI
        String defaultPassword = "password123"; // NOTE: In a real application, passwords should be securely hashed.
        String defaultBirthday = "2000-01-01"; // Default birthday
        String defaultAddress = "N/A";
        String defaultPhoneNumber = "N/A";
        String defaultStatus = "Probationary";
        String defaultPosition = "Staff";
        String defaultSupervisor = "N/A";
        double defaultBasicSalary = 0.00; 
        double defaultRiceSubsidy = 0.00;
        double defaultPhoneAllowance = 0.00;
        double defaultClothingAllowance = 0.00;
        double defaultGrossSemiMonthlyRate = 0.00;
        double defaultHourlyRate = 0.00;
        double defaultDailyRate = 0.00;
        String defaultPayPeriod = "Monthly";

        // constructor
        Employee newEmployee = new Employee(
            employeeId,
            defaultPassword,
            lastName,
            firstName,
            defaultBirthday,       // Default value
            defaultAddress,        // Default value
            defaultPhoneNumber,    // Default value
            sssNo,
            philhealthNo,
            tinNo,
            pagibigNo,
            defaultStatus,         // Default value
            defaultPosition,       // Default value
            defaultSupervisor,     // Default value
            defaultBasicSalary,    // Default value
            defaultRiceSubsidy,    // Default value
            defaultPhoneAllowance, // Default value
            defaultClothingAllowance, // Default value
            defaultGrossSemiMonthlyRate, // Default value
            defaultHourlyRate,     // Default value
            defaultDailyRate,      // Default value
            defaultPayPeriod       // Default value
        );

        try {
            EmployeeCSVReader.saveEmployeeToCSV(newEmployee); 
            JOptionPane.showMessageDialog(this, "New employee added successfully with ID: " + employeeId, "Success", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.refreshEmployeeTable(); 
            dispose(); 
        } catch (Exception e) {
            System.err.println("Unexpected error during employee save: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred while saving employee data. Please check console.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

