package com.ticketbooking.model;

public class TicketDetail {
    private int detailId;
    private int ticketId;
    private String seatNumber;
    private String attendeeName;
    
    // Associated ticket for relationship navigation
    private Ticket ticket;

    // Default constructor
    public TicketDetail() {
    }

    // Constructor with all fields
    public TicketDetail(int detailId, int ticketId, String seatNumber, String attendeeName) {
        this.detailId = detailId;
        this.ticketId = ticketId;
        this.seatNumber = seatNumber;
        this.attendeeName = attendeeName;
    }

    // Constructor for new ticket details (without detailId)
    public TicketDetail(int ticketId, String seatNumber, String attendeeName) {
        this.ticketId = ticketId;
        this.seatNumber = seatNumber;
        this.attendeeName = attendeeName;
    }

    // Getters and Setters
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "TicketDetail{" +
                "detailId=" + detailId +
                ", ticketId=" + ticketId +
                ", seatNumber='" + seatNumber + '\'' +
                ", attendeeName='" + attendeeName + '\'' +
                '}';
    }
} 