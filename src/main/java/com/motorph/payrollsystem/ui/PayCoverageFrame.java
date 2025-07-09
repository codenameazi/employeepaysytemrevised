package com.motorph.payrollsystem.ui;

import com.motorph.payrollsystem.payrolldata.Employee;
import com.motorph.payrollsystem.payrolldata.AttendanceRecord;
import com.motorph.payrollsystem.calculations.PayCalculator;
import com.motorph.payrollsystem.csv.AttendanceCSVReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.text.DecimalFormat;


public class PayCoverageFrame extends JFrame {
    
    private Employee employee;
    private List<AttendanceRecord> allAttendanceRecords;
    
    // TextFields for Employee Basic Details
    private JTextField employeeIdField;
    private JTextField employeeNameField;
    private JTextField periodStartDateField;
    private JTextField periodEndDateField;
    private JTextField positionDepartmentField;
    
    // TextFields for Earnings
    private JTextField monthlyRateField;
    private JTextField dailyRateField;
    private JTextField daysWorkedField;
    private JTextField overtimePayField;
    private JTextField grossIncomeEarningsField;
    
    // TextFields for Allowance
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;
    private JTextField totalAllowanceField;
    
    // TextFields for Deductions
    private JTextField sssDeductionField;
    private JTextField philhealthDeductionField;
    private JTextField pagibigDeductionField;
    private JTextField withholdingTaxField;
    private JTextField totalDeductionsField;
    
    // TextFields for SUMMARY 
    private JTextField grossIncomeSummaryField;
    private JTextField benefitsSummaryField;
    private JTextField totalDeductionsSummaryField;
    private JTextField takeHomePayField;
    
    // Month/Year Selection & Calculate Button 
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JButton calculateButton;
    
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd,yyyy");
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

