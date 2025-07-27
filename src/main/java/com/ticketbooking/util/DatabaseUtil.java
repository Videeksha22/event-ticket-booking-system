package com.ticketbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseUtil {
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static final String PROPERTIES_FILE = "src/main/resources/db.properties";

    private DatabaseUtil() {
        // Private constructor to prevent instantiation
    }

    static {
        try {
            // First attempt to load from properties file
            try {
                loadProperties();
            } catch (IOException e) {
                System.err.println("Error loading properties, using defaults: " + e.getMessage());
                // Default values if property file not found
                dbUrl = "jdbc:mysql://localhost:3306/event_ticket_booking";
                dbUser = "root";
                dbPassword = "";
            }
            
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    private static void loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            props.load(input);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
        }
    }

    /**
     * Get a database connection - creates a new connection each time
     * 
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        // Always create a new connection
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Close a database connection safely
     * 
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Update database credentials manually (useful for the first run or GUI configuration)
     * 
     * @param url      JDBC URL for the database
     * @param user     Database username
     * @param password Database password
     */
    public static void setCredentials(String url, String user, String password) {
        dbUrl = url;
        dbUser = user;
        dbPassword = password;
    }
} 