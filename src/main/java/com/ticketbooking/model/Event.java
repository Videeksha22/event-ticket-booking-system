package com.ticketbooking.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Event {
    private int eventId;
    private String eventName;
    private String description;
    private String venue;
    private Date eventDate;
    private Time eventTime;
    private int totalSeats;
    private int availableSeats;
    private double ticketPrice;
    private String eventStatus;
    private int createdBy;
    private Timestamp createdAt;

    // Default constructor
    public Event() {
    }

    // Constructor with all fields
    public Event(int eventId, String eventName, String description, String venue, Date eventDate, 
                 Time eventTime, int totalSeats, int availableSeats, double ticketPrice, 
                 String eventStatus, int createdBy, Timestamp createdAt) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.venue = venue;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
        this.eventStatus = eventStatus;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Constructor for creating new event (without eventId and createdAt)
    public Event(String eventName, String description, String venue, Date eventDate, 
                Time eventTime, int totalSeats, int availableSeats, double ticketPrice, 
                String eventStatus, int createdBy) {
        this.eventName = eventName;
        this.description = description;
        this.venue = venue;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
        this.eventStatus = eventStatus;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Time getEventTime() {
        return eventTime;
    }

    public void setEventTime(Time eventTime) {
        this.eventTime = eventTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", venue='" + venue + '\'' +
                ", eventDate=" + eventDate +
                ", eventTime=" + eventTime +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                ", ticketPrice=" + ticketPrice +
                ", eventStatus='" + eventStatus + '\'' +
                '}';
    }
} 