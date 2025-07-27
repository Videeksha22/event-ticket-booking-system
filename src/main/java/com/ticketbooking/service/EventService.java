package com.ticketbooking.service;

import com.ticketbooking.dao.EventDAO;
import com.ticketbooking.model.Event;

import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class for handling event-related business logic
 */
public class EventService {
    private EventDAO eventDAO;
    
    public EventService() {
        eventDAO = new EventDAO();
    }
    
    /**
     * Get all events
     * 
     * @return List of all events
     * @throws SQLException if a database error occurs
     */
    public List<Event> getAllEvents() throws SQLException {
        return eventDAO.getAllEvents();
    }
    
    /**
     * Get upcoming events
     * 
     * @return List of upcoming events
     */
    public List<Event> getUpcomingEvents() {
        return eventDAO.getEventsByStatus("UPCOMING");
    }
    
    /**
     * Get ongoing events
     * 
     * @return List of ongoing events
     */
    public List<Event> getOngoingEvents() {
        return eventDAO.getEventsByStatus("ONGOING");
    }
    
    /**
     * Get completed events
     * 
     * @return List of completed events
     */
    public List<Event> getCompletedEvents() {
        return eventDAO.getEventsByStatus("COMPLETED");
    }
    
    /**
     * Get cancelled events
     * 
     * @return List of cancelled events
     */
    public List<Event> getCancelledEvents() {
        return eventDAO.getEventsByStatus("CANCELLED");
    }
    
    /**
     * Get event by ID
     * 
     * @param eventId Event ID
     * @return Event object if found, null otherwise
     */
    public Event getEventById(int eventId) throws SQLException {
        return eventDAO.getEventById(eventId);
    }
    
    /**
     * Create a new event
     * 
     * @param eventName Event name
     * @param description Event description
     * @param venue Event venue
     * @param eventDate Event date
     * @param eventTime Event time
     * @param totalSeats Total number of seats
     * @param ticketPrice Ticket price
     * @param createdBy ID of the user creating the event
     * @return Created event or null if creation failed
     */
    public Event createEvent(String eventName, String description, String venue, 
                           Date eventDate, Time eventTime, int totalSeats, 
                           double ticketPrice, int createdBy) {
        Event event = new Event(eventName, description, venue, eventDate, eventTime, 
                              totalSeats, totalSeats, ticketPrice, "UPCOMING", createdBy);
        return eventDAO.createEvent(event);
    }
    
    /**
     * Update an event
     * 
     * @param event Event object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateEvent(Event event) {
        return eventDAO.updateEvent(event);
    }
    
    /**
     * Delete an event
     * 
     * @param eventId ID of the event to delete
     * @return true if deletion successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteEvent(int eventId) throws SQLException {
        return eventDAO.deleteEvent(eventId);
    }
    
    /**
     * Cancel an event
     * 
     * @param eventId ID of the event to cancel
     * @return true if cancellation successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean cancelEvent(int eventId) throws SQLException {
        Event event = eventDAO.getEventById(eventId);
        if (event == null) {
            return false;
        }
        
        event.setEventStatus("CANCELLED");
        return eventDAO.updateEvent(event);
    }
    
    /**
     * Search events by name or venue
     * 
     * @param searchTerm Search term
     * @return List of matching events
     */
    public List<Event> searchEvents(String searchTerm) {
        return eventDAO.searchEvents(searchTerm);
    }
    
    /**
     * Check if an event has enough available seats
     * 
     * @param eventId Event ID
     * @param requestedSeats Number of seats requested
     * @return true if enough seats available, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean hasEnoughSeats(int eventId, int requestedSeats) throws SQLException {
        Event event = eventDAO.getEventById(eventId);
        return event != null && event.getAvailableSeats() >= requestedSeats;
    }
} 