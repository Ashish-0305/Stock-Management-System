package com.example.demo;

import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
public class SusersDAO extends JdbcDaoSupport {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init() {
        setDataSource(dataSource);
        System.out.println("SusersDAO initialized with datasource: " + dataSource);
    }

    public JdbcTemplate giveJdbcTemplate() {
        return getJdbcTemplate();
    }

    // Add a new user
    public ResponseEntity<Object> addAUser(Susers newUser) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("INSERT INTO SUSERS (USER_ID, USER_NAME, PSWD, USER_TYPE) VALUES (USER_ID_SEQ.NEXTVAL, ?, ?, ?)",
                newUser.getUserName(), newUser.getPswd(), newUser.getUserType());
        if (rowsAffected > 0)
            return ResponseEntity.status(201).body("User added successfully");
        else
            return ResponseEntity.status(500).body("Failed to add user");
    }

    // Fetch a user by user_id
    public ResponseEntity<Object> fetchUserByUserId(int userId) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM SUSERS WHERE USER_ID = ?";
        List<Susers> result = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            return new Susers(
                rs.getInt("USER_ID"),
                rs.getString("USER_NAME"),
                rs.getString("PSWD"),
                rs.getString("USER_TYPE")
            );
        });
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("User with ID " + userId + " not found");
        }
        return ResponseEntity.status(200).body(result.get(0));
    }

    // Authenticate a user with userType
    @SuppressWarnings("deprecation") // Ideally, address the deprecated method or class
    public ResponseEntity<Object> authenticateUser(String username, String password, String userType) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM SUSERS WHERE USER_NAME = ? AND USER_TYPE = ?";
        List<Susers> users = jdbcTemplate.query(sql, new Object[]{username, userType}, (rs, rowNum) -> {
            return new Susers(
                rs.getInt("USER_ID"),
                rs.getString("USER_NAME"),
                rs.getString("PSWD"),
                rs.getString("USER_TYPE")
            );
        });

        if (users.isEmpty()) {
            return ResponseEntity.status(401).body("Authentication failed: User not found or invalid user type");
        }

        Susers user = users.get(0);
        // Secure password verification should be implemented here (e.g., bcrypt comparison)
        // For demonstration, a simple string comparison is used (NOT SECURE)
        if (user.getPswd().equals(password)) {
            return ResponseEntity.status(200).body(user);
        } else {
            return ResponseEntity.status(401).body("Authentication failed: Invalid password");
        }
    }
}