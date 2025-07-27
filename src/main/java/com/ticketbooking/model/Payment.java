package com.ticketbooking.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int ticketId;
    private double amount;
    private Timestamp paymentDate;
    private String paymentMethod;
    private String transactionId;
    private String status;
    
    // Associated ticket for relationship navigation
    private Ticket ticket;

    // Default constructor
    public Payment() {
    }

    // Constructor with all fields
    public Payment(int paymentId, int ticketId, double amount, Timestamp paymentDate, 
                  String paymentMethod, String transactionId, String status) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
    }

    // Constructor for new payment (without paymentId and paymentDate)
    public Payment(int ticketId, double amount, String paymentMethod, String transactionId, String status) {
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", ticketId=" + ticketId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
} 