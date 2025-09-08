package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SusersService {
    @Autowired
    private SusersDAO susersDAO;

    public SusersService() {
        System.out.println("SusersService initialized");
    }

    // Add a new user
    public ResponseEntity<Object> addAUser(Susers newUser) {
        System.out.println("Adding a new user called from the service");
        // Additional business logic or validation can be added here if needed
        return susersDAO.addAUser(newUser);
    }

    // Fetch user details by user_id
    public ResponseEntity<Object> fetchUserByUserId(int userId) {
        System.out.println("Fetching user with ID: " + userId + " called from the service");
        // Additional business logic or validation can be added here if needed
        return susersDAO.fetchUserByUserId(userId);
    }

    // Authenticate a user with userType
    public ResponseEntity<Object> authenticateUser(String username, String password, String userType) {
        System.out.println("Authenticating user called from the service");
        // Additional validation or logging can be added here if needed
        return susersDAO.authenticateUser(username, password, userType);
    }
}