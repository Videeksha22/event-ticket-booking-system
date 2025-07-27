package com.ticketbooking.dao;

import com.ticketbooking.model.Payment;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    private TicketDAO ticketDAO = new TicketDAO();
    
    /**
     * Get all payments from the database
     * 
     * @return List of all payments
     */
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Payment payment = mapResultSetToPayment(rs);
                payments.add(payment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving payments: " + e.getMessage());
        }
        
        return payments;
    }
    
    /**
     * Get payment by ID
     * 
     * @param paymentId The payment ID to search for
     * @return Payment object if found, null otherwise
     */
    public Payment getPaymentById(int paymentId) {
        String query = "SELECT * FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, paymentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Payment payment = mapResultSetToPayment(rs);
                    
                    // Load associated ticket
                    Ticket ticket = ticketDAO.getTicketById(payment.getTicketId());
                    payment.setTicket(ticket);
                    
                    return payment;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving payment by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get payments by ticket ID
     * 
     * @param ticketId The ticket ID to search for
     * @return List of payments for the specified ticket
     */
    public List<Payment> getPaymentsByTicketId(int ticketId) {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, ticketId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = mapResultSetToPayment(rs);
                    payments.add(payment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving payments by ticket ID: " + e.getMessage());
        }
        
        return payments;
    }
    
    /**
     * Create a new payment in the database
     * 
     * @param payment The payment object to create
     * @return The created payment with ID set, or null if creation failed
     */
    public Payment createPayment(Payment payment) {
        String query = "INSERT INTO payments (ticket_id, amount, payment_method, transaction_id, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getTicketId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getTransactionId());
            pstmt.setString(5, payment.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setPaymentId(generatedKeys.getInt(1));
                        
                        // Update ticket payment status if payment is successful
                        if ("SUCCESS".equals(payment.getStatus())) {
                            Ticket ticket = ticketDAO.getTicketById(payment.getTicketId());
                            if (ticket != null) {
                                ticket.setPaymentStatus("COMPLETED");
                                ticketDAO.updateTicket(ticket);
                            }
                        }
                        
                        return payment;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update an existing payment
     * 
     * @param payment The payment object with updated values
     * @return true if update successful, false otherwise
     */
    public boolean updatePayment(Payment payment) {
        String query = "UPDATE payments SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, payment.getStatus());
            pstmt.setInt(2, payment.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Update ticket payment status if payment status is changed
                Ticket ticket = ticketDAO.getTicketById(payment.getTicketId());
                if (ticket != null) {
                    if ("SUCCESS".equals(payment.getStatus())) {
                        ticket.setPaymentStatus("COMPLETED");
                    } else if ("REFUNDED".equals(payment.getStatus())) {
                        ticket.setPaymentStatus("REFUNDED");
                    } else if ("FAILED".equals(payment.getStatus())) {
                        ticket.setPaymentStatus("CANCELLED");
                    }
                    ticketDAO.updateTicket(ticket);
                }
                
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Process a refund for a payment
     * 
     * @param paymentId The ID of the payment to refund
     * @return true if refund successful, false otherwise
     */
    public boolean refundPayment(int paymentId) {
        Payment payment = getPaymentById(paymentId);
        if (payment == null || !"SUCCESS".equals(payment.getStatus())) {
            return false;
        }
        
        payment.setStatus("REFUNDED");
        return updatePayment(payment);
    }
    
    /**
     * Helper method to map ResultSet to Payment object
     * 
     * @param rs The ResultSet containing payment data
     * @return Mapped Payment object
     * @throws SQLException if a database access error occurs
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setTicketId(rs.getInt("ticket_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setStatus(rs.getString("status"));
        return payment;
    }
} 