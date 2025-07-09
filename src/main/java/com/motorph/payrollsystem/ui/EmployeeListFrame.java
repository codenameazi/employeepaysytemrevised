
package com.motorph.payrollsystem.ui;

import com.motorph.payrollsystem.csv.EmployeeCSVReader;
import com.motorph.payrollsystem.payrolldata.Employee;
import com.motorph.payrollsystem.calculations.PayCalculator;
import com.motorph.payrollsystem.csv.AttendanceCSVReader;
import com.motorph.payrollsystem.payrolldata.AttendanceRecord;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat; 
import java.io.FileWriter; 
import java.io.IOException;
import java.io.PrintWriter;



public class EmployeeListFrame extends JFrame {
    
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private List<Employee> allEmployees;
    private List<AttendanceRecord> allAttendanceRecords;
    
    //search components
    private JTextField searchField;
    private JButton searchButton;
    
    //buttons at the bottom
    private JButton viewEmployeeButton; 
    private JButton newEmployeeButton;
    private JButton updateEmployeeButton;
    private JButton deleteEmployeeButton;
    
    // TextFields for Employee Details 
    private JTextField empIdField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField birthdayField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField sssNoField;
    private JTextField philhealthNoField;
    private JTextField tinNoField;
    private JTextField pagibigNoField;
    private JTextField statusField;
    private JTextField positionField;
    private JTextField supervisorField; 
    private JTextField basicSalaryField;
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;
    private JTextField grossSemiMonthlyRateField;
    private JTextField hourlyRateField;
    private JTextField dailyRateField;
    private JTextField payPeriodField;

    // TextFields for Basic Pay Coverage
    private JTextField basicSalaryCoverField;
    private JTextField riceSubsidyCoverField;
    private JTextField phoneAllowanceCoverField;
    private JTextField clothingAllowanceCoverField;
    private JTextField totalAllowanceCoverField;
    private JTextField sssDeductionCoverField;
    private JTextField philhealthDeductionCoverField;
    private JTextField pagibigDeductionCoverField;
    private JTextField totalDeductionsCoverField;

    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

    public EmployeeListFrame() {
        setTitle("MotorPh Employee Management System");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); //center window
        
        //initialize employee data
        loadEmployeeData();
        allAttendanceRecords = AttendanceCSVReader.loadAttendanceRecords(); // Load attendance records

        //search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Search Employee (ID or Name):"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
        
        //table 
        String[] columnNames = {"Employee No.", "Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pag-IBIG No."};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //make all cells non-editable
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

        //populate table with data
        populateEmployeeTable(allEmployees);

        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        //Employee Details & Basic Pay Coverage
        JPanel detailsAndPayPanel = new JPanel();
        detailsAndPayPanel.setLayout(new BoxLayout(detailsAndPayPanel, BoxLayout.Y_AXIS)); 
        detailsAndPayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //for Employee Details
        JPanel employeeDetailsPanel = new JPanel(new GridBagLayout());
        employeeDetailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details (for Update)"));

        JScrollPane detailsScrollPane = new JScrollPane(employeeDetailsPanel);
        detailsScrollPane.setPreferredSize(new Dimension(450, 400)); 
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        GridBagConstraints gbcDetails = new GridBagConstraints();
        gbcDetails.insets = new Insets(4, 4, 4, 4);
        gbcDetails.fill = GridBagConstraints.HORIZONTAL;
        gbcDetails.anchor = GridBagConstraints.WEST;

