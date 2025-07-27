package com.ticketbooking.ui;

import com.ticketbooking.model.Event;
import com.ticketbooking.model.TicketType;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.service.EventService;
import com.ticketbooking.service.TicketTypeService;
import com.ticketbooking.service.TicketService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Dashboard for admin users
 */
public class AdminDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel eventsPanel;
    private JPanel ticketTypesPanel;
    private JPanel reportsPanel;
    private JTable eventsTable;
    private JTable ticketTypesTable;
    private DefaultTableModel eventsTableModel;
    private DefaultTableModel ticketTypesTableModel;
    private EventService eventService;
    private TicketTypeService ticketTypeService;

    public AdminDashboard() {
        setTitle("Admin Dashboard - Event Ticket Booking System");
        setSize(1000, 700); // Larger default size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            System.out.println("Initializing AdminDashboard...");
            
            // Set custom look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Set custom colors
                UIManager.put("Panel.background", new Color(245, 245, 250));
                UIManager.put("Button.background", new Color(70, 130, 180));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));
                System.out.println("AdminDashboard UI settings applied");
            } catch (Exception e) {
                System.err.println("Error setting look and feel: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("Initializing services...");
            eventService = new EventService();
            ticketTypeService = new TicketTypeService();
            System.out.println("Services initialized successfully");

            System.out.println("Initializing components...");
            initializeComponents();
            System.out.println("Components initialized successfully");
            
            System.out.println("Loading events...");
            loadEvents();
            System.out.println("Events loaded successfully");
            
            System.out.println("Loading ticket types...");
            loadTicketTypes();
            System.out.println("Ticket types loaded successfully");
            
            System.out.println("AdminDashboard initialization complete");
        } catch (Exception e) {
            System.err.println("Error initializing AdminDashboard: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error initializing AdminDashboard: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
            
            // Make sure we properly display the login form again
            SwingUtilities.invokeLater(() -> {
                dispose();
                new LoginForm().setVisible(true);
            });
        }
    }

    private void initializeComponents() {
        // Create a main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 60, 90));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Event Ticket Booking System - Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setOpaque(true);
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Create tabbed pane with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        // Events Panel
        eventsPanel = new JPanel(new BorderLayout(0, 10));
        eventsPanel.setBackground(Color.WHITE);
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] eventColumns = {"ID", "Name", "Date", "Venue", "Description"};
        eventsTableModel = new DefaultTableModel(eventColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventsTable = new JTable(eventsTableModel);
        eventsTable.setRowHeight(25);
        eventsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        eventsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        eventsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        eventsTable.getTableHeader().setForeground(Color.BLACK);
        eventsTable.setSelectionBackground(new Color(173, 216, 230));
        eventsTable.setGridColor(new Color(230, 230, 230));
        
        JScrollPane eventsScrollPane = new JScrollPane(eventsTable);
        eventsScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel eventButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        eventButtonsPanel.setBackground(Color.WHITE);
        
        JButton addEventBtn = createStyledButton("Add Event", new Color(40, 167, 69));
        JButton editEventBtn = createStyledButton("Edit Event", new Color(0, 123, 255));
        JButton deleteEventBtn = createStyledButton("Delete Event", new Color(220, 53, 69));
        JButton manageTicketsBtn = createStyledButton("Manage Tickets", new Color(255, 193, 7));

        eventButtonsPanel.add(addEventBtn);
        eventButtonsPanel.add(editEventBtn);
        eventButtonsPanel.add(deleteEventBtn);
        eventButtonsPanel.add(manageTicketsBtn);

        eventsPanel.add(eventsScrollPane, BorderLayout.CENTER);
        eventsPanel.add(eventButtonsPanel, BorderLayout.SOUTH);

        // Ticket Types Panel
        ticketTypesPanel = new JPanel(new BorderLayout(0, 10));
        ticketTypesPanel.setBackground(Color.WHITE);
        ticketTypesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] ticketColumns = {"ID", "Event", "Category", "Price", "Available", "Status"};
        ticketTypesTableModel = new DefaultTableModel(ticketColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ticketTypesTable = new JTable(ticketTypesTableModel);
        ticketTypesTable.setRowHeight(25);
        ticketTypesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        ticketTypesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        ticketTypesTable.getTableHeader().setBackground(new Color(70, 130, 180));
        ticketTypesTable.getTableHeader().setForeground(Color.BLACK);
        ticketTypesTable.setSelectionBackground(new Color(173, 216, 230));
        ticketTypesTable.setGridColor(new Color(230, 230, 230));
        
        JScrollPane ticketTypesScrollPane = new JScrollPane(ticketTypesTable);
        ticketTypesScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel ticketButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        ticketButtonsPanel.setBackground(Color.WHITE);
        
        JButton addTicketTypeBtn = createStyledButton("Add Ticket Type", new Color(40, 167, 69));
        JButton editTicketTypeBtn = createStyledButton("Edit Ticket Type", new Color(0, 123, 255));
        JButton deleteTicketTypeBtn = createStyledButton("Delete Ticket Type", new Color(220, 53, 69));

        ticketButtonsPanel.add(addTicketTypeBtn);
        ticketButtonsPanel.add(editTicketTypeBtn);
        ticketButtonsPanel.add(deleteTicketTypeBtn);

        ticketTypesPanel.add(ticketTypesScrollPane, BorderLayout.CENTER);
        ticketTypesPanel.add(ticketButtonsPanel, BorderLayout.SOUTH);

        // Reports Panel
        reportsPanel = new JPanel(new BorderLayout(0, 10));
        reportsPanel.setBackground(Color.WHITE);
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel reportContent = new JPanel();
        reportContent.setLayout(new BoxLayout(reportContent, BoxLayout.Y_AXIS));
        reportContent.setBackground(Color.WHITE);
        
        JLabel reportsTitle = new JLabel("Reports Dashboard");
        reportsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        reportsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        reportsTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JPanel reportButtonsPanel = new JPanel();
        reportButtonsPanel.setLayout(new BoxLayout(reportButtonsPanel, BoxLayout.Y_AXIS));
        reportButtonsPanel.setBackground(Color.WHITE);
        reportButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        reportButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton bookingSummaryBtn = createReportButton("Booking Summary", "View summary of bookings for all events");
        JButton revenueReportBtn = createReportButton("Revenue Report", "View detailed revenue breakdown by event and ticket type");
        
        reportButtonsPanel.add(bookingSummaryBtn);
        reportButtonsPanel.add(Box.createVerticalStrut(20));
        reportButtonsPanel.add(revenueReportBtn);
        
        reportContent.add(reportsTitle);
        reportContent.add(reportButtonsPanel);
        
        reportsPanel.add(reportContent, BorderLayout.CENTER);

        // Add action listeners
        addEventBtn.addActionListener(e -> showAddEventDialog());
        editEventBtn.addActionListener(e -> showEditEventDialog());
        deleteEventBtn.addActionListener(e -> deleteSelectedEvent());
        manageTicketsBtn.addActionListener(e -> showTicketTypeDialog());

        addTicketTypeBtn.addActionListener(e -> showAddTicketTypeDialog());
        editTicketTypeBtn.addActionListener(e -> showEditTicketTypeDialog());
        deleteTicketTypeBtn.addActionListener(e -> deleteSelectedTicketType());

        bookingSummaryBtn.addActionListener(e -> showBookingSummary());
        revenueReportBtn.addActionListener(e -> showRevenueReport());

        // Add panels to tabbed pane
        tabbedPane.addTab("Events", eventsPanel);
        tabbedPane.addTab("Ticket Types", ticketTypesPanel);
        tabbedPane.addTab("Reports", reportsPanel);
        
        // Now set the foreground color after tabs are added
        tabbedPane.setForegroundAt(0, new Color(70, 130, 180));
        
        // Add components to main frame
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setPreferredSize(new Dimension(getWidth(), 25));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.setBackground(new Color(240, 240, 240));
        
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        
        // Reset button UI to avoid Look and Feel interference
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        
        // Set colors with high contrast - black text instead of white
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        
        // Use a bolder font for better visibility
        button.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add a border for better definition
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
        
        // Ensure the styling is applied
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        return button;
    }
    
    private JButton createReportButton(String text, String description) {
        JButton button = new JButton(text);
        
        // Reset button UI to avoid Look and Feel interference
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        
        // Set colors with high contrast - black text instead of white
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.BLACK);
        
        // Use a bolder font for better visibility
        button.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Add a border for better definition
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
        
        // Size settings
        button.setMaximumSize(new Dimension(300, 50));
        button.setPreferredSize(new Dimension(300, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tooltip and styling
        button.setToolTipText(description);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        return button;
    }

    private void loadEvents() {
        try {
            eventsTableModel.setRowCount(0);
            List<Event> events = eventService.getAllEvents();
            for (Event event : events) {
                eventsTableModel.addRow(new Object[]{
                    event.getEventId(),
                    event.getEventName(),
                    event.getEventDate(),
                    event.getVenue(),
                    event.getDescription()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTicketTypes() {
        try {
            ticketTypesTableModel.setRowCount(0);
            List<Event> events = eventService.getAllEvents();
            
            for (Event event : events) {
                List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEventId(event.getEventId());
                for (TicketType type : ticketTypes) {
                    ticketTypesTableModel.addRow(new Object[]{
                        type.getTicketTypeId(),
                        event.getEventName(),
                        type.getCategory(),
                        String.format("$%.2f", type.getPrice()),
                        type.getAvailableQuantity(),
                        type.isActive() ? "Active" : "Inactive"
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading ticket types: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddEventDialog() {
        JDialog dialog = new JDialog(this, "Add New Event", true);
        dialog.setLayout(new GridLayout(8, 2, 5, 5));
        
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField venueField = new JTextField();
        JTextField totalSeatsField = new JTextField();
        JTextField priceField = new JTextField();
        JTextArea descArea = new JTextArea();
        
        dialog.add(new JLabel("Event Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Date (YYYY-MM-DD):"));
        dialog.add(dateField);
        dialog.add(new JLabel("Time (HH:MM:SS):"));
        dialog.add(timeField);
        dialog.add(new JLabel("Venue:"));
        dialog.add(venueField);
        dialog.add(new JLabel("Total Seats:"));
        dialog.add(totalSeatsField);
        dialog.add(new JLabel("Ticket Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Description:"));
        dialog.add(new JScrollPane(descArea));
        
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(e -> {
            try {
                // Validate input fields
                if (nameField.getText().trim().isEmpty() || 
                    dateField.getText().trim().isEmpty() ||
                    timeField.getText().trim().isEmpty() ||
                    venueField.getText().trim().isEmpty() ||
                    totalSeatsField.getText().trim().isEmpty() ||
                    priceField.getText().trim().isEmpty()) {
                    
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill all required fields", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create Event object
                Event event = new Event();
                event.setEventName(nameField.getText().trim());
                event.setEventDate(java.sql.Date.valueOf(dateField.getText().trim()));
                event.setEventTime(java.sql.Time.valueOf(timeField.getText().trim()));
                event.setVenue(venueField.getText().trim());
                
                int totalSeats = Integer.parseInt(totalSeatsField.getText().trim());
                event.setTotalSeats(totalSeats);
                event.setAvailableSeats(totalSeats); // Initially all seats are available
                
                double ticketPrice = Double.parseDouble(priceField.getText().trim());
                event.setTicketPrice(ticketPrice);
                
                event.setDescription(descArea.getText().trim());
                
                // Save event to database
                Event createdEvent = eventService.createEvent(
                    nameField.getText().trim(),
                    descArea.getText().trim(),
                    venueField.getText().trim(),
                    java.sql.Date.valueOf(dateField.getText().trim()),
                    java.sql.Time.valueOf(timeField.getText().trim()),
                    Integer.parseInt(totalSeatsField.getText().trim()),
                    Double.parseDouble(priceField.getText().trim()),
                    1  // createdBy (admin ID)
                );
                
                if (createdEvent != null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Event created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadEvents(); // Reload the events table
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to create event. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Invalid number format. Please check total seats and price fields.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.add(saveBtn);
        dialog.add(cancelBtn);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditEventDialog() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Similar to add dialog but with pre-filled values
    }

    private void deleteSelectedEvent() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this event?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            int eventId = (Integer) eventsTable.getValueAt(selectedRow, 0);
            try {
                eventService.deleteEvent(eventId);
                loadEvents();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting event: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showTicketTypeDialog() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to manage tickets",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int eventId = (Integer) eventsTable.getValueAt(selectedRow, 0);
            Event event = eventService.getEventById(eventId);
            if (event != null) {
                TicketTypeDialog dialog = new TicketTypeDialog(this, event);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Event not found",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading event: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddTicketTypeDialog() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event for the new ticket type",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int eventId = (Integer) eventsTable.getValueAt(selectedRow, 0);
            Event event = eventService.getEventById(eventId);
            
            if (event == null) {
                JOptionPane.showMessageDialog(this, "Event not found",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog(this, "Add Ticket Type - " + event.getEventName(), true);
            dialog.setLayout(new GridLayout(5, 2, 5, 5));
            
            JTextField categoryField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField quantityField = new JTextField();
            JCheckBox activeCheckBox = new JCheckBox("", true);
            
            dialog.add(new JLabel("Category:"));
            dialog.add(categoryField);
            dialog.add(new JLabel("Price:"));
            dialog.add(priceField);
            dialog.add(new JLabel("Available Quantity:"));
            dialog.add(quantityField);
            dialog.add(new JLabel("Active:"));
            dialog.add(activeCheckBox);
            
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            
            saveButton.addActionListener(e -> {
                try {
                    if (categoryField.getText().trim().isEmpty() || 
                        priceField.getText().trim().isEmpty() ||
                        quantityField.getText().trim().isEmpty()) {
                        
                        JOptionPane.showMessageDialog(dialog, 
                            "Please fill all required fields", 
                            "Validation Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Create ticket type object
                    TicketType ticketType = new TicketType();
                    ticketType.setEventId(event.getEventId());
                    ticketType.setCategory(categoryField.getText().trim());
                    ticketType.setPrice(Double.parseDouble(priceField.getText().trim()));
                    ticketType.setAvailableQuantity(Integer.parseInt(quantityField.getText().trim()));
                    ticketType.setActive(activeCheckBox.isSelected());
                    
                    // Save ticket type
                    boolean success = ticketTypeService.addTicketType(ticketType);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(dialog,
                            "Ticket type added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        
                        // Reload ticket types
                        loadTicketTypes();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Failed to add ticket type. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Invalid number format. Please check price and quantity fields.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Database error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            dialog.add(saveButton);
            dialog.add(cancelButton);
            
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditTicketTypeDialog() {
        int selectedRow = ticketTypesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket type to edit",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int ticketTypeId = (Integer) ticketTypesTable.getValueAt(selectedRow, 0);
            String eventName = (String) ticketTypesTable.getValueAt(selectedRow, 1);
            
            // Get all events to find the right one
            List<Event> events = eventService.getAllEvents();
            Event event = null;
            for (Event e : events) {
                if (e.getEventName().equals(eventName)) {
                    event = e;
                    break;
                }
            }
            
            if (event == null) {
                JOptionPane.showMessageDialog(this, "Event not found",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get the ticket type
            List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEventId(event.getEventId());
            final TicketType selectedType = ticketTypes.stream()
                .filter(t -> t.getTicketTypeId() == ticketTypeId)
                .findFirst()
                .orElse(null);
            
            if (selectedType == null) {
                JOptionPane.showMessageDialog(this, "Ticket type not found",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create dialog for editing
            JDialog dialog = new JDialog(this, "Edit Ticket Type - " + eventName, true);
            dialog.setLayout(new GridLayout(5, 2, 5, 5));
            
            JTextField categoryField = new JTextField(selectedType.getCategory());
            JTextField priceField = new JTextField(String.valueOf(selectedType.getPrice()));
            JTextField quantityField = new JTextField(String.valueOf(selectedType.getAvailableQuantity()));
            JCheckBox activeCheckBox = new JCheckBox("", selectedType.isActive());
            
            dialog.add(new JLabel("Category:"));
            dialog.add(categoryField);
            dialog.add(new JLabel("Price:"));
            dialog.add(priceField);
            dialog.add(new JLabel("Available Quantity:"));
            dialog.add(quantityField);
            dialog.add(new JLabel("Active:"));
            dialog.add(activeCheckBox);
            
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            
            saveButton.addActionListener(e -> {
                try {
                    if (categoryField.getText().trim().isEmpty() || 
                        priceField.getText().trim().isEmpty() ||
                        quantityField.getText().trim().isEmpty()) {
                        
                        JOptionPane.showMessageDialog(dialog, 
                            "Please fill all required fields", 
                            "Validation Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Update ticket type
                    selectedType.setCategory(categoryField.getText().trim());
                    selectedType.setPrice(Double.parseDouble(priceField.getText().trim()));
                    selectedType.setAvailableQuantity(Integer.parseInt(quantityField.getText().trim()));
                    selectedType.setActive(activeCheckBox.isSelected());
                    
                    // Save changes
                    boolean success = ticketTypeService.updateTicketType(selectedType);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(dialog,
                            "Ticket type updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        
                        // Reload ticket types
                        loadTicketTypes();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Failed to update ticket type. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Invalid number format. Please check price and quantity fields.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Database error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            dialog.add(saveButton);
            dialog.add(cancelButton);
            
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTicketType() {
        int selectedRow = ticketTypesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket type to delete",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int ticketTypeId = (Integer) ticketTypesTable.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this ticket type?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = ticketTypeService.deleteTicketType(ticketTypeId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Ticket type deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload ticket types
                    loadTicketTypes();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete ticket type. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showBookingSummary() {
        try {
            // Create a dialog to show booking summary
            JDialog dialog = new JDialog(this, "Booking Summary", true);
            dialog.setLayout(new BorderLayout(5, 5));
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);

            // Create table model for booking summary
            String[] columns = {"Event Name", "Total Tickets", "Booked Tickets", "Available Tickets", "Revenue"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable summaryTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(summaryTable);

            // Get all events
            List<Event> events = eventService.getAllEvents();
            double totalRevenue = 0;
            
            // Create TicketService instance to query ticket data
            TicketService ticketService = new TicketService();

            // Populate table with booking summary for each event
            for (Event event : events) {
                // Get all tickets for this event to calculate accurate booking numbers
                List<Ticket> eventTickets = ticketService.getTicketsByEventId(event.getEventId());
                
                // Count only paid tickets
                int paidTickets = 0;
                double eventRevenue = 0;
                
                for (Ticket ticket : eventTickets) {
                    if ("PAID".equals(ticket.getPaymentStatus())) {
                        paidTickets += ticket.getQuantity();
                        eventRevenue += ticket.getTotalPrice();
                    }
                }
                
                // Get event's total tickets from ticket types
                List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEventId(event.getEventId());
                int totalTickets = 0;
                
                for (TicketType type : ticketTypes) {
                    totalTickets += type.getAvailableQuantity();
                }
                
                // Calculate available tickets
                int availableTickets = totalTickets - paidTickets;
                
                model.addRow(new Object[]{
                    event.getEventName(),
                    totalTickets,
                    paidTickets,
                    availableTickets,
                    String.format("$%.2f", eventRevenue)
                });

                totalRevenue += eventRevenue;
            }

            // Add total row
            model.addRow(new Object[]{
                "TOTAL",
                "-",
                "-",
                "-",
                String.format("$%.2f", totalRevenue)
            });

            // Add components to dialog
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.setForeground(Color.BLACK);
            closeButton.addActionListener(e -> dialog.dispose());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error generating booking summary: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRevenueReport() {
        try {
            // Create a dialog to show revenue report
            JDialog dialog = new JDialog(this, "Revenue Report", true);
            dialog.setLayout(new BorderLayout(5, 5));
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);

            // Create table model for revenue report
            String[] columns = {"Event Name", "Ticket Type", "Price", "Sold", "Revenue"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable revenueTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(revenueTable);

            // Get all events
            List<Event> events = eventService.getAllEvents();
            double totalRevenue = 0;
            
            // Create TicketService instance to query ticket data
            TicketService ticketService = new TicketService();
            
            // Populate table with revenue details for each event and ticket type
            for (Event event : events) {
                List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEventId(event.getEventId());
                double eventRevenue = 0;
                
                // Get all tickets for this event
                List<Ticket> eventTickets = ticketService.getTicketsByEventId(event.getEventId());
                
                for (TicketType type : ticketTypes) {
                    // Calculate sold tickets from actual tickets data
                    int soldTickets = 0;
                    double typeRevenue = 0;
                    
                    // In a real implementation, we would match tickets to ticket types
                    // For now, we'll just calculate based on the price
                    for (Ticket ticket : eventTickets) {
                        if ("PAID".equals(ticket.getPaymentStatus()) && 
                            Math.abs(ticket.getTotalPrice()/ticket.getQuantity() - type.getPrice()) < 0.01) {
                            soldTickets += ticket.getQuantity();
                            typeRevenue += ticket.getTotalPrice();
                        }
                    }

                    model.addRow(new Object[]{
                        event.getEventName(),
                        type.getCategory(),
                        String.format("$%.2f", type.getPrice()),
                        soldTickets,
                        String.format("$%.2f", typeRevenue)
                    });

                    eventRevenue += typeRevenue;
                }

                // Add event subtotal
                model.addRow(new Object[]{
                    event.getEventName() + " (Subtotal)",
                    "-",
                    "-",
                    "-",
                    String.format("$%.2f", eventRevenue)
                });

                totalRevenue += eventRevenue;
            }

            // Add grand total
            model.addRow(new Object[]{
                "GRAND TOTAL",
                "-",
                "-",
                "-",
                String.format("$%.2f", totalRevenue)
            });

            // Add components to dialog
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.setForeground(Color.BLACK);
            closeButton.addActionListener(e -> dialog.dispose());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error generating revenue report: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
} 