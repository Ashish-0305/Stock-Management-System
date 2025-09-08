/**
 * @license
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
/*
 * Your customer ViewModel code goes here
 */
define(['../accUtils','knockout'],
  function(accUtils,ko) {
    function CustomerViewModel() {
      var self = this;

      function checkAuth() {
        const userStr = sessionStorage.getItem("user");
        if (!userStr) return false;
        try {
          const user = JSON.parse(userStr);
          // Allow if userType is User or Admin
          return user && (user.userType === "admin");
        } catch (e) {
          return false;
        }
      }

      // Knockout Observables for Form Visibility
      self.addFormVisible = ko.observable(false);
      self.listFormVisible = ko.observable(false);
      self.deleteFormVisible = ko.observable(false);
      self.updateFormVisible = ko.observable(false);

      // Knockout Observables for Form Data
      self.customers = ko.observableArray([]);
      self.newCustomer = {
        custId: ko.observable(),
        firstName: ko.observable(''),
        lastName: ko.observable(''),
        city: ko.observable(''),
        phoneNumber: ko.observable(''),
        emailId: ko.observable('')
      };
      self.customerToDeleteId = ko.observable('');
      self.customerToUpdateId = ko.observable('');
      self.updateForm = {
        firstName: ko.observable(''),
        lastName: ko.observable(''),
        city: ko.observable(''),
        phoneNumber: ko.observable(''),
        emailId: ko.observable('')
      };

      // Helper function to manage form visibility
      function showForm(formName) {
        self.addFormVisible(formName === 'add');
        self.listFormVisible(formName === 'list');
        self.deleteFormVisible(formName === 'delete');
        self.updateFormVisible(formName === 'update');
      }

      // Event handlers to show forms
      self.showAddForm = () => showForm('add');
      self.showListForm = () => showForm('list');
      self.showDeleteForm = () => showForm('delete');
      self.showUpdateForm = () => showForm('update');

      // API call functions
      self.submitAddCustomer = () => {
        const payload = {
          custId: Number(self.newCustomer.custId()),
          firstName: self.newCustomer.firstName(),
          lastName: self.newCustomer.lastName(),
          city: self.newCustomer.city(),
          phoneNumber: self.newCustomer.phoneNumber(),
          emailId: self.newCustomer.emailId()
        };

        fetch('http://localhost:4569/customers', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
          })
          .then(response => {
            if (!response.ok) throw new Error('Failed to add customer');
            return response.json();
          })
          .then(data => {
            alert('Customer added successfully!');
            self.newCustomer.custId(null);
            self.newCustomer.firstName('');
            self.newCustomer.lastName('');
            self.newCustomer.city('');
            self.newCustomer.phoneNumber('');
            self.newCustomer.emailId('');
            self.submitListCustomer(); // Refresh the list
          })
          .catch(error => {
            alert('Error: ' + error.message);
          });
      };

      self.submitListCustomer = () => {
        fetch('http://localhost:4569/customers', {
            method: 'GET'
          })
          .then(response => {
            if (!response.ok) throw new Error('Failed to list customers');
            return response.json();
          })
          .then(data => {
            console.log(data);
            if (data && data.body) {
              self.customers(data.body); // Correctly updating the observable
            } else {
              self.customers([]);
            }
          })
          .catch(error => {
            alert('Failed to list customers: ' + error);
          });
      };

      self.submitDeleteCustomer = () => {
        const id = self.customerToDeleteId();
        if (!id) {
          alert('Please enter a customer ID to delete.');
          return;
        }

        if (!confirm(`Are you sure you want to delete customer with ID: ${id}?`)) return;

        fetch(`http://localhost:4569/customers/${id}`, {
            method: 'DELETE'
          })
          .then(response => {
            if (!response.ok) throw new Error('Failed to delete customer');
            alert('Customer deleted successfully!');
            self.customerToDeleteId('');
            self.submitListCustomer(); // Refresh the list
          })
          .catch(error => {
            alert('Error: ' + error.message);
          });
      };

      self.submitUpdateCustomer = () => {
        const id = self.customerToUpdateId();
        if (!id) {
          alert('Please enter a customer ID to update.');
          return;
        }

        const payload = {};
        if (self.updateForm.firstName()) payload.firstName = self.updateForm.firstName();
        if (self.updateForm.lastName()) payload.lastName = self.updateForm.lastName();
        if (self.updateForm.city()) payload.city = self.updateForm.city();
        if (self.updateForm.phoneNumber()) payload.phoneNumber = self.updateForm.phoneNumber();
        if (self.updateForm.emailId()) payload.emailId = self.updateForm.emailId();

        if (Object.keys(payload).length === 0) {
          alert('Please provide at least one field to update.');
          return;
        }

        fetch(`http://localhost:4569/customers/${id}`, {
            method: 'PATCH',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
          })
          .then(response => {
            if (!response.ok) throw new Error('Failed to update customer');
            return response.json();
          })
          .then(data => {
            alert('Customer updated successfully!');
            self.customerToUpdateId('');
            self.updateForm.firstName('');
            self.updateForm.lastName('');
            self.updateForm.city('');
            self.updateForm.phoneNumber('');
            self.updateForm.emailId('');
            self.submitListCustomer(); // Refresh the list
          })
          .catch(error => {
            alert('Error: ' + error.message);
          });
      };

      self.connected = function() {
        if (!checkAuth()) {
          sessionStorage.setItem('loginMessage', 'Please login to continue');
          window.router.go({
            path: 'login'
          });
          return;
        }
      };
      self.disconnected = () => {};
      self.transitionCompleted = () => {};
    }
    return CustomerViewModel;
  }
);