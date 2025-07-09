
package com.motorph.payrollsystem.payrolldata;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration; 
import java.time.format.DateTimeParseException;


public class AttendanceRecord {
    
    private String employeeId;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private double hoursWorked; // = timeOut - timeIn
    
    public AttendanceRecord(String employeeId, String dateStr, String timeInStr, String timeOutStr) {
        this.employeeId = employeeId;
        try {
            this.date = LocalDate.parse(dateStr);
            this.timeIn = LocalTime.parse(timeInStr);
            this.timeOut = LocalTime.parse(timeOutStr);
            calculateHoursWorked(); 
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date or time for employee " + employeeId +
                               " (Date: " + dateStr + ", Time In: " + timeInStr + ", Time Out: " + timeOutStr +
                               "). Setting hours worked to 0. Error: " + e.getMessage());
            this.hoursWorked = 0; 
        }
    }
    
    // constructor 
    public AttendanceRecord(String employeeId, LocalDate date, LocalTime timeIn, LocalTime timeOut, double hoursWorked) {
        this.employeeId = employeeId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hoursWorked = hoursWorked;
    }
    
    private void calculateHoursWorked() {
        if (timeIn != null && timeOut != null) {
            long minutes = Duration.between(timeIn, timeOut).toMinutes();
            this.hoursWorked = minutes / 60.0; 

            if (this.hoursWorked > 8) {
                this.hoursWorked = Math.max(0, this.hoursWorked - 1); 
            }
        } else {
            this.hoursWorked = 0; 
        }
    }
    
    public String getEmployeeId() { return employeeId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTimeIn() { return timeIn; }
    public LocalTime getTimeOut() { return timeOut; }
    public double getHoursWorked() { return hoursWorked; }
    
}


