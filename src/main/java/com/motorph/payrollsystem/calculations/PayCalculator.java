
package com.motorph.payrollsystem.calculations;

import com.motorph.payrollsystem.payrolldata.Employee;
import com.motorph.payrollsystem.payrolldata.AttendanceRecord;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;


public class PayCalculator {
    
    private Employee employee;
    private List<AttendanceRecord> allAttendanceRecords;
    private int month;
    private int year;
    
    //calculated values
    private double daysWorked;
    private double overtimePay;
    private double grossIncome;
    private double totalAllowances;
    private double sssContribution;
    private double philhealthContribution;
    private double pagibigContribution;
    private double withholdingTax;
    private double totalDeductions;
    private double takeHomePay;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    
    public PayCalculator(Employee employee, List<AttendanceRecord> allAttendanceRecords, int month, int year) {
        this.employee = employee;
        this.allAttendanceRecords = allAttendanceRecords;
        this.month = month;
        this.year = year;

        //calculate pay period start and end dates for the given month/year.
        this.payPeriodStart = LocalDate.of(year, month, 1);
        this.payPeriodEnd = payPeriodStart.with(TemporalAdjusters.lastDayOfMonth());

        //perform all calculations immediately.
        calculateAllPayComponents();
    }
    
    private void calculateAllPayComponents() {
        // 1. Calculate attendance fields first
        this.daysWorked = calculateDaysWorked();
        this.overtimePay = calculateOvertimePay();

        // 2. Calculate Gross Income and total allowances
        // Gross income from daily rate * days worked + overtime pay
        this.grossIncome = (this.daysWorked * employee.getDailyRate()) + this.overtimePay;
        this.totalAllowances = calculateTotalAllowances();

        if (this.daysWorked == 0) {
            this.sssContribution = 0.0;
            this.philhealthContribution = 0.0;
            this.pagibigContribution = 0.0;
        } else {
            this.sssContribution = calculateSssDeduction(employee.getBasicSalary());
            this.philhealthContribution = calculatePhilhealthDeduction(employee.getBasicSalary());
            this.pagibigContribution = calculatePagibigDeduction(employee.getBasicSalary());
        }

        // Total contributions (excluding tax)
        double initialDeductions = this.sssContribution + this.philhealthContribution + this.pagibigContribution;

        // Taxable income = (Gross Income + Total Allowances) - initialDeductions
        double taxableIncomeForTax = (this.grossIncome + this.totalAllowances) - initialDeductions;
        this.withholdingTax = calculateWithholdingTax(taxableIncomeForTax);

        // Calculate TotalDeductions
        this.totalDeductions = initialDeductions + this.withholdingTax;

        // Take home pay = Gross Income + Total Allowances - Total Deductions
        this.takeHomePay = (this.grossIncome + this.totalAllowances) - this.totalDeductions;
    }
    