        int detailRow = 0;
        //non-editable fields: Employee #, First Name, Birthday, Gov IDs
        empIdField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Employee #:", false);
        lastNameField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Last Name:", true);
        firstNameField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "First Name:", false);
        birthdayField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Birthday (YYYY-MM-DD):", false);
        addressField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Address:", true);
        phoneNumberField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Phone Number:", true);
        sssNoField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "SSS #:", false);
        philhealthNoField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Philhealth #:", false);
        tinNoField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "TIN #:", false);
        pagibigNoField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Pag-IBIG #:", false);
        statusField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Status:", true);
        positionField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Position:", true);
        supervisorField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Immediate Supervisor:", true);
        basicSalaryField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Basic Salary:", true);
        riceSubsidyField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Rice Subsidy:", true);
        phoneAllowanceField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Phone Allowance:", true);
        clothingAllowanceField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Clothing Allowance:", true);
        grossSemiMonthlyRateField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Gross Semi-monthly Rate:", true);
        hourlyRateField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Hourly Rate:", true);
        dailyRateField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Daily Rate:", true);
        payPeriodField = addDetailField(employeeDetailsPanel, gbcDetails, detailRow++, "Pay Period:", true);

        detailsAndPayPanel.add(detailsScrollPane);


        //for Basic Pay Coverage-feature1
        JPanel basicPayCoveragePanel = new JPanel(new GridBagLayout());
        basicPayCoveragePanel.setBorder(BorderFactory.createTitledBorder("Basic Pay Coverage (Selected Employee)"));
        basicPayCoveragePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); 

        GridBagConstraints gbcCoverage = new GridBagConstraints();
        gbcCoverage.insets = new Insets(4, 4, 4, 4);
        gbcCoverage.fill = GridBagConstraints.HORIZONTAL;
        gbcCoverage.anchor = GridBagConstraints.WEST;

        int coverRow = 0;
        basicSalaryCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Basic Salary:", false);
        riceSubsidyCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Rice Subsidy:", false);
        phoneAllowanceCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Phone Allowance:", false);
        clothingAllowanceCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Clothing Allowance:", false);
        totalAllowanceCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Total Allowances:", false);

        coverRow++; // Add a separator row or just increment to create space
        sssDeductionCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "SSS (Employee Share):", false);
        philhealthDeductionCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "PhilHealth (Employee Share):", false);
        pagibigDeductionCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Pag-IBIG (Employee Share):", false);
        totalDeductionsCoverField = addDetailField(basicPayCoveragePanel, gbcCoverage, coverRow++, "Total Statutory Deductions (Employee Share):", false);

        detailsAndPayPanel.add(basicPayCoveragePanel);

        //divide the main content area
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, detailsAndPayPanel);
        mainSplitPane.setDividerLocation(650); 
        add(mainSplitPane, BorderLayout.CENTER);

        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        //view
        viewEmployeeButton = new JButton("View Full Payroll Details");
        viewEmployeeButton.setEnabled(false); //disabled until an employee is selected
        buttonPanel.add(viewEmployeeButton);
        //new
        newEmployeeButton = new JButton("New Employee");
        buttonPanel.add(newEmployeeButton);
        //update
        updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.setEnabled(false); //disabled until an employee is selected
        buttonPanel.add(updateEmployeeButton);
        //delete    
        deleteEmployeeButton = new JButton("Delete Employee");
        deleteEmployeeButton.setEnabled(false); //disabled until an employee is selected
        buttonPanel.add(deleteEmployeeButton);
            
        add(buttonPanel, BorderLayout.SOUTH);
            
            //initial state
        clearEmployeeDetailFields();
        clearBasicPayCoverage();
        enableEmployeeDetailFields(false); 
        enableButtonsOnSelection(false); 

        //event listeners
        
        //search buttons
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable();
            }
        });
        
        //enable-disable buttons based on table selection
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { 
                    int selectedRow = employeeTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
                        Employee selectedEmployee = allEmployees.stream()
                                .filter(emp -> emp.getId().equals(employeeId))
                                .findFirst()
                                .orElse(null);

                        if (selectedEmployee != null) {
                            populateEmployeeDetailFields(selectedEmployee);
                            populateBasicPayCoverage(selectedEmployee);
                            enableEmployeeDetailFields(true); //enable editable fields for the selected employee
                            enableButtonsOnSelection(true); 
                        } else {
                            JOptionPane.showMessageDialog(EmployeeListFrame.this, "Employee data not found in internal list!", "ERROR", JOptionPane.ERROR_MESSAGE);
                            clearEmployeeDetailFields();
                            clearBasicPayCoverage();
                            enableEmployeeDetailFields(false);
                            enableButtonsOnSelection(false);
                        }
                    } else {
                        //no row selected
                        clearEmployeeDetailFields();
                        clearBasicPayCoverage();
                        enableEmployeeDetailFields(false); //disable all fields
                        enableButtonsOnSelection(false); //disable action buttons
                    }
                }
            }
        });
        
        //view button action
        viewEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow != -1) {
                    String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
                    Employee selectedEmployee = allEmployees.stream()
                            .filter(emp -> emp.getId().equals(employeeId))
                            .findFirst()
                            .orElse(null);
                    if (selectedEmployee != null) {
                        PayCoverageFrame payFrame = new PayCoverageFrame(selectedEmployee);
                        payFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(EmployeeListFrame.this, "Employee data not found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(EmployeeListFrame.this,
                            "Please select an employee to view full payroll details.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        //new
        newEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewEmployeeFrame newEmpFrame = new NewEmployeeFrame(EmployeeListFrame.this);
                newEmpFrame.setVisible(true);
            }
        });
        
        //update 
        updateEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });
        
        //delete 
        deleteEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });
    }
    
    
    private JTextField addDetailField(JPanel panel, GridBagConstraints gbc, int row, String labelText, boolean editable) {
        gbc.gridx = 0; 
        gbc.gridy = row;
        gbc.weightx = 0; 
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1; 
        gbc.gridy = row;
        gbc.weightx = 1.0;
        JTextField field = new JTextField(20);
        field.setEditable(editable);
        panel.add(field, gbc);
        gbc.weightx = 0; 
        return field;
    }
    
    
    private void loadEmployeeData() {
        allEmployees = new ArrayList<>(EmployeeCSVReader.loadEmployees().values());
    }
    
    //initial population and refresh table
    public void populateEmployeeTable(List<Employee> employeesToDisplay) {
        tableModel.setRowCount(0); 
        if (employeesToDisplay != null) {
            for (Employee emp : employeesToDisplay) {
                tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilHealthNumber(),
                    emp.getTin(),
                    emp.getPagIbigNumber()
                });
            }
        }
    }
    
    public void refreshEmployeeTable() {
        loadEmployeeData(); 
        filterTable();
        clearEmployeeDetailFields();
        clearBasicPayCoverage();
        enableEmployeeDetailFields(false); //disable fields
        enableButtonsOnSelection(false); //disable action buttons
    }
    
    //filter table based on search
    private void filterTable() {
        String searchText = searchField.getText().trim().toLowerCase();
        List<Employee> filteredEmployees;

        if (searchText.isEmpty()) {
            //if search field is empty, show all employees
            filteredEmployees = allEmployees;
        } else {
            filteredEmployees = allEmployees.stream()
                .filter(emp -> emp.getId().toLowerCase().contains(searchText) ||
                               emp.getFirstName().toLowerCase().contains(searchText) ||
                               emp.getLastName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

            if (filteredEmployees.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No employees found matching '" + searchText + "'.",
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        populateEmployeeTable(filteredEmployees);
        if (employeeTable.getSelectedRow() == -1) {
            clearEmployeeDetailFields();
            clearBasicPayCoverage();
            enableEmployeeDetailFields(false);
            enableButtonsOnSelection(false);
        }
    }
    
    
    private void enableEmployeeDetailFields(boolean enable) {
        //permanently non-editable fields
        empIdField.setEditable(false);
        firstNameField.setEditable(false);
        birthdayField.setEditable(false);
        sssNoField.setEditable(false);
        philhealthNoField.setEditable(false);
        tinNoField.setEditable(false);
        pagibigNoField.setEditable(false);

        //fields that can be enabled/disabled for editing
        lastNameField.setEditable(enable);
        addressField.setEditable(enable);
        phoneNumberField.setEditable(enable);
        statusField.setEditable(enable);
        positionField.setEditable(enable);
        supervisorField.setEditable(enable);
        basicSalaryField.setEditable(enable);
        riceSubsidyField.setEditable(enable);
        phoneAllowanceField.setEditable(enable);
        clothingAllowanceField.setEditable(enable);
        grossSemiMonthlyRateField.setEditable(enable);
        hourlyRateField.setEditable(enable);
        dailyRateField.setEditable(enable);
        payPeriodField.setEditable(enable);
    }


    private void clearEmployeeDetailFields() {
        empIdField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        birthdayField.setText("");
        addressField.setText("");
        phoneNumberField.setText("");
        sssNoField.setText("");
        philhealthNoField.setText("");
        tinNoField.setText("");
        pagibigNoField.setText("");
        statusField.setText("");
        positionField.setText("");
        supervisorField.setText("");
        basicSalaryField.setText("");
        riceSubsidyField.setText("");
        phoneAllowanceField.setText("");
        clothingAllowanceField.setText("");
        grossSemiMonthlyRateField.setText("");
        hourlyRateField.setText("");
        dailyRateField.setText("");
        payPeriodField.setText("");
    }

    private void populateEmployeeDetailFields(Employee emp) {
        empIdField.setText(emp.getId());
        lastNameField.setText(emp.getLastName());
        firstNameField.setText(emp.getFirstName());
        birthdayField.setText(emp.getBirthday());
        addressField.setText(emp.getAddress());
        phoneNumberField.setText(emp.getPhoneNumber());
        sssNoField.setText(emp.getSssNumber());
        philhealthNoField.setText(emp.getPhilHealthNumber());
        tinNoField.setText(emp.getTin());
        pagibigNoField.setText(emp.getPagIbigNumber());
        statusField.setText(emp.getStatus());
        positionField.setText(emp.getPosition());
        supervisorField.setText(emp.getSupervisor());
        //format numbers for display
        basicSalaryField.setText(currencyFormat.format(emp.getBasicSalary()));
        riceSubsidyField.setText(currencyFormat.format(emp.getRiceSubsidy()));
        phoneAllowanceField.setText(currencyFormat.format(emp.getPhoneAllowance()));
        clothingAllowanceField.setText(currencyFormat.format(emp.getClothingAllowance()));
        grossSemiMonthlyRateField.setText(currencyFormat.format(emp.getGrossSemiMonthlyRate()));
        hourlyRateField.setText(currencyFormat.format(emp.getHourlyRate()));
        dailyRateField.setText(currencyFormat.format(emp.getDailyRate()));
        payPeriodField.setText(emp.getPayPeriod());
    }

    private void clearBasicPayCoverage() {
        basicSalaryCoverField.setText("");
        riceSubsidyCoverField.setText("");
        phoneAllowanceCoverField.setText("");
        clothingAllowanceCoverField.setText("");
        totalAllowanceCoverField.setText("");
        sssDeductionCoverField.setText("");
        philhealthDeductionCoverField.setText("");
        pagibigDeductionCoverField.setText("");
        totalDeductionsCoverField.setText("");
    }

    private void populateBasicPayCoverage(Employee emp) {
        PayCalculator tempCalculator = new PayCalculator(emp, allAttendanceRecords, LocalDate.now().getMonthValue(), LocalDate.now().getYear());

        basicSalaryCoverField.setText(currencyFormat.format(emp.getBasicSalary()));
        riceSubsidyCoverField.setText(currencyFormat.format(emp.getRiceSubsidy()));
        phoneAllowanceCoverField.setText(currencyFormat.format(emp.getPhoneAllowance()));
        clothingAllowanceCoverField.setText(currencyFormat.format(emp.getClothingAllowance()));
        totalAllowanceCoverField.setText(currencyFormat.format(tempCalculator.getTotalAllowances()));

        sssDeductionCoverField.setText(currencyFormat.format(tempCalculator.getSssContribution()));
        philhealthDeductionCoverField.setText(currencyFormat.format(tempCalculator.getPhilhealthContribution()));
        pagibigDeductionCoverField.setText(currencyFormat.format(tempCalculator.getPagibigContribution()));
        
        totalDeductionsCoverField.setText(currencyFormat.format(
                tempCalculator.getSssContribution() + tempCalculator.getPhilhealthContribution() + tempCalculator.getPagibigContribution()));
    }

    private void enableButtonsOnSelection(boolean enable) {
        viewEmployeeButton.setEnabled(enable);
        updateEmployeeButton.setEnabled(enable);
        deleteEmployeeButton.setEnabled(enable);
    }
    
    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String originalEmployeeId = empIdField.getText().trim();

        //validate input fields
        if (lastNameField.getText().trim().isEmpty() ||
            basicSalaryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last Name and Basic Salary are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate, dailyRate;
        try {
            basicSalary = currencyFormat.parse(basicSalaryField.getText().replace(",", "")).doubleValue();
            riceSubsidy = currencyFormat.parse(riceSubsidyField.getText().replace(",", "")).doubleValue();
            phoneAllowance = currencyFormat.parse(phoneAllowanceField.getText().replace(",", "")).doubleValue();
            clothingAllowance = currencyFormat.parse(clothingAllowanceField.getText().replace(",", "")).doubleValue();
            grossSemiMonthlyRate = currencyFormat.parse(grossSemiMonthlyRateField.getText().replace(",", "")).doubleValue();
            hourlyRate = currencyFormat.parse(hourlyRateField.getText().replace(",", "")).doubleValue();
            dailyRate = currencyFormat.parse(dailyRateField.getText().replace(",", "")).doubleValue();
        } catch (java.text.ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Basic Salary, Allowances, Gross Semi-monthly Rate, Hourly Rate, and Daily Rate.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (!birthdayField.getText().trim().isEmpty()) {
                LocalDate.parse(birthdayField.getText()); // Throws DateTimeParseException if format is wrong
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Birthday format must be INSEE-MM-DD. Example: 1990-01-15", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //find the employee in the current list and update their properties
        Employee employeeToUpdate = allEmployees.stream()
                .filter(emp -> emp.getId().equals(originalEmployeeId))
                .findFirst()
                .orElse(null);

        if (employeeToUpdate != null) {
            //update the employee object with new values from the fields
            employeeToUpdate.setLastName(lastNameField.getText().trim());
            
            employeeToUpdate.setAddress(addressField.getText().trim());
            employeeToUpdate.setPhoneNumber(phoneNumberField.getText().trim());
            
            employeeToUpdate.setStatus(statusField.getText().trim());
            employeeToUpdate.setPosition(positionField.getText().trim());
            employeeToUpdate.setSupervisor(supervisorField.getText().trim());
            employeeToUpdate.setBasicSalary(basicSalary);
            employeeToUpdate.setRiceSubsidy(riceSubsidy);
            employeeToUpdate.setPhoneAllowance(phoneAllowance);
            employeeToUpdate.setClothingAllowance(clothingAllowance);
            employeeToUpdate.setGrossSemiMonthlyRate(grossSemiMonthlyRate);
            employeeToUpdate.setHourlyRate(hourlyRate);
            employeeToUpdate.setDailyRate(dailyRate);
            employeeToUpdate.setPayPeriod(payPeriodField.getText().trim());
           
            EmployeeCSVReader.saveAllEmployees(allEmployees); 
            JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshEmployeeTable(); 
            
        } else {
            JOptionPane.showMessageDialog(this, "Error: Employee not found in data for update.", "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //get employee ID and name for confirmation message from the table model
        String employeeIdToDelete = (String) tableModel.getValueAt(selectedRow, 0);
        String employeeFirstName = (String) tableModel.getValueAt(selectedRow, 2);
        String employeeLastName = (String) tableModel.getValueAt(selectedRow, 1);
        String employeeFullName = employeeFirstName + " " + employeeLastName;

        //ask for user confirmation before deleting
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee " + employeeFullName + " (ID: " + employeeIdToDelete + ")?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            //filter out the employee to be deleted
            List<Employee> updatedEmployees = allEmployees.stream()
                    .filter(emp -> !emp.getId().equals(employeeIdToDelete))
                    .collect(Collectors.toList());

            EmployeeCSVReader.saveAllEmployees(updatedEmployees); 
            JOptionPane.showMessageDialog(this, "Employee " + employeeFullName + " (ID: " + employeeIdToDelete + ") deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshEmployeeTable(); 
        }
    }

    public static void main(String[] args) {
        // Ensure data directory exists for testing purposes
        File dataDir = new File("data"); // Use "data" directly as it's relative to current working dir for testing
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File employeeCsv = new File("data" + File.separator + "employee_dataV1.csv");
        if (!employeeCsv.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(employeeCsv))) {
                writer.println("Employee #,Password,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-IBIG #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate,Daily Rate,Pay Period");
                writer.println("10001,pass123,Dela Cruz,Juan,1995-01-15,123 Main St,09171234567,90-0000000-0,1234567890,000-000-000,1234-5678-9012,Regular,Staff,Supervisor A,25000.00,1500.00,1000.00,500.00,12500.00,156.25,1250.00,Monthly");
                System.out.println("Created dummy employee_dataV1.csv with header for testing.");
            } catch (IOException e) {
                System.err.println("Error creating dummy employee_dataV1.csv for testing: " + e.getMessage());
            }
        }

        SwingUtilities.invokeLater(() -> {
            new EmployeeListFrame().setVisible(true);
        });
    }
}


