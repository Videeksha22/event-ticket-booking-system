package com.ticketbooking.model;

public class TicketType {
    private int ticketTypeId;
    private int eventId;
    private String category;
    private double price;
    private int availableQuantity;
    private boolean isActive;

    public TicketType() {}

    public TicketType(int ticketTypeId, int eventId, String category, double price, int availableQuantity, boolean isActive) {
        this.ticketTypeId = ticketTypeId;
        this.eventId = eventId;
        this.category = category;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getTicketTypeId() { return ticketTypeId; }
    public void setTicketTypeId(int ticketTypeId) { this.ticketTypeId = ticketTypeId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "TicketType{" +
                "ticketTypeId=" + ticketTypeId +
                ", eventId=" + eventId +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", availableQuantity=" + availableQuantity +
                ", isActive=" + isActive +
                '}';
    }
} 