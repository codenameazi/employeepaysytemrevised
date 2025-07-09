package com.motorph.payrollsystem;

import com.motorph.payrollsystem.ui.LoginFrame;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.io.FileWriter;
import java.io.PrintWriter; 


public class MainApp {
    
    public static final String DATA_DIRECTORY = "data";
    public static final String EMPLOYEE_CSV_FILENAME = "employee_dataV1.csv";
    public static final String ATTENDANCE_CSV_FILENAME = "attendance_record.csv";
    public static final String USER_CSV_FILENAME = "user_accounts.csv"; 
    
    public static void main(String[] args) {
        //createDummyCsvFiles();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
   
}


