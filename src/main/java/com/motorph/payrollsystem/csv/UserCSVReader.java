
package com.motorph.payrollsystem.csv;

import com.motorph.payrollsystem.MainApp; 
import com.motorph.payrollsystem.payrolldata.User;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File; 
import java.util.HashMap;
import java.util.Map;


public class UserCSVReader {
    
    private static final String CSV_FILE_PATH = MainApp.DATA_DIRECTORY + File.separator + MainApp.USER_CSV_FILENAME;

    public static Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        String line;
        File csvFile = new File(CSV_FILE_PATH);

        if (!csvFile.exists()) {
            System.err.println("Error: User CSV file not found at " + CSV_FILE_PATH);
            JOptionPane.showMessageDialog(null, "User accounts file not found: " + CSV_FILE_PATH, "File Not Found", JOptionPane.ERROR_MESSAGE);
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                //handle potential empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // split by comma
                String[] data = line.split(",", -1); 

                if (data.length >= 2) {
                    // Remove leading/trailing quotes and trim whitespace
                    String username = data[0].trim().replaceAll("^\"|\"$", "");
                    String password = data[1].trim().replaceAll("^\"|\"$", "");

                    users.put(username, new User(username, password));
                } else {
                    System.err.println("Skipping malformed user CSV line (expected 2+ fields, found " + data.length + "): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user data from " + CSV_FILE_PATH + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading user data from file. See console for details.", "Read Error", JOptionPane.ERROR_MESSAGE);
        }
        return users;
    }
}


