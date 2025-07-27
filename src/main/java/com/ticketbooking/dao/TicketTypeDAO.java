package com.ticketbooking.dao;

import com.ticketbooking.model.TicketType;
import com.ticketbooking.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketTypeDAO {
    public TicketTypeDAO() {
        // No need to get a connection in constructor
    }

    public boolean addTicketType(TicketType ticketType) throws SQLException {
        String sql = "INSERT INTO ticket_types (event_id, category, price, available_quantity, is_active) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, ticketType.getEventId());
            stmt.setString(2, ticketType.getCategory());
            stmt.setDouble(3, ticketType.getPrice());
            stmt.setInt(4, ticketType.getAvailableQuantity());
            stmt.setBoolean(5, ticketType.isActive());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ticketType.setTicketTypeId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean updateTicketType(TicketType ticketType) throws SQLException {
        String sql = "UPDATE ticket_types SET category = ?, price = ?, available_quantity = ?, is_active = ? WHERE ticket_type_id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, ticketType.getCategory());
            stmt.setDouble(2, ticketType.getPrice());
            stmt.setInt(3, ticketType.getAvailableQuantity());
            stmt.setBoolean(4, ticketType.isActive());
            stmt.setInt(5, ticketType.getTicketTypeId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    public List<TicketType> getTicketTypesByEventId(int eventId) throws SQLException {
        List<TicketType> ticketTypes = new ArrayList<>();
        String sql = "SELECT * FROM ticket_types WHERE event_id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TicketType ticketType = new TicketType(
                        rs.getInt("ticket_type_id"),
                        rs.getInt("event_id"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("available_quantity"),
                        rs.getBoolean("is_active")
                    );
                    ticketTypes.add(ticketType);
                }
            }
        }
        return ticketTypes;
    }

    public boolean deleteTicketType(int ticketTypeId) throws SQLException {
        String sql = "DELETE FROM ticket_types WHERE ticket_type_id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketTypeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateAvailableQuantity(int ticketTypeId, int quantity) throws SQLException {
        String sql = "UPDATE ticket_types SET available_quantity = ? WHERE ticket_type_id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, ticketTypeId);
            return stmt.executeUpdate() > 0;
        }
    }
} 