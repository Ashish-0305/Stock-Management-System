package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    // CustomerService depends on CustomerDAO
    @Autowired
    CustomerDAO customerDAO;

    public ResponseEntity<Object> addACustomer(Customer newCustomer) {
        return customerDAO.addACustomer(newCustomer);
    }

    public ResponseEntity<Object> fetchAllCustomers() {
        return ResponseEntity.status(200).body(customerDAO.fetchAllCustomers());
    }

    public ResponseEntity<Object> fetchCustomerById(int custId) {
        return customerDAO.fetchCustomerById(custId);
    }

    public ResponseEntity<Object> updateCustomerById(int custId, Customer updatedCustomer) {
        return customerDAO.updateCustomerById(custId, updatedCustomer);
    }

    public ResponseEntity<Object> patchCustomerById(int custId, Customer partialCustomer) {
        return customerDAO.patchCustomerById(custId, partialCustomer);
    }

    public ResponseEntity<Object> deleteACustomerById(int custId) {
        return customerDAO.deleteACustomerById(custId);
    }
}