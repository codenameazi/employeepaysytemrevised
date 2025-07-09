
package com.motorph.payrollsystem.payrolldata;


public class Employee {
    
    private String id;
    private String password; 
    private String lastName;
    private String firstName;
    private String birthday; 
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String status;
    private String position;
    private String supervisor; 
    private double basicSalary; 
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlyRate; 
    private double hourlyRate;           
    private double dailyRate;            
    private String payPeriod;
    
    
    //constructor
    public Employee(String id, String password, String lastName, String firstName, String birthday, String address,
                    String phoneNumber, String sssNumber, String philHealthNumber, String tin,
                    String pagIbigNumber, String status, String position, String supervisor, // 14 fields
                    double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, // 4 allowances
                    double grossSemiMonthlyRate, double hourlyRate, double dailyRate, String payPeriod) { // 4 rates/period
        this.id = id;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tin = tin;
        this.pagIbigNumber = pagIbigNumber;
        this.status = status;
        this.position = position;
        this.supervisor = supervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.payPeriod = payPeriod;
    }   
    
    //getters
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilHealthNumber() { return philHealthNumber; }
    public String getTin() { return tin; }
    public String getPagIbigNumber() { return pagIbigNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getSupervisor() { return supervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }
    public double getDailyRate() { return dailyRate; }
    public String getPayPeriod() { return payPeriod; }  
    
    // setters - for update feature
    public void setPassword(String password) { this.password = password; } // Added setter for password
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void voidsetFirstName(String firstName) { this.firstName = firstName; } // Typo: voidsetFirstName -> setFirstName
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }
    public void setTin(String tin) { this.tin = tin; }
    public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setPosition(String position) { this.position = position; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
    public void setPayPeriod(String payPeriod) { this.payPeriod = payPeriod; }

    // display of employee name
    public String getFullName() {
        return lastName + ", " + firstName;
    }

    //method to convert Employee object to CSV line string (useful for writing)
    public String toCsvLine() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%s",
                id, password, lastName, firstName, birthday, address, phoneNumber, sssNumber, philHealthNumber, tin,
                pagIbigNumber, status, position, supervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance,
                grossSemiMonthlyRate, hourlyRate, dailyRate, payPeriod);
    }
}
    


