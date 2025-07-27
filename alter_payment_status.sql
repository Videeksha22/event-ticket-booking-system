-- Increase the size of payment_status column in tickets table
USE event_ticket_booking;

-- Alter table to increase the size of the payment_status column
ALTER TABLE tickets MODIFY COLUMN payment_status VARCHAR(50);

-- Verify the change
DESCRIBE tickets; 