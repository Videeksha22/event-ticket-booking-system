package com.ticketbooking.service;

import com.ticketbooking.dao.PaymentDAO;
import com.ticketbooking.model.Payment;
import com.ticketbooking.model.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Service class for handling payment-related business logic
 */
public class PaymentService {
    private PaymentDAO paymentDAO;
    private TicketService ticketService;
    
    public PaymentService() throws SQLException {
        paymentDAO = new PaymentDAO();
        ticketService = new TicketService();
    }
    
    /**
     * Process a payment for a ticket
     * 
     * @param ticketId Ticket ID
     * @param amount Amount to pay
     * @param paymentMethod Payment method
     * @return Created payment or null if payment failed
     */
    public Payment processPayment(int ticketId, double amount, String paymentMethod) {
        // Get the ticket
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            return null;
        }
        
        // Check if the amount matches the ticket price
        if (Math.abs(amount - ticket.getTotalPrice()) > 0.01) {
            return null;
        }
        
        // Generate a transaction ID
        String transactionId = generateTransactionId();
        
        // Create the payment
        Payment payment = new Payment(ticketId, amount, paymentMethod, transactionId, "SUCCESS");
        return paymentDAO.createPayment(payment);
    }
    
    /**
     * Get payment by ID
     * 
     * @param paymentId Payment ID
     * @return Payment object if found, null otherwise
     */
    public Payment getPaymentById(int paymentId) {
        return paymentDAO.getPaymentById(paymentId);
    }
    
    /**
     * Get payments for a ticket
     * 
     * @param ticketId Ticket ID
     * @return List of payments for the ticket
     */
    public List<Payment> getPaymentsByTicketId(int ticketId) {
        return paymentDAO.getPaymentsByTicketId(ticketId);
    }
    
    /**
     * Process a refund for a payment
     * 
     * @param paymentId Payment ID
     * @return true if refund successful, false otherwise
     */
    public boolean refundPayment(int paymentId) {
        return paymentDAO.refundPayment(paymentId);
    }
    
    /**
     * Generate a transaction ID
     * 
     * @return Generated transaction ID
     */
    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 