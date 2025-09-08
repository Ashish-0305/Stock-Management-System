define(['../accUtils', 'knockout'],
    function(accUtils, ko) {
        function DashboardViewModel() {
            var self = this;

            function checkAuth() {
                const userStr = sessionStorage.getItem("user");
                if (!userStr) return false;
                try {
                    const user = JSON.parse(userStr);
                    return user && (user.userType === "user");
                } catch (e) {
                    return false;
                }
            }

            self.showTable = ko.observable(false);
            self.stocks = ko.observableArray([]);
            self.buyStockVisible = ko.observable(false);
            self.buyStockId = ko.observable('');
            self.buyQty = ko.observable('');
            self.buyMessage = ko.observable('');
            self.buyError = ko.observable('');

            self.listStocks = function() {
                fetch('http://localhost:4569/stocks', {
                        method: 'GET'
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log(data);
                        if (data && data.body) {
                            self.stocks(data.body);
                        } else {
                            self.stocks(data);
                        }
                    })
                    .catch(error => {
                        alert('Failed to list stocks: ' + error.message);
                    });
            };

            self.toggleStocks = function() {
                self.showTable(!self.showTable());
                if (self.showTable()) {
                    self.listStocks();
                }
            };

            self.toggleBuyStock = function() {
                self.buyStockVisible(!self.buyStockVisible());
                self.buyMessage('');
                self.buyError('');
                self.buyStockId('');
                self.buyQty('');
            };

            self.doBuyStock = function() {
                var user = JSON.parse(sessionStorage.getItem("user") || "{}");
                var stockId = self.buyStockId();
                var qty = Number(self.buyQty());

                self.buyMessage('');
                self.buyError('');

                if (!stockId || !qty) {
                    self.buyError("Please enter stock ID and quantity.");
                    return false;
                }

                fetch('http://localhost:4569/stocks/' + stockId)
                    .then(res => {
                        if (!res.ok) {
                            if (res.status === 404) {
                                throw new Error("Stock not found");
                            } else {
                                throw new Error("Error retrieving stock listing price");
                            }
                        }
                        return res.json();
                    })
                    .then(stock => {
                        var txn = {
                            custId: user.userId,
                            stockId: stockId,
                            txnPrice: qty * stock.stockPrice,
                            txnType: "BUY",
                            qty: qty,
                            // CRITICAL FIX: Add txnDate set to today's date
                            txnDate: new Date().toISOString().substring(0, 10)
                        };
                        return fetch('http://localhost:4569/transactions', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(txn)
                        });
                    })
                    .then(res => {
                        if (!res.ok) throw new Error("Buy failed");
                        self.buyMessage("Stock bought successfully.");
                        self.buyError('');
                    })
                    .catch(err => {
                        if (err.message === "Stock not found") {
                            self.buyError("Stock not found.");
                        } else {
                            self.buyError(err.message);
                        }
                        self.buyMessage('');
                    });
                return false;
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

        return DashboardViewModel;
    }
);