define(['../accUtils', 'knockout'], function (accUtils, ko) {
  function transactionsViewModel() {
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
    // State
    self.busy = ko.observable(false);
    self.transactions = ko.observableArray([]);

    // List transactions (GET)
    self.listTransaction = () => {
      self.busy(true);
      fetch('http://localhost:4569/transactions', { method: 'GET' })
        .then(function (res) {
          if (!res.ok) throw new Error('HTTP ' + res.status);
          return res.json();
        })
        .then(function (data) {
          // Adjust if backend returns a different shape
          self.transactions(data.body || data || []);
        })
        .catch(function (error) {
          alert('Failed to list transactions: ' + error.message);
        })
        .finally(function () {
          self.busy(false);
        });
    };

    self.connected = function() {
      self.loadPortfolio();
       if (!checkAuth()) {
    // If not logged in or not allowed, redirect to login
    sessionStorage.setItem('loginMessage', 'Please login to continue');
    window.router.go({path:'login'});
    return;
  }

    };

    self.disconnected = () => {};
    self.transitionCompleted = () => {};
  }
  return transactionsViewModel;
});