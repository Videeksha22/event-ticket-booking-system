package com.ticketbooking.service;

import com.ticketbooking.dao.UserDAO;
import com.ticketbooking.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for handling user-related business logic
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        userDAO = new UserDAO();
    }
    
    /**
     * Authenticate a user
     * 
     * @param username The username
     * @param password The password
     * @return User object if authentication successful, null otherwise
     * @throws SQLException if database error occurs
     */
    public User authenticate(String username, String password) throws SQLException {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        // Try to get user by credentials
        User user = userDAO.getUserByCredentials(username, password);
        
        if (user != null) {
            // Clear sensitive data before returning
            user.setPassword(null);
        }
        
        return user;
    }
    
    /**
     * Register a new user
     * 
     * @param username Username
     * @param password Password
     * @param fullName Full name
     * @param email Email
     * @param phone Phone number
     * @return Created user or null if registration failed
     */
    public User register(String username, String password, String fullName, String email, String phone) {
        // Check if username already exists
        if (userDAO.getUserByUsername(username) != null) {
            return null;
        }
        
        // Create new user with USER role
        User user = new User(username, password, fullName, email, phone, "USER");
        return userDAO.createUser(user);
    }
    
    /**
     * Get a user by ID
     * 
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    /**
     * Get all users
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * Update a user's information
     * 
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }
    
    /**
     * Delete a user
     * 
     * @param userId ID of the user to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
    
    /**
     * Check if a user is an admin
     * 
     * @param user User to check
     * @return true if user is an admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }
} 