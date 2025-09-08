define(['../accUtils', 'knockout'],
    function(accUtils, ko) {
        function CustomerViewModel() {
            var self = this;

            function checkAuth() {
                const userStr = sessionStorage.getItem("user");
                if (!userStr) return false;
                try {
                    const user = JSON.parse(userStr);
                    return user && (user.userType === "admin");
                } catch (e) {
                    return false;
                }
            }

            self.addFormVisible = ko.observable(false);
            self.listFormVisible = ko.observable(false);
            self.deleteFormVisible = ko.observable(false);
            self.updateFormVisible = ko.observable(false);

            self.customers = ko.observableArray([]);
            self.newCustomer = {
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

            function showForm(formName) {
                self.addFormVisible(formName === 'add');
                self.listFormVisible(formName === 'list');
                self.deleteFormVisible(formName === 'delete');
                self.updateFormVisible(formName === 'update');
            }

            self.showAddForm = () => showForm('add');
            self.showListForm = () => showForm('list');
            self.showDeleteForm = () => showForm('delete');
            self.showUpdateForm = () => showForm('update');

            self.submitAddCustomer = () => {
                const payload = {
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
                        self.newCustomer.firstName('');
                        self.newCustomer.lastName('');
                        self.newCustomer.city('');
                        self.newCustomer.phoneNumber('');
                        self.newCustomer.emailId('');
                        self.submitListCustomer();
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
                        // CRITICAL FIX: More robust data handling
                        let customersArray = data;
                        if (data && data.body) {
                            customersArray = data.body;
                        }
                        if (Array.isArray(customersArray)) {
                            self.customers(customersArray);
                        } else {
                            console.error('API response is not an array:', data);
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
                        self.submitListCustomer();
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
                        self.submitListCustomer();
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
                // CRITICAL FIX: Automatically load customers when the page connects
                self.submitListCustomer();
            };
            self.disconnected = () => {};
            self.transitionCompleted = () => {};
        }
        return CustomerViewModel;
    }
);
