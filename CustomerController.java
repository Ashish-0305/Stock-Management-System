package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    public CustomerController() {
        System.out.println("CustomerController initialized");
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public ResponseEntity<Object> addACustomer(@RequestBody Customer newCustomer) {
        System.out.println("Adding a new customer called from the controller");
        return customerService.addACustomer(newCustomer);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ResponseEntity<Object> fetchAllCustomers() {
        System.out.println("Fetching all customers called from the controller");
        return customerService.fetchAllCustomers();
    }

    @RequestMapping(value = "/customers/{custId}", method = RequestMethod.GET)
    public ResponseEntity<Object> fetchCustomerById(@PathVariable("custId") int custId) {
        System.out.println("Fetching customer with ID: " + custId + " called from the controller");
        return customerService.fetchCustomerById(custId);
    }

    @RequestMapping(value = "/customers/{custId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCustomerById(@PathVariable("custId") int custId, @RequestBody Customer updatedCustomer) {
        System.out.println("Updating customer with ID: " + custId + " called from the controller");
        return customerService.updateCustomerById(custId, updatedCustomer);
    }

    @RequestMapping(value = "/customers/{custId}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> patchCustomerById(@PathVariable("custId") int custId, @RequestBody Customer partialCustomer) {
        System.out.println("Partially updating customer with ID: " + custId + " called from the controller");
        return customerService.patchCustomerById(custId, partialCustomer);
    }

    @RequestMapping(value = "/customers/{custId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteACustomerById(@PathVariable("custId") int custId) {
        System.out.println("Deleting a customer by ID called from the controller");
        return customerService.deleteACustomerById(custId);
    }
}