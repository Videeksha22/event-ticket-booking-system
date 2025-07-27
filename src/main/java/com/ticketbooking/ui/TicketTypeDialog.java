package com.ticketbooking.ui;

import com.ticketbooking.model.Event;
import com.ticketbooking.model.TicketType;
import com.ticketbooking.service.EventService;
import com.ticketbooking.service.TicketTypeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TicketTypeDialog extends JDialog {
    private Event event;
    private TicketTypeService ticketTypeService;
    private EventService eventService;
    private JTable ticketTypesTable;
    private DefaultTableModel tableModel;

    public TicketTypeDialog(JFrame parent, Event event) {
        super(parent, "Manage Ticket Types - " + event.getEventName(), true);
        this.event = event;
        
        try {
            this.ticketTypeService = new TicketTypeService();
            this.eventService = new EventService();

            initializeComponents();
            loadTicketTypes();
            
            setSize(600, 400);
            setLocationRelativeTo(parent);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error initializing services: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(5, 5));

        // Create table
        String[] columns = {"ID", "Category", "Price", "Available", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ticketTypesTable = new JTable(tableModel);
        
        // Style the table
        ticketTypesTable.getTableHeader().setBackground(new Color(70, 130, 180));
        ticketTypesTable.getTableHeader().setForeground(Color.BLACK);
        ticketTypesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(ticketTypesTable);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Ticket Type");
        addButton.setForeground(Color.BLACK);
        JButton editButton = new JButton("Edit Ticket Type");
        editButton.setForeground(Color.BLACK);
        JButton deleteButton = new JButton("Delete Ticket Type");
        deleteButton.setForeground(Color.BLACK);
        JButton closeButton = new JButton("Close");
        closeButton.setForeground(Color.BLACK);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Add components
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> showAddTicketTypeDialog());
        editButton.addActionListener(e -> showEditTicketTypeDialog());
        deleteButton.addActionListener(e -> deleteSelectedTicketType());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadTicketTypes() {
        try {
            tableModel.setRowCount(0);
            List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEventId(event.getEventId());
            for (TicketType type : ticketTypes) {
                tableModel.addRow(new Object[]{
                    type.getTicketTypeId(),
                    type.getCategory(),
                    String.format("$%.2f", type.getPrice()),
                    type.getAvailableQuantity(),
                    type.isActive() ? "Active" : "Inactive"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading ticket types: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddTicketTypeDialog() {
        JDialog dialog = new JDialog(this, "Add Ticket Type", true);
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
        saveButton.setForeground(Color.BLACK);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.BLACK);

        saveButton.addActionListener(e -> {
            try {
                TicketType ticketType = new TicketType();
                ticketType.setEventId(event.getEventId());
                ticketType.setCategory(categoryField.getText());
                ticketType.setPrice(Double.parseDouble(priceField.getText()));
                ticketType.setAvailableQuantity(Integer.parseInt(quantityField.getText()));
                ticketType.setActive(activeCheckBox.isSelected());

                if (ticketTypeService.addTicketType(ticketType)) {
                    loadTicketTypes();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to add ticket type",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Invalid number format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error adding ticket type: " + ex.getMessage(),
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
    }

    private void showEditTicketTypeDialog() {
        int selectedRow = ticketTypesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket type to edit",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int ticketTypeId = (Integer) ticketTypesTable.getValueAt(selectedRow, 0);
            List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEventId(event.getEventId());
            TicketType selectedType = ticketTypes.stream()
                .filter(t -> t.getTicketTypeId() == ticketTypeId)
                .findFirst()
                .orElse(null);

            if (selectedType == null) {
                JOptionPane.showMessageDialog(this,
                    "Ticket type not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog(this, "Edit Ticket Type", true);
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
            saveButton.setForeground(Color.BLACK);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setForeground(Color.BLACK);

            saveButton.addActionListener(e -> {
                try {
                    selectedType.setCategory(categoryField.getText());
                    selectedType.setPrice(Double.parseDouble(priceField.getText()));
                    selectedType.setAvailableQuantity(Integer.parseInt(quantityField.getText()));
                    selectedType.setActive(activeCheckBox.isSelected());

                    if (ticketTypeService.updateTicketType(selectedType)) {
                        loadTicketTypes();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Failed to update ticket type",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Invalid number format",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Error updating ticket type: " + ex.getMessage(),
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
            JOptionPane.showMessageDialog(this,
                "Error loading ticket type: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTicketType() {
        int selectedRow = ticketTypesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket type to delete",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this ticket type?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int ticketTypeId = (Integer) ticketTypesTable.getValueAt(selectedRow, 0);
                if (ticketTypeService.deleteTicketType(ticketTypeId)) {
                    loadTicketTypes();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete ticket type",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting ticket type: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 