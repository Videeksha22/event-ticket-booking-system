package com.ticketbooking.dao;

import com.ticketbooking.model.TicketDetail;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDetailDAO {
    
    private TicketDAO ticketDAO = new TicketDAO();
    
    /**
     * Get all ticket details from the database
     * 
     * @return List of all ticket details
     */
    public List<TicketDetail> getAllTicketDetails() {
        List<TicketDetail> ticketDetails = new ArrayList<>();
        String query = "SELECT * FROM ticket_details";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                TicketDetail detail = mapResultSetToTicketDetail(rs);
                ticketDetails.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ticket details: " + e.getMessage());
        }
        
        return ticketDetails;
    }
    
    /**
     * Get ticket detail by ID
     * 
     * @param detailId The detail ID to search for
     * @return TicketDetail object if found, null otherwise
     */
    public TicketDetail getTicketDetailById(int detailId) {
        String query = "SELECT * FROM ticket_details WHERE detail_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, detailId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TicketDetail detail = mapResultSetToTicketDetail(rs);
                    
                    // Load associated ticket
                    Ticket ticket = ticketDAO.getTicketById(detail.getTicketId());
                    detail.setTicket(ticket);
                    
                    return detail;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ticket detail by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get ticket details by ticket ID
     * 
     * @param ticketId The ticket ID to search for
     * @return List of ticket details for the specified ticket
     */
    public List<TicketDetail> getTicketDetailsByTicketId(int ticketId) {
        List<TicketDetail> ticketDetails = new ArrayList<>();
        String query = "SELECT * FROM ticket_details WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, ticketId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TicketDetail detail = mapResultSetToTicketDetail(rs);
                    ticketDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ticket details by ticket ID: " + e.getMessage());
        }
        
        return ticketDetails;
    }
    
    /**
     * Create a new ticket detail in the database
     * 
     * @param ticketDetail The ticket detail object to create
     * @return The created ticket detail with ID set, or null if creation failed
     */
    public TicketDetail createTicketDetail(TicketDetail ticketDetail) {
        String query = "INSERT INTO ticket_details (ticket_id, seat_number, attendee_name) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, ticketDetail.getTicketId());
            pstmt.setString(2, ticketDetail.getSeatNumber());
            pstmt.setString(3, ticketDetail.getAttendeeName());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticketDetail.setDetailId(generatedKeys.getInt(1));
                        return ticketDetail;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating ticket detail: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create multiple ticket details in batch
     * 
     * @param ticketDetails List of ticket details to create
     * @return Number of successfully created ticket details
     */
    public int createTicketDetailsBatch(List<TicketDetail> ticketDetails) {
        String query = "INSERT INTO ticket_details (ticket_id, seat_number, attendee_name) VALUES (?, ?, ?)";
        int count = 0;
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            for (TicketDetail detail : ticketDetails) {
                pstmt.setInt(1, detail.getTicketId());
                pstmt.setString(2, detail.getSeatNumber());
                pstmt.setString(3, detail.getAttendeeName());
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            
            for (int result : results) {
                if (result > 0) {
                    count++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating ticket details batch: " + e.getMessage());
        }
        
        return count;
    }
    
    /**
     * Update an existing ticket detail
     * 
     * @param ticketDetail The ticket detail object with updated values
     * @return true if update successful, false otherwise
     */
    public boolean updateTicketDetail(TicketDetail ticketDetail) {
        String query = "UPDATE ticket_details SET seat_number = ?, attendee_name = ? WHERE detail_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, ticketDetail.getSeatNumber());
            pstmt.setString(2, ticketDetail.getAttendeeName());
            pstmt.setInt(3, ticketDetail.getDetailId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating ticket detail: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a ticket detail by ID
     * 
     * @param detailId The ID of the ticket detail to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteTicketDetail(int detailId) {
        String query = "DELETE FROM ticket_details WHERE detail_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, detailId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting ticket detail: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete all ticket details for a specific ticket
     * 
     * @param ticketId The ticket ID for which to delete details
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteTicketDetailsByTicketId(int ticketId) {
        String query = "DELETE FROM ticket_details WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, ticketId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting ticket details by ticket ID: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to map ResultSet to TicketDetail object
     * 
     * @param rs The ResultSet containing ticket detail data
     * @return Mapped TicketDetail object
     * @throws SQLException if a database access error occurs
     */
    private TicketDetail mapResultSetToTicketDetail(ResultSet rs) throws SQLException {
        TicketDetail detail = new TicketDetail();
        detail.setDetailId(rs.getInt("detail_id"));
        detail.setTicketId(rs.getInt("ticket_id"));
        detail.setSeatNumber(rs.getString("seat_number"));
        detail.setAttendeeName(rs.getString("attendee_name"));
        return detail;
    }
} 