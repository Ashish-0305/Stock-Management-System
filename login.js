define(['../accUtils', 'knockout','ojs/ojcore', 'ojs/ojrouter','appController'], function (accUtils, ko, oj, Router,ac) {
  function AuthViewModel() {
    var self = this;
    // State observables for UI
    self.activeTab = ko.observable('signup');
    self.username = ko.observable('');
    self.password = ko.observable('');
    self.usernameError = ko.observable('');
    self.passwordError = ko.observable('');
    self.busy = ko.observable(false);
    // Observables for Sign Up fields
    self.firstName = ko.observable('');
    self.lastName = ko.observable('');
    self.city = ko.observable('');
    self.phone = ko.observable('');
    self.emailId = ko.observable('');
    self.userType = ko.observable(''); // Used for both signup and login user type dropdown
    // Error observables for Sign Up and Login fields
    self.firstNameError = ko.observable('');
    self.lastNameError = ko.observable('');
    self.cityError = ko.observable('');
    self.phoneError = ko.observable('');
    self.emailIdError = ko.observable('');
    self.userTypeError = ko.observable(''); // Used for user type error in both tabs
    // Submit handler for both Login and Sign Up
    self.handleSubmit = function () {
      // Reset errors
      self.firstNameError(''); self.lastNameError('');
      self.cityError(''); self.phoneError(''); self.emailIdError('');
      self.usernameError(''); self.passwordError('');
      self.userTypeError(''); // Reset user type error
      if (self.activeTab() === 'signup') {
        // Validate fields for signup
        if (!self.firstName()) self.firstNameError('First Name is required');
        if (!self.lastName()) self.lastNameError('Last Name is required');
        if (!self.city()) self.cityError('City is required');
        if (!self.phone()) self.phoneError('Phone is required');
        if (!self.emailId()) self.emailIdError('Email ID is required');
        if (!self.username()) self.usernameError('Username is required');
        if (!self.password()) {
          self.passwordError('Password is required');
          alert('Password is required. Please enter a password.');
        } else if (self.password().trim() === '') {
          self.passwordError('Password cannot be empty or just spaces.');
          alert('Password cannot be empty or just spaces.');
        }
        if (!self.userType()) self.userTypeError('User Type is required'); // Validate user type for signup
        // Additional validation for phone number to ensure it's numeric and within range
        if (self.phone() && !/^\d{1,10}$/.test(self.phone())) {
          self.phoneError('Phone number must contain 1 to 10 digits');
        }
        if (self.firstNameError() || self.lastNameError() ||
            self.cityError() || self.phoneError() || self.emailIdError() ||
            self.usernameError() || self.passwordError() || self.userTypeError()) {
          return false;
        }
        // Prepare payload for SUSERS table (username, password, and userType)
        var userPayload = {
          userName: self.username(),
          pwsd: self.password(),
          userType: self.userType() // Added userType to payload
        };
        // Log the user payload for debugging with extra detail for password
        console.log('User Payload to be sent:', JSON.stringify(userPayload));
        console.log('Password value specifically:', self.password() ? 'Password is set (length: ' + self.password().length + ')' : 'Password is empty or undefined');
        // Prepare payload for CUSTOMERS table (only personal details, excluding username and password)
        var customerPayload = {
          firstName: self.firstName(),
          lastName: self.lastName(),
          city: self.city(),
          phoneNumber: self.phone(), // Send as string, backend will handle conversion
          emailId: self.emailId()
        };
        // Log the customer payload for debugging
        console.log('Customer Payload to be sent:', JSON.stringify(customerPayload));
        // Sign Up API calls (sequential: first SUSERS, then CUSTOMERS)
        self.busy(true);
        // Step 1: Add user to SUSERS table (username, password, and userType)
        fetch('http://localhost:4569/users', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(userPayload)
        })
          .then(function (res) {
            if (!res.ok) {
              return res.text().then(text => {
                throw new Error('HTTP ' + res.status + ' - Failed to add user: ' + text + ' | Payload sent: ' + JSON.stringify(userPayload));
              });
            }
            var contentType = res.headers.get('content-type') || '';
            return contentType.includes('application/json') ? res.json() : res.text();
          })
          .then(function (userData) {
            alert('User credentials saved: ' + (typeof userData === 'string' ? userData : (userData.message || 'User created')));
            // Step 2: Add customer details to CUSTOMERS table (only personal details)
            return fetch('http://localhost:4569/customers', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(customerPayload)
            });
          })
          .then(function (res) {
            if (!res.ok) {
              return res.text().then(text => {
                throw new Error('HTTP ' + res.status + ' - Failed to add customer details: ' + text + ' | Payload sent: ' + JSON.stringify(customerPayload));
              });
            }
            var contentType = res.headers.get('content-type') || '';
            return contentType.includes('application/json') ? res.json() : res.text();
          })
          .then(function (customerData) {
            alert('Customer details saved: ' + (typeof customerData === 'string' ? customerData : (customerData.message || 'Customer created')));
            // Switch to login tab and clear form
            self.activeTab('login');
            self.firstName(''); self.lastName('');
            self.city(''); self.phone(''); self.emailId('');
            self.username(''); self.password('');
            self.userType(''); // Clear userType field
          })
          .catch(function (err) {
            alert('Sign Up failed: ' + err.message);
            self.usernameError('Error during signup. Please try again.');
          })
          .finally(function () {
            self.busy(false);
          });
      } else {
        // Login validation
        if (!self.username() || !self.password() || !self.userType()) {
          alert('Enter all credentials');
          if (!self.username()) self.usernameError('Username is required');
          if (!self.password()) {
            self.passwordError('Password is required');
            alert('Password is required. Please enter a password.');
          } else if (self.password().trim() === '') {
            self.passwordError('Password cannot be empty or just spaces.');
            alert('Password cannot be empty or just spaces.');
          }
          if (!self.userType()) self.userTypeError('User Type is required'); // Validate userType for login
          return false;
        }
        // Prepare payload for login/authentication
        var loginPayload = {
          userName: self.username(),
          pwsd: self.password(),
          userType: self.userType() // Include userType in login payload
        };
        // Log the login payload for debugging with extra detail for password
        console.log('Login Payload to be sent:', JSON.stringify(loginPayload));
        console.log('Password value specifically for login:', self.password() ? 'Password is set (length: ' + self.password().length + ')' : 'Password is empty or undefined');
        // Login API call to authenticate endpoint
        self.busy(true);
        fetch('http://localhost:4569/authenticate', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(loginPayload)
        })
          .then(function (res) {
            if (!res.ok) {
              return res.text().then(text => {
                throw new Error('HTTP ' + res.status + ' - Failed to authenticate: ' + text);
              });
            }
            var contentType = res.headers.get('content-type') || '';
            return contentType.includes('application/json') ? res.json() : res.text();
          })
          .then(function (data) {
            alert('Login successful: ' + (typeof data === 'string' ? data : (data.message || 'Authenticated')));
            // Clear form
            // Note: 'user' variable is not defined in this scope; assuming it should store the logged-in user data
            sessionStorage.setItem("user", JSON.stringify(data));
            // Update global authentication state (assuming app is accessible globally)
            if (typeof app !== 'undefined' && app.setAuthenticated) {
              app.setAuthenticated(true); // Update the observable in ControllerViewModel
            }
            require(['appController'], function(appController) {
            appController.setNavForRole(data.userType); // "Admin" or "User"
            appController.userLogin(data.userName);
          });
                  ac.userLogin(data.userName);
                  // Route based on role
                  if (data.userType === "admin") {
                    window.router.go({path:'stocks'})
                  } else {
                    window.router.go({path:'customers'});
                  }
            // Optionally redirect to a default page like customers or stocks after login
            // accUtils.router.go('customers');
          })
          .catch(function (err) {
            alert('Login failed: ' + err.message);
            self.usernameError('Invalid username, password, or user type.');
          })
          .finally(function () {
            self.busy(false);
          });
      }
      return false; // Prevent default form submission
    };
    // Forgot Password handler (placeholder)
    self.forgotPassword = function () {
      alert('Forgot Password functionality is not implemented yet.');
    };
    // Lifecycle methods for OJET
    self.connected = function () {
      accUtils.announce('Authentication page loaded.', 'assertive');
    };
  }
  return AuthViewModel;
});