# Event Ticket Booking System

A Java-based application for booking event tickets with a MySQL database backend and Java Swing frontend.

## Features

- User authentication (login/registration)
- Different roles: Admin and User
- Event management (create, update, delete events)
- Ticket booking and management
- Payment processing
- User management (admin only)

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher
- MySQL Connector/J (JDBC driver)

## Setup

### Database Setup

1. Install MySQL Server if not already installed
2. Create a database named `event_ticket_booking`
3. Run the SQL script located at `sql/db_schema.sql` to create tables and insert sample data:

```bash
mysql -u username -p event_ticket_booking < sql/db_schema.sql
```

### Application Setup

1. Clone the repository
2. Update database credentials in `src/main/resources/db.properties` if needed
3. Compile the project:

```bash
javac -d target -cp "lib/*" src/main/java/com/ticketbooking/**/*.java
```

4. Run the application:

```bash
java -cp "target:lib/*" com.ticketbooking.Main
```

## Default Users

The system comes with two default users:

1. Admin:
   - Username: admin
   - Password: admin123

2. Regular User:
   - Username: user
   - Password: user123

## Project Structure

```
event-ticket-booking/
├── sql/                      # SQL scripts
│   └── db_schema.sql         # Database schema and sample data
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── ticketbooking/
│       │           ├── dao/       # Data Access Objects
│       │           ├── model/     # Model classes
│       │           ├── service/   # Business logic
│       │           ├── ui/        # User interface
│       │           ├── util/      # Utility classes
│       │           └── Main.java  # Application entry point
│       └── resources/
│           └── db.properties      # Database configuration
└── README.md                 # This file
```

## License

This project is licensed under the MIT License - see the LICENSE file for details. 