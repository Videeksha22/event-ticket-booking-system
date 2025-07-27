-- Create database
CREATE DATABASE IF NOT EXISTS event_ticket_booking;
USE event_ticket_booking;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create events table
CREATE TABLE IF NOT EXISTS events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    description TEXT,
    venue VARCHAR(100) NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    ticket_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create tickets table
CREATE TABLE IF NOT EXISTS tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create ticket_types table
CREATE TABLE IF NOT EXISTS ticket_types (
    ticket_type_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_quantity INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

-- Insert default admin user
INSERT INTO users (username, password, full_name, email, phone, role)
VALUES ('admin', 'admin123', 'System Administrator', 'admin@example.com', '1234567890', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username;

-- Insert default regular user
INSERT INTO users (username, password, full_name, email, phone, role)
VALUES ('user', 'user123', 'Regular User', 'user@example.com', '9876543210', 'USER')
ON DUPLICATE KEY UPDATE username=username; 