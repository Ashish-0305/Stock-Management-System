package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
public class CustomerDAO extends JdbcDaoSupport {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init() {
        setDataSource(dataSource);
        System.out.println("CustomerDAO initialized with datasource: " + dataSource);
    }

    public JdbcTemplate giveJdbcTemplate() {
        return getJdbcTemplate();
    }

    public ResponseEntity<Object> addACustomer(Customer newCustomer) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("INSERT INTO CUSTOMERSS (CUST_ID, FIRST_NAME, LAST_NAME, PHONE_NUMBER, CITY, EMAIL_ID) VALUES (?, ?, ?, ?, ?, ?)",
                newCustomer.getCustId(), newCustomer.getFirstName(), newCustomer.getLastName(), 
                newCustomer.getPhoneNumber(), newCustomer.getCity(), newCustomer.getEmailId());
        if (rowsAffected > 0)
            return ResponseEntity.status(201).body("Customer added successfully");
        else
            return ResponseEntity.status(500).body("Failed to add customer");
    }

    public ResponseEntity<ArrayList<Customer>> fetchAllCustomers() {
        ArrayList<Customer> allFetchedCustomers = new ArrayList<>();
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM CUSTOMERSS";
        List<Map<String, Object>> allCustomers = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> cust : allCustomers) {
            try {
                Customer c = new Customer();

                Object custIdObj = cust.get("CUST_ID");
                if (custIdObj != null) {
                    c.setCustId(Integer.valueOf(custIdObj.toString()));
                } else {
                    System.out.println("CUST_ID is null for a row");
                    continue; // Skip this row or handle as needed
                }

                Object firstNameObj = cust.get("FIRST_NAME");
                c.setFirstName(firstNameObj != null ? firstNameObj.toString() : "");

                Object lastNameObj = cust.get("LAST_NAME");
                c.setLastName(lastNameObj != null ? lastNameObj.toString() : "");

                Object phoneNumberObj = cust.get("PHONE_NUMBER");
                c.setPhoneNumber(phoneNumberObj != null ? Long.valueOf(phoneNumberObj.toString()) : 0L);

                Object cityObj = cust.get("CITY");
                c.setCity(cityObj != null ? cityObj.toString() : "");

                Object emailIdObj = cust.get("EMAIL_ID");
                c.setEmailId(emailIdObj != null ? emailIdObj.toString() : "");

                allFetchedCustomers.add(c);
            } catch (Exception e) {
                System.err.println("Error processing row: " + cust);
                e.printStackTrace();
                continue; // Skip problematic rows
            }
        }
        return ResponseEntity.status(200).body(allFetchedCustomers);
    }

    public ResponseEntity<Object> fetchCustomerById(int custId) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM CUSTOMERSS WHERE CUST_ID = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, custId);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("Customer with ID " + custId + " not found");
        }
        Map<String, Object> cust = result.get(0);
        try {
            Customer c = new Customer();

            Object custIdObj = cust.get("CUST_ID");
            if (custIdObj != null) {
                c.setCustId(Integer.valueOf(custIdObj.toString()));
            }

            Object firstNameObj = cust.get("FIRST_NAME");
            c.setFirstName(firstNameObj != null ? firstNameObj.toString() : "");

            Object lastNameObj = cust.get("LAST_NAME");
            c.setLastName(lastNameObj != null ? lastNameObj.toString() : "");

            Object phoneNumberObj = cust.get("PHONE_NUMBER");
            c.setPhoneNumber(phoneNumberObj != null ? Long.valueOf(phoneNumberObj.toString()) : 0L);

            Object cityObj = cust.get("CITY");
            c.setCity(cityObj != null ? cityObj.toString() : "");

            Object emailIdObj = cust.get("EMAIL_ID");
            c.setEmailId(emailIdObj != null ? emailIdObj.toString() : "");

            return ResponseEntity.status(200).body(c);
        } catch (Exception e) {
            System.err.println("Error processing customer with ID " + custId + ": " + cust);
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing customer data for ID " + custId);
        }
    }

    public ResponseEntity<Object> updateCustomerById(int custId, Customer updatedCustomer) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "UPDATE CUSTOMERSS SET FIRST_NAME = ?, LAST_NAME = ?, PHONE_NUMBER = ?, CITY = ?, EMAIL_ID = ? WHERE CUST_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                updatedCustomer.getFirstName(), updatedCustomer.getLastName(), updatedCustomer.getPhoneNumber(),
                updatedCustomer.getCity(), updatedCustomer.getEmailId(), custId);
        if (rowsAffected > 0) {
            return ResponseEntity.status(200).body("Customer updated successfully");
        } else {
            return ResponseEntity.status(404).body("Customer with ID " + custId + " not found");
        }
    }

    public ResponseEntity<Object> patchCustomerById(int custId, Customer partialCustomer) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        StringBuilder sql = new StringBuilder("UPDATE CUSTOMERSS SET ");
        List<Object> params = new ArrayList<>();
        boolean hasUpdates = false;

        if (partialCustomer.getFirstName() != null) {
            sql.append("FIRST_NAME = ?");
            params.add(partialCustomer.getFirstName());
            hasUpdates = true;
        }
        if (partialCustomer.getLastName() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("LAST_NAME = ?");
            params.add(partialCustomer.getLastName());
            hasUpdates = true;
        }
        if (partialCustomer.getPhoneNumber() != 0L) { // Check if phone number is set (assuming 0 means not set)
            if (hasUpdates) sql.append(", ");
            sql.append("PHONE_NUMBER = ?");
            params.add(partialCustomer.getPhoneNumber());
            hasUpdates = true;
        }
        if (partialCustomer.getCity() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("CITY = ?");
            params.add(partialCustomer.getCity());
            hasUpdates = true;
        }
        if (partialCustomer.getEmailId() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("EMAIL_ID = ?");
            params.add(partialCustomer.getEmailId());
            hasUpdates = true;
        }

        if (!hasUpdates) {
            return ResponseEntity.status(400).body("No fields provided for update");
        }
        sql.append(" WHERE CUST_ID = ?");
        params.add(custId);
        int rowsAffected = jdbcTemplate.update(sql.toString(), params.toArray());
        if (rowsAffected > 0) {
            return ResponseEntity.status(200).body("Customer partially updated successfully");
        } else {
            return ResponseEntity.status(404).body("Customer with ID " + custId + " not found");
        }
    }

    public ResponseEntity<Object> deleteACustomerById(int custId) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("DELETE FROM CUSTOMERSS WHERE CUST_ID = ?", custId);
        if (rowsAffected > 0)
            return ResponseEntity.status(200).body("Customer deleted successfully");
        else
            return ResponseEntity.status(404).body("Customer not found");
    }
}