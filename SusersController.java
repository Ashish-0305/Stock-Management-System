package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8000/")
public class SusersController {
    @Autowired
    SusersService susersService;

    public SusersController() {
        System.out.println("SusersController initialized");
    }

    // API to add a new user
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Object> addAUser(@RequestBody Susers newUser) {
        System.out.println("Adding a new user called from the controller. Received: " + newUser);
        return susersService.addAUser(newUser);
    }

    // API to fetch user details by user_id
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Object> fetchUserByUserId(@PathVariable("userId") int userId) {
        System.out.println("Fetching user with ID: " + userId + " called from the controller");
        return susersService.fetchUserByUserId(userId);
    }

    // API to authenticate a user (updated to include userType in request body)
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Object> authenticateUser(@RequestBody Susers credentials) {
        System.out.println("Authenticating user called from the controller");
        return susersService.authenticateUser(
            credentials.getUserName(), 
            credentials.getPswd(), 
            credentials.getUserType()
        );
    }
}