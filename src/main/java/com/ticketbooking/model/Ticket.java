package com.ticketbooking.model;

import java.sql.Timestamp;

public class Ticket {
    private int ticketId;
    private int eventId;
    private int userId;
    private int quantity;
    private double totalPrice;
    private Timestamp bookingDate;
    private String paymentStatus;
    
    // Associated objects for relationship navigation
    private Event event;
    private User user;

    // Default constructor
    public Ticket() {
    }

    // Constructor with all fields
    public Ticket(int ticketId, int eventId, int userId, int quantity, double totalPrice, 
                 Timestamp bookingDate, String paymentStatus) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.userId = userId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.paymentStatus = paymentStatus;
    }

    // Constructor for creating new tickets (without ticketId and bookingDate)
    public Ticket(int eventId, int userId, int quantity, double totalPrice, String paymentStatus) {
        this.eventId = eventId;
        this.userId = userId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", bookingDate=" + bookingDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
} 