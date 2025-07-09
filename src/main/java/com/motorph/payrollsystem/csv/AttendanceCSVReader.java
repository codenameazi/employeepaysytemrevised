
package com.motorph.payrollsystem.csv;

import com.motorph.payrollsystem.MainApp; 
import com.motorph.payrollsystem.payrolldata.AttendanceRecord;

import javax.swing.JOptionPane; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File; 
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class AttendanceCSVReader {

    private static final String CSV_FILE_PATH = MainApp.DATA_DIRECTORY + File.separator + MainApp.ATTENDANCE_CSV_FILENAME;

    public static List<AttendanceRecord> loadAttendanceRecords() {
        List<AttendanceRecord> records = new ArrayList<>();
        String line;
        File csvFile = new File(CSV_FILE_PATH);

        if (!csvFile.exists()) {
            System.err.println("Error: Attendance CSV file not found at " + CSV_FILE_PATH);
            JOptionPane.showMessageDialog(null, "Attendance data file not found: " + CSV_FILE_PATH, "File Not Found", JOptionPane.ERROR_MESSAGE);
            return records; //Return empty list if file not found
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            reader.readLine(); //skip header row
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Split by comma
                
                //expected columns: Employee #,Date,Time In,Time Out (4 columns)
                if (data.length >= 4) {
                    try {
                        String employeeId = data[0].trim();
                        String dateStr = data[1].trim();
                        String timeInStr = data[2].trim();
                        String timeOutStr = data[3].trim();

                        records.add(new AttendanceRecord(employeeId, dateStr, timeInStr, timeOutStr));

                    } catch (DateTimeParseException e) {
                        System.err.println("Skipping attendance record due to invalid date/time format in line: " + line + ". Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Skipping attendance record due to unexpected error in line: " + line + ". Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping malformed attendance CSV line (expected 4+ fields, found " + data.length + "): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading attendance data from " + CSV_FILE_PATH + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading attendance data from file. See console for details.", "Read Error", JOptionPane.ERROR_MESSAGE);
        }
        return records;
    }
}


