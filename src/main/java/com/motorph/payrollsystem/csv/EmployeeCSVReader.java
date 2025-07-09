
package com.motorph.payrollsystem.csv;

import com.motorph.payrollsystem.MainApp; 
import com.motorph.payrollsystem.payrolldata.Employee;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.io.File;
import javax.swing.JOptionPane; 


public class EmployeeCSVReader {
    
    private static final String CSV_FILE_PATH = MainApp.DATA_DIRECTORY + File.separator + MainApp.EMPLOYEE_CSV_FILENAME;

    public static Map<String, Employee> loadEmployees() {
        Map<String, Employee> employees = new HashMap<>();
        String line;
        File csvFile = new File(CSV_FILE_PATH);

        if (!csvFile.exists()) {
            System.err.println("Error: Employee CSV file not found at " + CSV_FILE_PATH);
            JOptionPane.showMessageDialog(null, "Employee data file not found: " + CSV_FILE_PATH, "File Not Found", JOptionPane.ERROR_MESSAGE);
            return employees; //return empty map if file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); //skip header row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length >= 22) { //expecting 22 columns
                    try {
                        String id = data[0].trim();
                        String password = data[1].trim(); 
                        String lastName = data[2].trim();
                        String firstName = data[3].trim();
                        String birthday = data[4].trim();
                        String address = data[5].trim();
                        String phoneNumber = data[6].trim();
                        String sssNo = data[7].trim();
                        String philhealthNo = data[8].trim();
                        String tin = data[9].trim();
                        String pagibigNo = data[10].trim();
                        String status = data[11].trim();
                        String position = data[12].trim();
                        String supervisor = data[13].trim(); 
                        double basicSalary = Double.parseDouble(data[14].trim());
                        double riceSubsidy = Double.parseDouble(data[15].trim());
                        double phoneAllowance = Double.parseDouble(data[16].trim());
                        double clothingAllowance = Double.parseDouble(data[17].trim());
                        double grossSemiMonthlyRate = Double.parseDouble(data[18].trim()); 
                        double hourlyRate = Double.parseDouble(data[19].trim()); 
                        double dailyRate = Double.parseDouble(data[20].trim()); 
                        String payPeriod = data[21].trim(); // New field

                        employees.put(id, new Employee(id, password, lastName, firstName, birthday, address, phoneNumber,
                                sssNo, philhealthNo, tin, pagibigNo, status, position, supervisor,
                                basicSalary, riceSubsidy, phoneAllowance, clothingAllowance,
                                grossSemiMonthlyRate, hourlyRate, dailyRate, payPeriod));

                    } catch (NumberFormatException e) {
                        System.err.println("Skipping row due to number format error: " + line + ". Details: " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Skipping row due to missing data (too few columns, expected 22+): " + line + ". Details: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error processing line: " + line + ". Details: " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping incomplete row (expected 22+ columns, found " + data.length + "): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employees from CSV: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading employee data from file. See console for details.", "Read Error", JOptionPane.ERROR_MESSAGE);
        }
        return employees;
    }
    
    public static void saveEmployeeToCSV(Employee employeeToSave) {
        File csvFile = new File(CSV_FILE_PATH);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true))) {
            bw.newLine();

            bw.write(employeeToSave.toCsvLine());
            System.out.println("Employee " + employeeToSave.getId() + " successfully appended to CSV.");
        } catch (IOException e) {
            System.err.println("Error appending employee to CSV: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save new employee data. Please check console for details and file permissions.", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void saveAllEmployees(List<Employee> employees) {
        File csvFile = new File(CSV_FILE_PATH);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, false))) {
            bw.write("Employee #,Password,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-IBIG #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate,Daily Rate,Pay Period\n");

            for (Employee emp : employees) {
                bw.write(emp.toCsvLine() + "\n"); 
            }
            System.out.println("All employees list successfully saved to CSV.");
        } catch (IOException e) {
            System.err.println("Error saving all employees to CSV: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save all employee data to file. Check console for details.", "Save All Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


