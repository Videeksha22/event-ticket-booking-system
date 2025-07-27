package com.ticketbooking.service;

import com.ticketbooking.dao.TicketDAO;
import com.ticketbooking.dao.TicketDetailDAO;
import com.ticketbooking.model.Event;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.TicketDetail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling ticket-related business logic
 */
public class TicketService {
    private TicketDAO ticketDAO;
    private TicketDetailDAO ticketDetailDAO;
    private EventService eventService;
    
    public TicketService() throws SQLException {
        this.ticketDAO = new TicketDAO();
        this.ticketDetailDAO = new TicketDetailDAO();
        this.eventService = new EventService();
    }
    
    /**
     * Book tickets for an event
     * 
     * @param eventId Event ID
     * @param userId User ID
     * @param quantity Number of tickets
     * @param attendeeNames List of attendee names
     * @return Created ticket or null if booking failed
     */
    public Ticket bookTickets(int eventId, int userId, int quantity, List<String> attendeeNames) throws SQLException {
        // Check if event exists and has enough seats
        Event event = eventService.getEventById(eventId);
        if (event == null || event.getAvailableSeats() < quantity) {
            return null;
        }
        
        // Calculate total price
        double totalPrice = event.getTicketPrice() * quantity;
        
        // Create ticket
        Ticket ticket = new Ticket(eventId, userId, quantity, totalPrice, "PENDING");
        ticket = ticketDAO.createTicket(ticket);
        
        if (ticket != null) {
            // Create ticket details
            List<TicketDetail> details = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                String seatNumber = generateSeatNumber(event, i + 1);
                String attendeeName = (i < attendeeNames.size()) ? attendeeNames.get(i) : "Guest";
                
                TicketDetail detail = new TicketDetail(ticket.getTicketId(), seatNumber, attendeeName);
                details.add(detail);
            }
            
            ticketDetailDAO.createTicketDetailsBatch(details);
        }
        
        return ticket;
    }
    
    /**
     * Get ticket by ID
     * 
     * @param ticketId Ticket ID
     * @return Ticket object if found, null otherwise
     */
    public Ticket getTicketById(int ticketId) {
        return ticketDAO.getTicketById(ticketId);
    }
    
    /**
     * Get tickets by user ID
     * 
     * @param userId User ID
     * @return List of tickets for the user
     */
    public List<Ticket> getTicketsByUserId(int userId) throws SQLException {
        return ticketDAO.getTicketsByUserId(userId);
    }
    
    /**
     * Get tickets by event ID
     * 
     * @param eventId Event ID
     * @return List of tickets for the event
     */
    public List<Ticket> getTicketsByEventId(int eventId) throws SQLException {
        return ticketDAO.getTicketsByEventId(eventId);
    }
    
    /**
     * Cancel a ticket
     * 
     * @param ticketId Ticket ID
     * @return true if cancellation successful, false otherwise
     */
    public boolean cancelTicket(int ticketId) throws SQLException {
        return ticketDAO.cancelTicket(ticketId);
    }
    
    /**
     * Get ticket details for a ticket
     * 
     * @param ticketId Ticket ID
     * @return List of ticket details
     */
    public List<TicketDetail> getTicketDetails(int ticketId) {
        return ticketDetailDAO.getTicketDetailsByTicketId(ticketId);
    }
    
    /**
     * Generate a seat number
     * 
     * @param event Event
     * @param seatIndex Seat index
     * @return Generated seat number
     */
    private String generateSeatNumber(Event event, int seatIndex) {
        // Simple seat number generation logic
        // For more complex venues, this would be more sophisticated
        int row = (seatIndex - 1) / 10 + 1;
        int seat = (seatIndex - 1) % 10 + 1;
        
        char rowChar = (char)('A' + row - 1);
        return rowChar + "-" + seat;
    }

    public Ticket bookTicket(int eventId, int userId, int quantity) throws SQLException {
        // Check if event exists and has enough seats
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return null;
        }
        
        // Check available seats
        if (event.getAvailableSeats() < quantity) {
            return null;
        }
        
        // Calculate total price
        double totalPrice = event.getTicketPrice() * quantity;
        
        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setEventId(eventId);
        ticket.setUserId(userId);
        ticket.setQuantity(quantity);
        ticket.setTotalPrice(totalPrice);
        ticket.setPaymentStatus("PENDING");
        
        // Save ticket to database
        return ticketDAO.createTicket(ticket);
    }

    public boolean updatePaymentStatus(int ticketId, String status) throws SQLException {
        return ticketDAO.updatePaymentStatus(ticketId, status);
    }

    public String generateSeatNumber(int row, int seat) throws SQLException {
        char rowChar = (char) ('A' + row - 1);
        return rowChar + "-" + seat;
    }
} 