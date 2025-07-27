package com.ticketbooking.dao;

import com.ticketbooking.model.Event;
import com.ticketbooking.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    
    /**
     * Get all events from the database
     * 
     * @return List of all events
     * @throws SQLException if a database error occurs
     */
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
        }
        return events;
    }
    
    /**
     * Get event by ID
     * 
     * @param eventId The event ID to search for
     * @return Event object if found, null otherwise
     */
    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Event event = new Event();
                    event.setEventId(rs.getInt("event_id"));
                    event.setEventName(rs.getString("event_name"));
                    event.setDescription(rs.getString("description"));
                    event.setVenue(rs.getString("venue"));
                    event.setEventDate(rs.getDate("event_date"));
                    event.setEventTime(rs.getTime("event_time"));
                    event.setTotalSeats(rs.getInt("total_seats"));
                    event.setAvailableSeats(rs.getInt("available_seats"));
                    event.setTicketPrice(rs.getDouble("ticket_price"));
                    event.setCreatedBy(rs.getInt("created_by"));
                    event.setEventStatus(rs.getString("event_status"));
                    return event;
                }
            }
        }
        return null;
    }
    
    /**
     * Get events by status
     * 
     * @param status The event status to filter by
     * @return List of events with the specified status
     */
    public List<Event> getEventsByStatus(String status) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE event_status = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Event event = mapResultSetToEvent(rs);
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving events by status: " + e.getMessage());
        }
        
        return events;
    }
    
    /**
     * Create a new event in the database
     * 
     * @param event The event object to create
     * @return The created event with ID set, or null if creation failed
     */
    public Event createEvent(Event event) {
        String query = "INSERT INTO events (event_name, description, venue, event_date, event_time, " +
                      "total_seats, available_seats, ticket_price, event_status, created_by) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, event.getEventName());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getVenue());
            pstmt.setDate(4, event.getEventDate());
            pstmt.setTime(5, event.getEventTime());
            pstmt.setInt(6, event.getTotalSeats());
            pstmt.setInt(7, event.getAvailableSeats());
            pstmt.setDouble(8, event.getTicketPrice());
            pstmt.setString(9, event.getEventStatus());
            pstmt.setInt(10, event.getCreatedBy());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        event.setEventId(generatedKeys.getInt(1));
                        return event;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating event: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update an existing event
     * 
     * @param event The event object with updated values
     * @return true if update successful, false otherwise
     */
    public boolean updateEvent(Event event) {
        String query = "UPDATE events SET event_name = ?, description = ?, venue = ?, " +
                      "event_date = ?, event_time = ?, total_seats = ?, available_seats = ?, " +
                      "ticket_price = ?, event_status = ? WHERE event_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, event.getEventName());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getVenue());
            pstmt.setDate(4, event.getEventDate());
            pstmt.setTime(5, event.getEventTime());
            pstmt.setInt(6, event.getTotalSeats());
            pstmt.setInt(7, event.getAvailableSeats());
            pstmt.setDouble(8, event.getTicketPrice());
            pstmt.setString(9, event.getEventStatus());
            pstmt.setInt(10, event.getEventId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete an event by ID
     * 
     * @param eventId The ID of the event to delete
     * @return true if deletion successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteEvent(int eventId) throws SQLException {
        String query = "DELETE FROM events WHERE event_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, eventId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    /**
     * Update available seats for an event
     * 
     * @param eventId The ID of the event
     * @param seatsToReduce Number of seats to reduce
     * @return true if update successful, false otherwise
     */
    public boolean updateAvailableSeats(int eventId, int seatsToReduce) {
        String query = "UPDATE events SET available_seats = available_seats - ? WHERE event_id = ? AND available_seats >= ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, seatsToReduce);
            pstmt.setInt(2, eventId);
            pstmt.setInt(3, seatsToReduce);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating available seats: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Search events by name or venue
     * 
     * @param searchTerm The search term to look for in event name or venue
     * @return List of matching events
     */
    public List<Event> searchEvents(String searchTerm) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events WHERE event_name LIKE ? OR venue LIKE ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Event event = mapResultSetToEvent(rs);
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching events: " + e.getMessage());
        }
        
        return events;
    }
    
    /**
     * Helper method to map ResultSet to Event object
     * 
     * @param rs The ResultSet containing event data
     * @return Mapped Event object
     * @throws SQLException if a database access error occurs
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setEventName(rs.getString("event_name"));
        event.setDescription(rs.getString("description"));
        event.setVenue(rs.getString("venue"));
        event.setEventDate(rs.getDate("event_date"));
        event.setEventTime(rs.getTime("event_time"));
        event.setTotalSeats(rs.getInt("total_seats"));
        event.setAvailableSeats(rs.getInt("available_seats"));
        event.setTicketPrice(rs.getDouble("ticket_price"));
        event.setEventStatus(rs.getString("event_status"));
        event.setCreatedBy(rs.getInt("created_by"));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        return event;
    }
} 