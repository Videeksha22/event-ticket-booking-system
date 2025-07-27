package com.ticketbooking;

import com.ticketbooking.ui.LoginForm;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Event Ticket Booking System...");
        
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("UI Look and Feel set to: " + UIManager.getSystemLookAndFeelClassName());
            
            // Print basic environment info
            System.out.println("Java version: " + System.getProperty("java.version"));
            System.out.println("OS: " + System.getProperty("os.name"));
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Create and show login form
                        LoginForm loginForm = new LoginForm();
                        
                        // Debug info
                        System.out.println("Login form created");
                        System.out.println("Login form size: " + loginForm.getSize());
                        
                        // Check if components are visible
                        Component[] components = ((JPanel)loginForm.getContentPane().getComponent(0)).getComponents();
                        System.out.println("Main panel component count: " + components.length);
                        
                        // Make the form visible
                        loginForm.setVisible(true);
                        System.out.println("Login form should now be visible");
                    } catch (Exception e) {
                        System.err.println("Error initializing login form: " + e.getMessage());
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, 
                            "Error starting application: " + e.getMessage(),
                            "Startup Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error in application startup: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Critical error in application startup: " + e.getMessage(),
                "Critical Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 