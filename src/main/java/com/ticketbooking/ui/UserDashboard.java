package com.ticketbooking.ui;

import com.ticketbooking.model.Event;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.User;
import com.ticketbooking.service.EventService;
import com.ticketbooking.service.TicketService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Dashboard for regular users
 */
public class UserDashboard extends JFrame {
    private User currentUser;
    private EventService eventService;
    private TicketService ticketService;
    
    private JTabbedPane tabbedPane;
    private JTable eventsTable;
    private JTable myTicketsTable;
    private DefaultTableModel eventsTableModel;
    private DefaultTableModel myTicketsTableModel;
    private JButton btnLogout;
    
    public UserDashboard(User user) {
        this.currentUser = user;
        
        try {
            this.ticketService = new TicketService();
            this.eventService = new EventService();
            
            initComponents();
            loadEvents();
            loadTickets();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error initializing services: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private void initComponents() {
        // Set up the frame
        setTitle("Event Ticket Booking System - User Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create Events tab
        JPanel eventsPanel = createEventsPanel();
        tabbedPane.addTab("Available Events", eventsPanel);
        
        // Create My Tickets tab
        JPanel myTicketsPanel = createMyTicketsPanel();
        tabbedPane.addTab("My Tickets", myTicketsPanel);
        
        // Create user info panel
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblUserInfo = new JLabel("Logged in as: " + currentUser.getFullName());
        btnLogout = new JButton("Logout");
        btnLogout.setForeground(Color.BLACK);
        userInfoPanel.add(lblUserInfo);
        userInfoPanel.add(btnLogout);
        
        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(userInfoPanel, BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Add action listeners
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }
    
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Events table
        String[] columns = {"ID", "Event Name", "Venue", "Date", "Time", "Available Seats", "Ticket Price"};
        eventsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventsTable = new JTable(eventsTableModel);
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        
        // Style the table
        eventsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        eventsTable.getTableHeader().setForeground(Color.BLACK);
        eventsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBookTicket = new JButton("Book Ticket");
        btnBookTicket.setForeground(Color.BLACK);
        btnBookTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookTicket();
            }
        });
        buttonPanel.add(btnBookTicket);
        
        // Add components to panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMyTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // My tickets table
        String[] columns = {"Ticket ID", "Event", "Quantity", "Total Price", "Booking Date", "Status"};
        myTicketsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myTicketsTable = new JTable(myTicketsTableModel);
        JScrollPane scrollPane = new JScrollPane(myTicketsTable);
        
        // Style the table
        myTicketsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        myTicketsTable.getTableHeader().setForeground(Color.BLACK);
        myTicketsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnViewTicket = new JButton("View Details");
        btnViewTicket.setForeground(Color.BLACK);
        JButton btnCancelTicket = new JButton("Cancel Ticket");
        btnCancelTicket.setForeground(Color.BLACK);
        
        // Add action listeners
        btnViewTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTicketDetails();
            }
        });
        
        btnCancelTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelTicket();
            }
        });
        
        buttonPanel.add(btnViewTicket);
        buttonPanel.add(btnCancelTicket);
        
        // Add components to panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadEvents() {
        // Clear table
        eventsTableModel.setRowCount(0);
        
        // Get upcoming events
        List<Event> events = eventService.getUpcomingEvents();
        
        // Add events to table
        for (Event event : events) {
            Object[] row = {
                event.getEventId(),
                event.getEventName(),
                event.getVenue(),
                event.getEventDate(),
                event.getEventTime(),
                event.getAvailableSeats(),
                "$" + event.getTicketPrice()
            };
            eventsTableModel.addRow(row);
        }
    }
    
    private void loadTickets() {
        try {
            List<Ticket> tickets = ticketService.getTicketsByUserId(currentUser.getUserId());
            myTicketsTableModel.setRowCount(0);
            
            for (Ticket ticket : tickets) {
                Event event = eventService.getEventById(ticket.getEventId());
                if (event != null) {
                    myTicketsTableModel.addRow(new Object[]{
                        ticket.getTicketId(),
                        event.getEventName(),
                        ticket.getQuantity(),
                        String.format("$%.2f", ticket.getTotalPrice()),
                        ticket.getBookingDate(),
                        ticket.getPaymentStatus()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading tickets: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        // Create a custom dialog instead of using JOptionPane directly
        JDialog logoutDialog = new JDialog(this, "Confirm Logout", true);
        logoutDialog.setLayout(new BorderLayout());
        logoutDialog.setSize(300, 150);
        logoutDialog.setLocationRelativeTo(this);
        
        // Create icon and message
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
        JLabel messageLabel = new JLabel("Are you sure you want to logout?");
        messagePanel.add(iconLabel, BorderLayout.WEST);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Create buttons with black text
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // Set text color to black
        yesButton.setForeground(Color.BLACK);
        noButton.setForeground(Color.BLACK);
        
        // Add action listeners
        yesButton.addActionListener(e -> {
            logoutDialog.dispose();
            dispose();  // Close UserDashboard
            new LoginForm().setVisible(true);
        });
        
        noButton.addActionListener(e -> {
            logoutDialog.dispose();
        });
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        
        // Add panels to dialog
        logoutDialog.add(messagePanel, BorderLayout.CENTER);
        logoutDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        logoutDialog.setVisible(true);
    }
    
    private void bookTicket() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an event to book tickets for.", 
                "No Event Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get selected event data
            int eventId = (Integer) eventsTableModel.getValueAt(selectedRow, 0);
            String eventName = (String) eventsTableModel.getValueAt(selectedRow, 1);
            int availableSeats = (Integer) eventsTableModel.getValueAt(selectedRow, 5);
            
            // Check if seats are available
            if (availableSeats <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Sorry, there are no available tickets for this event.", 
                    "No Available Tickets", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create a custom ticket quantity input dialog with black text buttons
            JDialog quantityDialog = new JDialog(this, "Ticket Quantity", true);
            quantityDialog.setLayout(new BorderLayout());
            quantityDialog.setSize(400, 150);
            quantityDialog.setLocationRelativeTo(this);
            
            // Create message panel
            JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
            messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
            JLabel messageLabel = new JLabel("How many tickets would you like to book for " + eventName + "?");
            
            JTextField quantityField = new JTextField(10);
            
            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(messageLabel, BorderLayout.NORTH);
            inputPanel.add(quantityField, BorderLayout.CENTER);
            
            messagePanel.add(iconLabel, BorderLayout.WEST);
            messagePanel.add(inputPanel, BorderLayout.CENTER);
            
            // Create buttons with black text
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");
            
            // Set text color to black
            okButton.setForeground(Color.BLACK);
            cancelButton.setForeground(Color.BLACK);
            
            // Variable to store the quantity input
            final int[] quantity = {0};
            final boolean[] dialogCancelled = {true};
            
            // Add action listeners
            okButton.addActionListener(e -> {
                String quantityStr = quantityField.getText().trim();
                if (quantityStr.isEmpty()) {
                    return;
                }
                
                try {
                    int qty = Integer.parseInt(quantityStr);
                    if (qty <= 0) {
                        JOptionPane.showMessageDialog(quantityDialog, 
                            "Please enter a valid quantity greater than zero.", 
                            "Invalid Quantity", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (qty > availableSeats) {
                        JOptionPane.showMessageDialog(quantityDialog, 
                            "Sorry, only " + availableSeats + " seats available.", 
                            "Not Enough Seats", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    quantity[0] = qty;
                    dialogCancelled[0] = false;
                    quantityDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(quantityDialog, 
                        "Please enter a valid number.", 
                        "Invalid Input", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> {
                quantityDialog.dispose();
            });
            
            // Add buttons to panel
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            
            // Add panels to dialog
            quantityDialog.add(messagePanel, BorderLayout.CENTER);
            quantityDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            // Show dialog and wait for result
            quantityDialog.setVisible(true);
            
            // If dialog was cancelled or no quantity selected, return
            if (dialogCancelled[0] || quantity[0] == 0) {
                return;
            }
            
            int finalQuantity = quantity[0];
                
            // Book the tickets
            Ticket ticket = ticketService.bookTicket(eventId, currentUser.getUserId(), finalQuantity);
            
            if (ticket != null) {
                // Show payment dialog
                showPaymentDialog(ticket);
                
                // Refresh the tables
                loadEvents();
                loadTickets();
                
                // Switch to My Tickets tab
                tabbedPane.setSelectedIndex(1);
                
                // Show success message with black button text
                showSuccessMessageDialog(this,
                    "Your tickets have been booked successfully!",
                    "Booking Successful");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to book tickets. Please try again.", 
                    "Booking Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error booking tickets: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to show a message dialog with black text buttons
    private void showSuccessMessageDialog(Component parentComponent, String message, String title) {
        JDialog dialog = new JDialog((Frame) parentComponent, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(parentComponent);
        
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        JLabel messageLabel = new JLabel(message);
        
        messagePanel.add(iconLabel, BorderLayout.WEST);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.setForeground(Color.BLACK);
        
        okButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        
        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showPaymentDialog(Ticket ticket) {
        JDialog paymentDialog = new JDialog(this, "Payment", true);
        paymentDialog.setLayout(new BorderLayout());
        paymentDialog.setSize(400, 300);
        paymentDialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Total Amount:"));
        formPanel.add(new JLabel(String.format("$%.2f", ticket.getTotalPrice())));
        
        formPanel.add(new JLabel("Payment Method:"));
        JComboBox<String> paymentMethodCombo = new JComboBox<>(
            new String[]{"Credit Card", "Debit Card", "PayPal", "Bank Transfer"}
        );
        formPanel.add(paymentMethodCombo);
        
        formPanel.add(new JLabel("Card Number:"));
        JTextField cardNumberField = new JTextField("1234-5678-9012-3456");
        formPanel.add(cardNumberField);
        
        formPanel.add(new JLabel("Expiry Date:"));
        JTextField expiryDateField = new JTextField("12/25");
        formPanel.add(expiryDateField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton payButton = new JButton("Process Payment");
        payButton.setForeground(Color.BLACK);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.BLACK);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(payButton);
        
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Use a shorter payment status value that fits within VARCHAR(20)
                    // Note: In the database schema, payment_status is VARCHAR(20)
                    ticketService.updatePaymentStatus(ticket.getTicketId(), "PAID");
                    paymentDialog.dispose();
                    
                    // Refresh tickets list to display updated status
                    loadTickets();

                    // Show success message with black text button
                    showSuccessMessageDialog(UserDashboard.this,
                        "Payment processed successfully!",
                        "Payment Complete");
                } catch (SQLException ex) {
                    // Show error message with black text button
                    showErrorMessageDialog(paymentDialog,
                        "Error processing payment: " + ex.getMessage(),
                        "Payment Error");
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentDialog.dispose();
            }
        });
        
        paymentDialog.add(formPanel, BorderLayout.CENTER);
        paymentDialog.add(buttonPanel, BorderLayout.SOUTH);
        paymentDialog.setVisible(true);
    }
    
    // Helper method to show an error message dialog with black text buttons
    private void showErrorMessageDialog(Component parentComponent, String message, String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(parentComponent);
        
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
        JLabel messageLabel = new JLabel(message);
        
        messagePanel.add(iconLabel, BorderLayout.WEST);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.setForeground(Color.BLACK);
        
        okButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        
        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void viewTicketDetails() {
        int selectedRow = myTicketsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a ticket to view details.", 
                "No Ticket Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get ticket ID
            int ticketId = (Integer) myTicketsTableModel.getValueAt(selectedRow, 0);
            String eventName = (String) myTicketsTableModel.getValueAt(selectedRow, 1);
            int quantity = (Integer) myTicketsTableModel.getValueAt(selectedRow, 2);
            String totalPrice = (String) myTicketsTableModel.getValueAt(selectedRow, 3);
            String bookingDate = (String) myTicketsTableModel.getValueAt(selectedRow, 4);
            String status = (String) myTicketsTableModel.getValueAt(selectedRow, 5);
            
            // Create ticket details dialog
            JDialog detailsDialog = new JDialog(this, "Ticket Details", true);
            detailsDialog.setLayout(new BorderLayout());
            detailsDialog.setSize(400, 300);
            detailsDialog.setLocationRelativeTo(this);
            
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Add ticket details
            JLabel lblTitle = new JLabel("Ticket Details");
            lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JPanel infoPanel = new JPanel(new GridLayout(6, 2, 5, 10));
            infoPanel.add(new JLabel("Ticket ID:"));
            infoPanel.add(new JLabel(String.valueOf(ticketId)));
            infoPanel.add(new JLabel("Event:"));
            infoPanel.add(new JLabel(eventName));
            infoPanel.add(new JLabel("Quantity:"));
            infoPanel.add(new JLabel(String.valueOf(quantity)));
            infoPanel.add(new JLabel("Total Price:"));
            infoPanel.add(new JLabel(totalPrice));
            infoPanel.add(new JLabel("Booking Date:"));
            infoPanel.add(new JLabel(bookingDate));
            infoPanel.add(new JLabel("Status:"));
            infoPanel.add(new JLabel(status));
            
            JButton closeButton = new JButton("Close");
            closeButton.setForeground(Color.BLACK);
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    detailsDialog.dispose();
                }
            });
            
            detailsPanel.add(lblTitle);
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            detailsPanel.add(infoPanel);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(closeButton);
            
            detailsDialog.add(detailsPanel, BorderLayout.CENTER);
            detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
            detailsDialog.setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error retrieving ticket details: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelTicket() {
        int selectedRow = myTicketsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a ticket to cancel.", 
                "No Ticket Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get ticket ID and status
        int ticketId = (Integer) myTicketsTableModel.getValueAt(selectedRow, 0);
        String status = (String) myTicketsTableModel.getValueAt(selectedRow, 5);
        
        // Check if ticket is already cancelled
        if ("CANCELLED".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "This ticket has already been cancelled.", 
                "Already Cancelled", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create custom cancellation confirmation dialog with black text buttons
        JDialog cancelDialog = new JDialog(this, "Confirm Cancellation", true);
        cancelDialog.setLayout(new BorderLayout());
        cancelDialog.setSize(350, 150);
        cancelDialog.setLocationRelativeTo(this);
        
        // Create icon and message
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        JLabel messageLabel = new JLabel("Are you sure you want to cancel this ticket? This cannot be undone.");
        messagePanel.add(iconLabel, BorderLayout.WEST);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Create buttons with black text
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        // Set text color to black
        yesButton.setForeground(Color.BLACK);
        noButton.setForeground(Color.BLACK);
        
        // Add action listeners
        yesButton.addActionListener(e -> {
            cancelDialog.dispose();
            try {
                if (ticketService.cancelTicket(ticketId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Ticket cancelled successfully. Refunds will be processed according to policy.", 
                        "Cancellation Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh tickets
                    loadTickets();
                    loadEvents(); // Also reload events as available seats may have changed
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to cancel ticket. Please try again.", 
                        "Cancellation Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error cancelling ticket: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        noButton.addActionListener(e -> {
            cancelDialog.dispose();
        });
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        
        // Add panels to dialog
        cancelDialog.add(messagePanel, BorderLayout.CENTER);
        cancelDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        cancelDialog.setVisible(true);
    }
} 