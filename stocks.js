define(['../accUtils', 'knockout'], function(accUtils, ko) {
    function StocksViewModel() {
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

        self.busy = ko.observable(false);
        self.stocks = ko.observableArray([]);

        self.addFormVisible = ko.observable(false);
        self.deleteFormVisible = ko.observable(false);
        self.updateFormVisible = ko.observable(false);
        self.listFormVisible = ko.observable(false);

        function showForm(form) {
            self.addFormVisible(form === 'add');
            self.deleteFormVisible(form === 'delete');
            self.updateFormVisible(form === 'update');
            self.listFormVisible(form === 'list');
        }

        self.newStock = {
            stockId: ko.observable(),
            stockName: ko.observable(''),
            stockPrice: ko.observable(),
            stockVolume: ko.observable(),
            listingPrice: ko.observable(),
            listedExchange: ko.observable('')
        };

        self.stockIdToDelete = ko.observable('');

        self.stockIdToUpdate = ko.observable();
        self.form = {
            stockName: ko.observable(''),
            stockPrice: ko.observable(),
            stockVolume: ko.observable(),
            listingPrice: ko.observable(),
            listedExchange: ko.observable('')
        };

        self.showAddForm = () => showForm('add');
        self.showDeleteForm = () => showForm('delete');
        self.showUpdateForm = () => showForm('update');
        self.showListForm = () => showForm('list');

        self.submitAdd = function() {
            if (!self.newStock.stockId() || !self.newStock.stockName()) {
                alert('Please enter at least ID and Stock Name.');
                return;
            }

            var payload = {
                stockId: Number(self.newStock.stockId()),
                stockName: self.newStock.stockName(),
                stockPrice: self.newStock.stockPrice() != null ? Number(self.newStock.stockPrice()) : null,
                stockVolume: self.newStock.stockVolume() != null ? Number(self.newStock.stockVolume()) : null,
                listingPrice: self.newStock.listingPrice() != null ? Number(self.newStock.listingPrice()) : null,
                listedExchange: self.newStock.listedExchange()
            };

            self.busy(true);
            fetch('http://localhost:4569/stocks', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                })
                .then(function(res) {
                    if (!res.ok) throw new Error('HTTP ' + res.status);
                    return res.text();
                })
                .then(function() {
                    alert('Stock added successfully');
                    self.newStock.stockId(null);
                    self.newStock.stockName('');
                    self.newStock.stockPrice(null);
                    self.newStock.stockVolume(null);
                    self.newStock.listingPrice(null);
                    self.newStock.listedExchange('');
                    self.submitList();
                })
                .catch(function(err) {
                    alert('Failed to add stock: ' + err.message);
                })
                .finally(function() {
                    self.busy(false);
                });
        };

        self.submitList = () => {
            self.busy(true);
            fetch('http://localhost:4569/stocks', {
                    method: 'GET'
                })
                .then(function(res) {
                    if (!res.ok) throw new Error('HTTP ' + res.status);
                    return res.json();
                })
                .then(function(data) {
                    // CRITICAL FIX: More robust data handling
                    let stocksArray = data;
                    if (data && data.body) {
                        stocksArray = data.body;
                    }
                    if (Array.isArray(stocksArray)) {
                        self.stocks(stocksArray);
                    } else {
                        console.error('API response is not an array:', data);
                        self.stocks([]);
                    }
                })
                .catch(function(error) {
                    alert('Failed to list stocks: ' + error.message);
                })
                .finally(function() {
                    self.busy(false);
                });
        };

        self.submitDelete = function() {
            var id = self.stockIdToDelete();
            if (!id) {
                alert('Please enter a Stock ID.');
                return;
            }
            id = Number(id);
            if (Number.isNaN(id)) {
                alert('Stock ID must be a number.');
                return;
            }
            if (!confirm('Delete stock with ID ' + id + '?')) return;

            self.busy(true);
            fetch('http://localhost:4569/stocks/' + id, {
                    method: 'DELETE'
                })
                .then(function(res) {
                    if (!res.ok) {
                        if (res.status === 404) throw new Error('Stock not found');
                        throw new Error('HTTP ' + res.status);
                    }
                    return res.text();
                })
                .then(function() {
                    self.stocks.remove(function(s) {
                        return Number(s.stockId) === id;
                    });
                    self.stockIdToDelete(null);
                    alert('Stock deleted successfully.');
                    self.submitList();
                })
                .catch(function(err) {
                    alert('Failed to delete: ' + err.message);
                })
                .finally(function() {
                    self.busy(false);
                });
        };

        self.submitUpdate = function() {
            var id = self.stockIdToUpdate();
            if (!id) {
                alert('Please enter a Stock ID to update.');
                return;
            }
            var payload = {};
            if (self.form.stockName()) payload.stockName = self.form.stockName();
            if (self.form.stockPrice() != null) payload.stockPrice = Number(self.form.stockPrice());
            if (self.form.stockVolume() != null) payload.stockVolume = Number(self.form.stockVolume());
            if (self.form.listingPrice() != null) payload.listingPrice = Number(self.form.listingPrice());
            if (self.form.listedExchange()) payload.listedExchange = self.form.listedExchange();
            if (Object.keys(payload).length === 0) {
                alert('Provide at least one field to update.');
                return;
            }
            self.busy(true);
            fetch('http://localhost:4569/stocks/' + id, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                })
                .then(function(res) {
                    if (!res.ok) {
                        if (res.status === 404) throw new Error('Stock not found');
                        throw new Error('HTTP ' + res.status);
                    }
                    return res.text();
                })
                .then(function() {
                    alert('Stock updated successfully');
                    self.stockIdToUpdate(null);
                    self.form.stockName('');
                    self.form.stockPrice(null);
                    self.form.stockVolume(null);
                    self.form.listingPrice(null);
                    self.form.listedExchange('');
                    self.submitList();
                })
                .catch(function(err) {
                    alert('Failed to update stock: ' + err.message);
                })
                .finally(function() {
                    self.busy(false);
                });
        };

        self.connected = () => {
            if (!checkAuth()) {
                sessionStorage.setItem('loginMessage', 'Please login to continue');
                window.router.go({
                    path: 'login'
                });
                return;
            }
            // CRITICAL FIX: Automatically load stocks when the page connects
            self.submitList();
            accUtils.announce('Stocks page loaded.');
            document.title = "Stocks";
        };

        self.disconnected = () => {};
        self.transitionCompleted = () => {};
    }
    return StocksViewModel;
});
