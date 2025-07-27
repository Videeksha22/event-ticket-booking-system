package com.ticketbooking.dao;

import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.Event;
import com.ticketbooking.model.User;
import com.ticketbooking.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    
    private EventDAO eventDAO = new EventDAO();
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Get all tickets from the database
     * 
     * @return List of all tickets
     */
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Ticket ticket = mapResultSetToTicket(rs);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tickets: " + e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Get ticket by ID
     * 
     * @param ticketId The ticket ID to search for
     * @return Ticket object if found, null otherwise
     */
    public Ticket getTicketById(int ticketId) {
        String query = "SELECT * FROM tickets WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, ticketId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ticket ticket = mapResultSetToTicket(rs);
                    
                    // Load associated event and user
                    Event event = eventDAO.getEventById(ticket.getEventId());
                    User user = userDAO.getUserById(ticket.getUserId());
                    
                    ticket.setEvent(event);
                    ticket.setUser(user);
                    
                    return ticket;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ticket by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get tickets by user ID
     * 
     * @param userId The user ID to search for
     * @return List of tickets for the specified user
     */
    public List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = mapResultSetToTicket(rs);
                    
                    // Load associated event
                    Event event = eventDAO.getEventById(ticket.getEventId());
                    ticket.setEvent(event);
                    
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tickets by user ID: " + e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Get tickets by event ID
     * 
     * @param eventId The event ID to search for
     * @return List of tickets for the specified event
     */
    public List<Ticket> getTicketsByEventId(int eventId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE event_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, eventId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = mapResultSetToTicket(rs);
                    
                    // Load associated user
                    User user = userDAO.getUserById(ticket.getUserId());
                    ticket.setUser(user);
                    
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tickets by event ID: " + e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Create a new ticket in the database
     * 
     * @param ticket The ticket object to create
     * @return The created ticket with ID set, or null if creation failed
     */
    public Ticket createTicket(Ticket ticket) {
        String query = "INSERT INTO tickets (event_id, user_id, quantity, total_price, payment_status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, ticket.getEventId());
            pstmt.setInt(2, ticket.getUserId());
            pstmt.setInt(3, ticket.getQuantity());
            pstmt.setDouble(4, ticket.getTotalPrice());
            pstmt.setString(5, ticket.getPaymentStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticket.setTicketId(generatedKeys.getInt(1));
                        
                        // Update available seats in the event
                        eventDAO.updateAvailableSeats(ticket.getEventId(), ticket.getQuantity());
                        
                        return ticket;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating ticket: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update an existing ticket
     * 
     * @param ticket The ticket object with updated values
     * @return true if update successful, false otherwise
     */
    public boolean updateTicket(Ticket ticket) {
        String query = "UPDATE tickets SET payment_status = ? WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, ticket.getPaymentStatus());
            pstmt.setInt(2, ticket.getTicketId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cancel a ticket by ID
     * 
     * @param ticketId The ID of the ticket to cancel
     * @return true if cancellation successful, false otherwise
     */
    public boolean cancelTicket(int ticketId) {
        // First get the ticket to know how many seats to add back
        Ticket ticket = getTicketById(ticketId);
        if (ticket == null) {
            return false;
        }
        
        String query = "UPDATE tickets SET payment_status = 'CANCELLED' WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, ticketId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Update event to add seats back
                String updateEventQuery = "UPDATE events SET available_seats = available_seats + ? WHERE event_id = ?";
                try (PreparedStatement eventPstmt = conn.prepareStatement(updateEventQuery)) {
                    eventPstmt.setInt(1, ticket.getQuantity());
                    eventPstmt.setInt(2, ticket.getEventId());
                    eventPstmt.executeUpdate();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error cancelling ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to map ResultSet to Ticket object
     * 
     * @param rs The ResultSet containing ticket data
     * @return Mapped Ticket object
     * @throws SQLException if a database access error occurs
     */
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketId(rs.getInt("ticket_id"));
        ticket.setEventId(rs.getInt("event_id"));
        ticket.setUserId(rs.getInt("user_id"));
        ticket.setQuantity(rs.getInt("quantity"));
        ticket.setTotalPrice(rs.getDouble("total_price"));
        ticket.setBookingDate(rs.getTimestamp("booking_date"));
        ticket.setPaymentStatus(rs.getString("payment_status"));
        return ticket;
    }

    public boolean updatePaymentStatus(int ticketId, String status) throws SQLException {
        String sql = "UPDATE tickets SET payment_status = ? WHERE ticket_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, ticketId);
            
            return stmt.executeUpdate() > 0;
        }
    }
} 