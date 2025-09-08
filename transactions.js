define(['../accUtils', 'knockout'], function (accUtils, ko) {
    function transactionsViewModel() {
        var self = this;

        function checkAuth() {
            const userStr = sessionStorage.getItem("user");
            if (!userStr) return false;
            try {
                const user = JSON.parse(userStr);
                // Allow if userType is User or Admin for broader access.
                return user && (user.userType === "user" || user.userType === "admin");
            } catch (e) {
                return false;
            }
        }

        // State & Observables
        self.busy = ko.observable(false);
        self.transactions = ko.observableArray([]);

        // Form visibility
        self.listFormVisible = ko.observable(true);
        self.deleteFormVisible = ko.observable(false);
        self.updateFormVisible = ko.observable(false);

        // Form data
        self.txnIdToDelete = ko.observable('');
        self.txnIdToUpdate = ko.observable('');
        self.updateForm = {
            custId: ko.observable(),
            stockId: ko.observable(),
            txnPrice: ko.observable(),
            txnType: ko.observable(''),
            qty: ko.observable(),
            txnDate: ko.observable('')
        };

        // Helper to manage form visibility
        function showForm(formName) {
            self.listFormVisible(formName === 'list');
            self.deleteFormVisible(formName === 'delete');
            self.updateFormVisible(formName === 'update');
        }

        // Event handlers for buttons
        self.showListForm = () => showForm('list');
        self.showDeleteForm = () => showForm('delete');
        self.showUpdateForm = () => showForm('update');

        // API call functions
        self.listTransaction = () => {
            self.busy(true);
            fetch('http://localhost:4569/transactions', { method: 'GET' })
                .then(function (res) {
                    if (!res.ok) throw new Error('HTTP ' + res.status);
                    return res.json();
                })
                .then(function (data) {
                    self.transactions(data || []);
                })
                .catch(function (error) {
                    alert('Failed to list transactions: ' + error.message);
                })
                .finally(function () {
                    self.busy(false);
                });
        };

        self.submitDelete = () => {
            const id = self.txnIdToDelete();
            if (!id) {
                alert('Please enter a Transaction ID to delete.');
                return;
            }
            if (!confirm(`Are you sure you want to delete transaction with ID: ${id}?`)) return;

            self.busy(true);
            fetch(`http://localhost:4569/transactions/${id}`, { method: 'DELETE' })
                .then(function (res) {
                    if (!res.ok) {
                        if (res.status === 404) throw new Error('Transaction not found');
                        throw new Error('HTTP ' + res.status);
                    }
                    alert('Transaction deleted successfully!');
                    self.txnIdToDelete('');
                    self.listTransaction();
                })
                .catch(function (err) {
                    alert('Failed to delete: ' + err.message);
                })
                .finally(function () {
                    self.busy(false);
                });
        };

        self.submitUpdate = () => {
            const id = self.txnIdToUpdate();
            if (!id) {
                alert('Please enter a Transaction ID to update.');
                return;
            }
            const payload = {};
            if (self.updateForm.custId()) payload.custId = Number(self.updateForm.custId());
            if (self.updateForm.stockId()) payload.stockId = Number(self.updateForm.stockId());
            if (self.updateForm.txnPrice()) payload.txnPrice = Number(self.updateForm.txnPrice());
            if (self.updateForm.txnType()) payload.txnType = self.updateForm.txnType();
            if (self.updateForm.qty()) payload.qty = Number(self.updateForm.qty());
            if (self.updateForm.txnDate()) payload.txnDate = self.updateForm.txnDate();

            if (Object.keys(payload).length === 0) {
                alert('Provide at least one field to update.');
                return;
            }

            self.busy(true);
            fetch(`http://localhost:4569/transactions/${id}`, {
                    method: 'PATCH',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                })
                .then(function (res) {
                    if (!res.ok) {
                        if (res.status === 404) throw new Error('Transaction not found');
                        throw new Error('HTTP ' + res.status);
                    }
                    alert('Transaction updated successfully!');
                    self.txnIdToUpdate('');
                    // Clear form
                    self.updateForm.custId(null); self.updateForm.stockId(null);
                    self.updateForm.txnPrice(null); self.updateForm.txnType('');
                    self.updateForm.qty(null); self.updateForm.txnDate('');
                    self.listTransaction();
                })
                .catch(function (err) {
                    alert('Failed to update: ' + err.message);
                })
                .finally(function () {
                    self.busy(false);
                });
        };

        self.connected = function() {
            if (!checkAuth()) {
                sessionStorage.setItem('loginMessage', 'Please login to continue');
                window.router.go({path:'login'});
                return;
            }
            // CRITICAL FIX: Load transactions on page entry
            self.listTransaction();
        };

        self.disconnected = () => {};
        self.transitionCompleted = () => {};
    }
    return transactionsViewModel;
});
