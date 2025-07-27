-- Database Schema for Event Ticket Booking System

-- Drop database if exists
DROP DATABASE IF EXISTS event_ticket_booking;

-- Create database
CREATE DATABASE event_ticket_booking;

-- Use the database
USE event_ticket_booking;

-- Create Users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Events table
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    description TEXT,
    venue VARCHAR(100) NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    ticket_price DECIMAL(10, 2) NOT NULL,
    event_status ENUM('UPCOMING', 'ONGOING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'UPCOMING',
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- Create Tickets table
CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create TicketDetails table
CREATE TABLE ticket_details (
    detail_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    seat_number VARCHAR(20) NOT NULL,
    attendee_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
);

-- Create Payments table
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    status ENUM('SUCCESS', 'FAILED', 'REFUNDED') NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
);

-- Create ticket_types table
CREATE TABLE IF NOT EXISTS ticket_types (
    ticket_type_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_quantity INT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);

-- Insert default admin user
INSERT INTO users (username, password, full_name, email, phone, role)
VALUES ('admin', 'admin123', 'System Administrator', 'admin@ticketbooking.com', '1234567890', 'ADMIN');

-- Insert default user
INSERT INTO users (username, password, full_name, email, phone, role)
VALUES ('user', 'user123', 'Default User', 'user@ticketbooking.com', '0987654321', 'USER');

-- Insert sample events
INSERT INTO events (event_name, description, venue, event_date, event_time, total_seats, available_seats, ticket_price, created_by)
VALUES 
('Summer Music Festival', 'Annual music festival featuring top artists', 'Central Park', '2024-07-15', '18:00:00', 500, 500, 50.00, 1),
('Tech Conference 2024', 'Conference for software developers and tech enthusiasts', 'Convention Center', '2024-06-20', '09:00:00', 300, 300, 75.00, 1),
('Comedy Night', 'Stand-up comedy show featuring local comedians', 'Laugh Factory', '2024-05-30', '20:00:00', 150, 150, 25.00, 1);

-- Add some sample ticket types
INSERT INTO ticket_types (event_id, category, price, available_quantity, is_active) VALUES
(1, 'VIP', 199.99, 50, true),
(1, 'Regular', 99.99, 200, true),
(2, 'Premium', 149.99, 100, true),
(2, 'Standard', 79.99, 300, true); 