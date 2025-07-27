package com.ticketbooking.ui;

import com.ticketbooking.model.User;
import com.ticketbooking.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.sql.SQLException;

/**
 * Login form for user authentication
 */
public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblStatus;
    
    private UserService userService;
    
    public LoginForm() {
        userService = new UserService();
        initComponents();
    }
    
    private void initComponents() {
        // Set up the frame
        setTitle("Event Ticket Booking System - Login");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set custom colors
        Color primaryColor = new Color(70, 130, 180); // Steel Blue
        Color accentColor = new Color(0, 123, 255);   // Blue
        Color bgColor = new Color(245, 248, 250);     // Light Gray

        // Create main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(bgColor);
        
        // Title Panel
        JLabel iconLabel = new JLabel(createTicketIcon(80, 80));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("Event Ticket Booking");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(primaryColor);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Sign in to your account");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.DARK_GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Form Panel for username/password with better styling
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(bgColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsername.setForeground(new Color(80, 80, 80));
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setMargin(new Insets(8, 10, 8, 10));
        txtUsername.setText("admin"); // Pre-filled for admin testing
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setForeground(new Color(80, 80, 80));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setMargin(new Insets(8, 10, 8, 10));
        txtPassword.setText("admin123"); // Pre-filled for admin testing
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Create buttons with modern styling
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setForeground(Color.BLUE);
        btnLogin.setBackground(accentColor);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setContentAreaFilled(true);
        btnLogin.setOpaque(true);
        
        btnRegister = new JButton("Create new account");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setForeground(accentColor);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Status label
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        lblStatus.setForeground(new Color(220, 53, 69));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add some spacing between components
        formPanel.add(lblUsername);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtPassword);
        
        // Add components to main panel with appropriate spacing
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lblSubtitle);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnRegister);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(lblStatus);
        
        // Add panel to frame
        getContentPane().setBackground(bgColor);
        add(mainPanel);
        
        // Add action listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationForm();
            }
        });
    }
    
    private ImageIcon createTicketIcon(int width, int height) {
        // Create a simple ticket icon
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw ticket shape
        g2d.setColor(new Color(70, 130, 180));
        g2d.fillRoundRect(0, 10, width-10, height-20, 10, 10);
        
        // Draw ticket stub
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.WHITE);
        g2d.drawLine(width-30, 10, width-30, height-10);
        
        // Draw perforations
        g2d.setColor(Color.WHITE);
        for (int i = 15; i < height-15; i += 7) {
            g2d.drawLine(width-30, i, width-30, i+3);
        }
        
        // Add some ticket details (lines)
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.drawLine(10, 30, width-40, 30);
        g2d.drawLine(10, 50, width-40, 50);
        g2d.drawLine(10, 70, width-40, 70);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        lblStatus.setText("Logging in...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);
        
        System.out.println("Login attempt: " + username);
        
        // Use a SwingWorker for background processing
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                try {
                    User user = userService.authenticate(username, password);
                    if (user != null) {
                        System.out.println("Authentication successful for: " + username + ", Role: " + user.getRole());
                    } else {
                        System.out.println("Authentication failed for: " + username);
                    }
                    return user;
                } catch (SQLException e) {
                    System.err.println("SQL Error during authentication: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                } catch (Exception e) {
                    System.err.println("Unexpected error during authentication: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        if ("ADMIN".equals(user.getRole())) {
                            System.out.println("Admin login successful, opening AdminDashboard...");
                            try {
                                dispose();
                                AdminDashboard adminDashboard = new AdminDashboard();
                                adminDashboard.setVisible(true);
                                System.out.println("AdminDashboard set visible");
                            } catch (Exception e) {
                                System.err.println("Error creating AdminDashboard: " + e.getMessage());
                                e.printStackTrace();
                                lblStatus.setText("Error opening admin dashboard: " + e.getMessage());
                                btnLogin.setEnabled(true);
                                setCursor(Cursor.getDefaultCursor());
                                JOptionPane.showMessageDialog(LoginForm.this,
                                    "Error opening admin dashboard: " + e.getMessage(),
                                    "Dashboard Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            System.out.println("User login successful, opening UserDashboard...");
                            dispose();
                            new UserDashboard(user).setVisible(true);
                        }
                    } else {
                        lblStatus.setText("Invalid username or password!");
                        btnLogin.setEnabled(true);
                        setCursor(Cursor.getDefaultCursor());
                    }
                } catch (Exception e) {
                    System.err.println("Error in login completion: " + e.getMessage());
                    e.printStackTrace();
                    lblStatus.setText("Error: " + e.getMessage());
                    btnLogin.setEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        
        worker.execute();
    }
    
    private void showRegistrationForm() {
        RegistrationForm registrationForm = new RegistrationForm(this);
        registrationForm.setVisible(true);
        setVisible(false);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set some global UI manager properties
            UIManager.put("Button.focus", new Color(0,0,0,0));
            UIManager.put("TextField.caretForeground", new Color(70, 130, 180));
            UIManager.put("Button.select", new Color(0, 100, 210)); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
} 