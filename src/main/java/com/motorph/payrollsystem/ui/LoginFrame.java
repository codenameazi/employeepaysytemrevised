
package com.motorph.payrollsystem.ui;

import com.motorph.payrollsystem.csv.UserCSVReader;
import com.motorph.payrollsystem.payrolldata.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;


public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private Map<String, User> authorizedUsers;
    
    public LoginFrame() {
        setTitle("MotorPH Payroll System Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //center window

        //load authorized users
        authorizedUsers = UserCSVReader.loadUsers();

        //setup Panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; //allow horizontal expansion
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        //password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        //login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });
        panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); 

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = authorizedUsers.get(username);

        //authentication
        if (user != null && user.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(this,
                    "Login Successful!",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);
            //open the main application window
            this.dispose();
            //EmployeeListFrame as the main application window
            EmployeeListFrame employeeListFrame = new EmployeeListFrame();
            employeeListFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Username or Password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
        //clear password field
        passwordField.setText("");
    }
    
}