    public PayCoverageFrame(Employee employee) {
        this.employee = employee;
        this.allAttendanceRecords = AttendanceCSVReader.loadAttendanceRecords();

        setTitle("Employee Details for " + employee.getFullName());
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10)); 

        // Panel for Employee Details
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(5, 5, 5, 5); 
        gbcTop.fill = GridBagConstraints.HORIZONTAL; 

        int row = 0;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Employee ID:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row; gbcTop.weightx = 1.0; employeeIdField = createReadOnlyTextField(employee.getId()); topPanel.add(employeeIdField, gbcTop);

        row++;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Employee Name:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row; gbcTop.weightx = 1.0; employeeNameField = createReadOnlyTextField(employee.getFullName()); topPanel.add(employeeNameField, gbcTop);

        row++;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Position:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row; gbcTop.weightx = 1.0; positionDepartmentField = createReadOnlyTextField(employee.getPosition()); topPanel.add(positionDepartmentField, gbcTop);

        row++;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Period Start Date:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row; gbcTop.weightx = 1.0; periodStartDateField = createReadOnlyTextField("N/A"); topPanel.add(periodStartDateField, gbcTop);

        row++;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Period End Date:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row; gbcTop.weightx = 1.0; periodEndDateField = createReadOnlyTextField("N/A"); topPanel.add(periodEndDateField, gbcTop);

        // Month/Year combo boxes and Calculate Button
        row++;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Select Month:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row;
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(LocalDate.now().getMonthValue() - 1); // Default to current month
        topPanel.add(monthComboBox, gbcTop);

        row++;
        gbcTop.gridx = 0; gbcTop.gridy = row; gbcTop.anchor = GridBagConstraints.WEST; topPanel.add(new JLabel("Select Year:"), gbcTop);
        gbcTop.gridx = 1; gbcTop.gridy = row;
        Integer[] years = {LocalDate.now().getYear() - 1, LocalDate.now().getYear(), LocalDate.now().getYear() + 1}; // Current, prev, next year
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem(LocalDate.now().getYear()); 
        topPanel.add(yearComboBox, gbcTop);

        add(topPanel, BorderLayout.NORTH);
        
        //(Earnings, Allowances, Deductions)
        JPanel mainContentPanel = new JPanel(new GridLayout(2, 2, 10, 10)); 
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Earnings Panel
        JPanel earningsPanel = createSectionPanel("Earnings");
        monthlyRateField = addFieldToPanel(earningsPanel, "Monthly Rate:", 0);
        dailyRateField = addFieldToPanel(earningsPanel, "Daily Rate:", 1);
        daysWorkedField = addFieldToPanel(earningsPanel, "Days Worked:", 2);
        overtimePayField = addFieldToPanel(earningsPanel, "Overtime Pay:", 3);
        grossIncomeEarningsField = addFieldToPanel(earningsPanel, "Gross Income:", 4);
        mainContentPanel.add(earningsPanel);
        
        // Allowances Panel
        JPanel allowancesPanel = createSectionPanel("Allowances");
        riceSubsidyField = addFieldToPanel(allowancesPanel, "Rice Subsidy:", 0);
        phoneAllowanceField = addFieldToPanel(allowancesPanel, "Phone Allowance:", 1);
        clothingAllowanceField = addFieldToPanel(allowancesPanel, "Clothing Allowance:", 2);
        totalAllowanceField = addFieldToPanel(allowancesPanel, "Total Allowance:", 3);
        mainContentPanel.add(allowancesPanel);
        
        // Deductions Panel 
        JPanel deductionsPanel = createSectionPanel("Deductions");
        sssDeductionField = addFieldToPanel(deductionsPanel, "SSS:", 0);
        philhealthDeductionField = addFieldToPanel(deductionsPanel, "PhilHealth:", 1);
        pagibigDeductionField = addFieldToPanel(deductionsPanel, "Pag-IBIG:", 2);
        withholdingTaxField = addFieldToPanel(deductionsPanel, "Withholding Tax:", 3);
        totalDeductionsField = addFieldToPanel(deductionsPanel, "Total Deductions:", 4);
        mainContentPanel.add(deductionsPanel);
        
        // SUMMARY Panel
        JPanel summaryPanel = createSectionPanel("Summary");
        grossIncomeSummaryField = addFieldToPanel(summaryPanel, "Gross Income:", 0);
        benefitsSummaryField = addFieldToPanel(summaryPanel, "Benefits:", 1);
        totalDeductionsSummaryField = addFieldToPanel(summaryPanel, "Total Deductions:", 2);
        takeHomePayField = addFieldToPanel(summaryPanel, "Take Home Pay:", 3);
        mainContentPanel.add(summaryPanel);
        
        // Panel for Calculate Button
        JPanel southPanel = new JPanel(new BorderLayout());
        calculateButton = new JButton("Compute Pay");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndDisplayPay(); 
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        buttonPanel.add(calculateButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH); 

        add(southPanel, BorderLayout.SOUTH);

        //initial calculation and display when the frame is opened
        calculateAndDisplayPay();
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }
    
    private JTextField addFieldToPanel(JPanel panel, String labelText, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        JTextField field = new JTextField(10); 
        field.setEditable(false); 
        panel.add(field, gbc);
        return field;
    }
    
    private JTextField createReadOnlyTextField(String text) {
        JTextField field = new JTextField(text, 15);
        field.setEditable(false);
        return field;
    }
    
    // Method for full calculation 
    private void calculateAndDisplayPay() {
        int selectedMonth = monthComboBox.getSelectedIndex() + 1; // Month is 0-indexed in JComboBox
        int selectedYear = (int) yearComboBox.getSelectedItem();

        try {
            PayCalculator calculator = new PayCalculator(employee, allAttendanceRecords, selectedMonth, selectedYear);

            // Set Period Dates from the calculator
            periodStartDateField.setText(calculator.getPayPeriodStart().format(DATE_FORMATTER));
            periodEndDateField.setText(calculator.getPayPeriodEnd().format(DATE_FORMATTER));

            // Populate Earnings fields
            monthlyRateField.setText(currencyFormat.format(employee.getBasicSalary()));
            dailyRateField.setText(currencyFormat.format(employee.getDailyRate())); 
            daysWorkedField.setText(String.valueOf((int) calculator.getDaysWorked())); 
            overtimePayField.setText(currencyFormat.format(calculator.getOvertimePay()));
            grossIncomeEarningsField.setText(currencyFormat.format(calculator.getGrossIncome()));

            // Populate Allowances fields
            riceSubsidyField.setText(currencyFormat.format(employee.getRiceSubsidy()));
            phoneAllowanceField.setText(currencyFormat.format(employee.getPhoneAllowance()));
            clothingAllowanceField.setText(currencyFormat.format(employee.getClothingAllowance()));
            totalAllowanceField.setText(currencyFormat.format(calculator.getTotalAllowances()));

            // Populate Deductions fields
            sssDeductionField.setText(currencyFormat.format(calculator.getSssContribution()));
            philhealthDeductionField.setText(currencyFormat.format(calculator.getPhilhealthContribution()));
            pagibigDeductionField.setText(currencyFormat.format(calculator.getPagibigContribution()));
            withholdingTaxField.setText(currencyFormat.format(calculator.getWithholdingTax()));
            totalDeductionsField.setText(currencyFormat.format(calculator.getTotalDeductions()));

            // Populate Summary fields
            grossIncomeSummaryField.setText(currencyFormat.format(calculator.getGrossIncome()));
            benefitsSummaryField.setText(currencyFormat.format(calculator.getTotalAllowances()));
            totalDeductionsSummaryField.setText(currencyFormat.format(calculator.getTotalDeductions()));
            takeHomePayField.setText(currencyFormat.format(calculator.getTakeHomePay()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error computing pay: " + e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Clear fields and set to "N/A" on error
            daysWorkedField.setText("N/A");
            overtimePayField.setText("N/A");
            grossIncomeEarningsField.setText("N/A");
            totalAllowanceField.setText("N/A");
            sssDeductionField.setText("N/A");
            philhealthDeductionField.setText("N/A");
            pagibigDeductionField.setText("N/A");
            withholdingTaxField.setText("N/A");
            totalDeductionsField.setText("N/A");
            grossIncomeSummaryField.setText("N/A");
            benefitsSummaryField.setText("N/A");
            totalDeductionsSummaryField.setText("N/A");
            takeHomePayField.setText("N/A");
        }
    }

}


