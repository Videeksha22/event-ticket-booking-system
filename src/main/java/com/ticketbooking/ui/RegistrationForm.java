package com.ticketbooking.ui;

import com.ticketbooking.model.User;
import com.ticketbooking.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration form for new user registration
 */
public class RegistrationForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JButton btnRegister;
    private JButton btnCancel;
    private JLabel lblStatus;
    
    private UserService userService;
    private JFrame parentFrame;
    
    public RegistrationForm(JFrame parent) {
        this.parentFrame = parent;
        userService = new UserService();
        initComponents();
    }
    
    private void initComponents() {
        // Set up the frame
        setTitle("Event Ticket Booking System - Registration");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create components
        JLabel lblTitle = new JLabel("Register New User");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        txtConfirmPassword = new JPasswordField(20);
        
        JLabel lblFullName = new JLabel("Full Name:");
        txtFullName = new JTextField(20);
        
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);
        
        JLabel lblPhone = new JLabel("Phone:");
        txtPhone = new JTextField(20);
        
        btnRegister = new JButton("Register");
        btnRegister.setForeground(Color.BLACK);
        btnCancel = new JButton("Cancel");
        btnCancel.setForeground(Color.BLACK);
        
        lblStatus = new JLabel("");
        lblStatus.setForeground(Color.RED);
        
        // Set up layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword);
        formPanel.add(txtConfirmPassword);
        formPanel.add(lblFullName);
        formPanel.add(txtFullName);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblPhone);
        formPanel.add(txtPhone);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(lblStatus, BorderLayout.CENTER);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add action listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
    }
    
    private void register() {
        // Clear previous status
        lblStatus.setText("");
        
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());  
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            fullName.isEmpty() || email.isEmpty()) {
            lblStatus.setText("Please fill in all required fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblStatus.setText("Passwords do not match");
            return;
        }

        try {
            // Register user
            User user = userService.register(username, password, fullName, email, phone);
            
            if (user != null) {
                // Create custom success dialog with black text buttons
                JDialog successDialog = new JDialog(this, "Registration Success", true);
                successDialog.setLayout(new BorderLayout());
                successDialog.setSize(350, 150);
                successDialog.setLocationRelativeTo(this);
                
                // Create message panel
                JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
                messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
                JLabel messageLabel = new JLabel("Registration successful! Please login.");
                
                messagePanel.add(iconLabel, BorderLayout.WEST);
                messagePanel.add(messageLabel, BorderLayout.CENTER);
                
                // Create OK button with black text
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton okButton = new JButton("OK");
                okButton.setForeground(Color.BLACK);
                
                okButton.addActionListener(e -> {
                    successDialog.dispose();
                    dispose(); // Close registration form
                    if (parentFrame != null) {
                        parentFrame.setVisible(true); // Show login form
                    }
                });
                
                buttonPanel.add(okButton);
                
                // Add panels to dialog
                successDialog.add(messagePanel, BorderLayout.CENTER);
                successDialog.add(buttonPanel, BorderLayout.SOUTH);
                
                // Show dialog
                successDialog.setVisible(true);
            } else {
                lblStatus.setText("Registration failed. Username may already exist.");
            }
        } catch (Exception ex) {
            lblStatus.setText("Registration error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void cancel() {
        dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }
} 