    //attendance and earnings calculations
    private double calculateDaysWorked() {
        return allAttendanceRecords.stream()
                .filter(ar -> ar.getEmployeeId().equals(employee.getId()) &&
                        ar.getDate().getMonthValue() == month &&
                        ar.getDate().getYear() == year)
                .filter(ar -> ar.getDate().getDayOfWeek() != DayOfWeek.SATURDAY &&
                        ar.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
                .map(AttendanceRecord::getDate)
                .distinct() // Count distinct dates to avoid double-counting if multiple entries for one day
                .count();
    }

   
    //calculate overtime
    private double calculateOvertimePay() {
        double totalOvertimeHours = 0.0;
        double regularWorkHours = 8.0;

        List<AttendanceRecord> employeeMonthlyAttendance = allAttendanceRecords.stream()
                .filter(ar -> ar.getEmployeeId().equals(employee.getId()) &&
                        ar.getDate().getMonthValue() == month &&
                        ar.getDate().getYear() == year)
                .filter(ar -> ar.getDate().getDayOfWeek() != DayOfWeek.SATURDAY &&
                        ar.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
                .collect(Collectors.toList());

        for (AttendanceRecord record : employeeMonthlyAttendance) {
            try {
                LocalTime timeIn = record.getTimeIn();
                LocalTime timeOut = record.getTimeOut();

                double hoursWorked = calculateHoursWorked(timeIn, timeOut);

                double dailyOvertime = hoursWorked - regularWorkHours;
                if (dailyOvertime < 0) {
                    dailyOvertime = 0; // No negative overtime
                }
                totalOvertimeHours += dailyOvertime;
            } catch (Exception e) {
                System.err.println("Error parsing time for employee " + employee.getId() +
                                   " on " + record.getDate() + ": " + e.getMessage() +
                                   ". Skipping this attendance record for overtime calculation.");
            }
        }

        // Only calculate overtime pay if there are actual days worked and positive overtime hours.
        if (this.daysWorked > 0 && totalOvertimeHours > 0) {
            double overtimeRate = employee.getHourlyRate() * 1.25; // 125% of hourly rate
            return totalOvertimeHours * overtimeRate;
        } else {
            return 0.0;
        }
    }
         
    private double calculateHoursWorked(LocalTime timeIn, LocalTime timeOut) {
        if (timeOut.isBefore(timeIn)) {
            return 0.0;
        }
        Duration duration = Duration.between(timeIn, timeOut);
        double hours = duration.toMinutes() / 60.0;

        if (hours > 4) {
            hours -= 1.0;
        }
        return hours;
    }
    
    private double calculateTotalAllowances() {
        // If no days worked, then benefits are zero as well.
        if (this.daysWorked == 0) {
            return 0.0;
        }
        return employee.getRiceSubsidy() + employee.getPhoneAllowance() + employee.getClothingAllowance();
    }
    
    // CONTRIBUTIONS
    // sss contributions
    public double calculateSssDeduction(double monthlyBasicSalary) {
        if (this.daysWorked == 0) {
            return 0.0;
        }
        if (monthlyBasicSalary < 3250) return 135.00;
        if (monthlyBasicSalary <= 3750) return 157.50;
        if (monthlyBasicSalary <= 4250) return 180.00;
        if (monthlyBasicSalary <= 4750) return 202.50;
        if (monthlyBasicSalary <= 5250) return 225.00;
        if (monthlyBasicSalary <= 5750) return 247.50;
        if (monthlyBasicSalary <= 6250) return 270.00;
        if (monthlyBasicSalary <= 6750) return 292.50;
        if (monthlyBasicSalary <= 7250) return 315.00;
        if (monthlyBasicSalary <= 7750) return 337.50;
        if (monthlyBasicSalary <= 8250) return 360.00;
        if (monthlyBasicSalary <= 8750) return 382.50;
        if (monthlyBasicSalary <= 9250) return 405.00;
        if (monthlyBasicSalary <= 9750) return 427.50;
        if (monthlyBasicSalary <= 10250) return 450.00;
        if (monthlyBasicSalary <= 10750) return 472.50;
        if (monthlyBasicSalary <= 11250) return 495.00;
        if (monthlyBasicSalary <= 11750) return 517.50;
        if (monthlyBasicSalary <= 12250) return 540.00;
        if (monthlyBasicSalary <= 12750) return 562.50;
        if (monthlyBasicSalary <= 13250) return 585.00;
        if (monthlyBasicSalary <= 13750) return 607.50;
        if (monthlyBasicSalary <= 14250) return 630.00;
        if (monthlyBasicSalary <= 14750) return 652.50;
        if (monthlyBasicSalary <= 15250) return 675.00;
        if (monthlyBasicSalary <= 15750) return 697.50;
        if (monthlyBasicSalary <= 16250) return 720.00;
        if (monthlyBasicSalary <= 16750) return 742.50;
        if (monthlyBasicSalary <= 17250) return 765.00;
        if (monthlyBasicSalary <= 17750) return 787.50;
        if (monthlyBasicSalary <= 18250) return 810.00;
        if (monthlyBasicSalary <= 18750) return 832.50;
        if (monthlyBasicSalary <= 19250) return 855.00;
        if (monthlyBasicSalary <= 19750) return 877.50;
        if (monthlyBasicSalary <= 20250) return 900.00;
        if (monthlyBasicSalary <= 20750) return 922.50;
        if (monthlyBasicSalary <= 21250) return 945.00;
        if (monthlyBasicSalary <= 21750) return 967.50;
        if (monthlyBasicSalary <= 22250) return 990.00;
        if (monthlyBasicSalary <= 22750) return 1012.50;
        if (monthlyBasicSalary <= 23250) return 1035.00;
        if (monthlyBasicSalary <= 23750) return 1057.50;
        if (monthlyBasicSalary <= 24250) return 1080.00;
        if (monthlyBasicSalary <= 24750) return 1102.50;
        return 1125.00; // Over 24,750
    }
    
    // philhealth contribution
    public double calculatePhilhealthDeduction(double monthlyBasicSalary) {
        if (this.daysWorked == 0) {
            return 0.0; }
        double premiumRate = 0.03; // 3%
        double monthlyPremium;

        if (monthlyBasicSalary <= 10000) { //monthly Basic Salary 10,000 and below
            monthlyPremium = 300.00; //fixed premium for this bracket
        } else if (monthlyBasicSalary < 60000) { // 10,000.01 to 59,999.99
            monthlyPremium = monthlyBasicSalary * premiumRate;
            //cap the monthly premium at 1,800 if it exceeds for this bracket
            if (monthlyPremium > 1800.00) {
                monthlyPremium = 1800.00;
            }
        } else { // basicSalary >= 60000
            monthlyPremium = 1800.00; //max premium for salaries 60,000 and above
        }
        return monthlyPremium * 0.50; //employee's share is 50%
    }
    
    // pagibig contribution
    public double calculatePagibigDeduction(double monthlyBasicSalary) {
        if (this.daysWorked == 0) {
            return 0.0;}
        double employeeContributionRate;
        if (monthlyBasicSalary >= 1000 && monthlyBasicSalary <= 1500) {
            employeeContributionRate = 0.01; // 1%
        } else if (monthlyBasicSalary > 1500) {
            employeeContributionRate = 0.02; // 2%
        } else {
            return 0.0; // If basicSalary is below 1000, assuming no Pag-IBIG contribution.
        }

        double contribution = monthlyBasicSalary * employeeContributionRate;
        return Math.min(contribution, 100.00); // Maximum contribution is 100
    }
    
    // withholding tax
    public double calculateWithholdingTax(double taxableIncome) {
        // If taxable income is 0 or negative, or no days worked, withholding tax is 0.
        if (taxableIncome <= 0 || this.daysWorked == 0) {
            return 0.0;}
        double tax = 0.0;
        // Formula: tax = addtax + (taxable income - taxbase) * taxrate
        if (taxableIncome <= 20832) { // 20,832 and below
            tax = 0.0; // NO withholding tax
        } else if (taxableIncome <= 33333) { // 20,833 to below 33,333
            tax = (taxableIncome - 20833) * 0.20; // 20% in excess of 20,833
        } else if (taxableIncome <= 66667) { // 33,333 to below 66,667
            tax = 2500 + ((taxableIncome - 33333) * 0.25); // 2,500 plus 25% in excess of 33,333
        } else if (taxableIncome <= 166667) { // 66,667 to below 166,667
            tax = 10833.33 + ((taxableIncome - 66667) * 0.30); // 10,833.33 plus 30% in excess of 66,667
        } else if (taxableIncome <= 666667) { // 166,667 to below 666,667
            tax = 40833.33 + ((taxableIncome - 166667) * 0.32); // 40,833.33 plus 32% in excess over 166,667
        } else { // taxableIncome > 666667
            tax = 200833.33 + ((taxableIncome - 666667) * 0.35); // 200,833.33 plus 35% in excess of 666,667
        }
        return Math.max(0, tax); // tax is never negative
    }
    
    //getters
    public LocalDate getPayPeriodStart() { return payPeriodStart; }
    public LocalDate getPayPeriodEnd() { return payPeriodEnd; }
    public double getDaysWorked() { return daysWorked; }
    public double getOvertimePay() { return overtimePay; }
    public double getGrossIncome() { return grossIncome; }
    public double getTotalAllowances() { return totalAllowances; }
    public double getSssContribution() { return sssContribution; }
    public double getPhilhealthContribution() { return philhealthContribution; }
    public double getPagibigContribution() { return pagibigContribution; }
    public double getWithholdingTax() { return withholdingTax; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getTakeHomePay() { return takeHomePay; }
}


