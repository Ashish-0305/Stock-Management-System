define(['../accUtils', 'knockout'],
    function(accUtils, ko) {
        function PortfolioViewModel() {
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

            self.portfolio = ko.observableArray([]);
            self.showSellForm = ko.observable(false);
            self.sellStockId = ko.observable('');
            self.sellStockName = ko.observable('');
            self.sellMaxQty = ko.observable(0);
            self.sellQty = ko.observable('');
            self.sellMsg = ko.observable('');
            self.sellError = ko.observable('');
            self.txnHistoryVisible = ko.observable(false);
            self.transactionHistory = ko.observableArray([]);

            self.connected = function() {
                if (!checkAuth()) {
                    sessionStorage.setItem('loginMessage', 'Please login to continue');
                    window.router.go({
                        path: 'login'
                    });
                    return;
                }
                self.loadPortfolio();
            };

            self.loadPortfolio = function() {
                self.sellMsg('');
                self.sellError('');
                var user = JSON.parse(sessionStorage.getItem("user") || "{}");
                fetch('http://localhost:4569/api/portfolio/' + user.userId)
                    .then(res => {
                        if (!res.ok) throw new Error("Failed to fetch portfolio");
                        return res.json();
                    })
                    .then(data => {
                        self.portfolio(data || []);
                    })
                    .catch(err => {
                        console.error("Error loading portfolio:", err);
                        self.sellError("Failed to load portfolio. Please try again.");
                    });
            };

            self.sellStock = function(stock) {
                self.sellStockId(stock.stockId);
                self.sellStockName(stock.stockName);
                self.sellMaxQty(stock.netHolding);
                self.sellQty('');
                self.showSellForm(true);
                self.sellMsg('');
                self.sellError('');
            };

            self.hideSellForm = function() {
                self.showSellForm(false);
                self.sellQty('');
                self.sellStockId('');
                self.sellStockName('');
                self.sellMaxQty(0);
            };

            self.confirmSell = function() {
                var qtyToSell = Number(self.sellQty());
                if (!qtyToSell || qtyToSell < 1) {
                    self.sellError("Enter a valid quantity.");
                    return false;
                }
                if (qtyToSell > self.sellMaxQty()) {
                    self.sellError("Cannot sell more than you own.");
                    return false;
                }
                var user = JSON.parse(sessionStorage.getItem("user") || "{}");

                fetch('http://localhost:4569/stocks/' + self.sellStockId())
                    .then(res => {
                        if (!res.ok) throw new Error("Stock listing price not found");
                        return res.json();
                    })
                    .then(stock => {
                        var txn = {
                            custId: user.userId,
                            stockId: self.sellStockId(),
                            txnPrice: qtyToSell * (stock.stockPrice || stock.listingPrice),
                            txnType: "SELL",
                            qty: qtyToSell,
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
                        if (!res.ok) throw new Error("Sell failed");
                        self.sellMsg("Stock sold successfully.");
                        self.hideSellForm();
                        self.loadPortfolio();
                    })
                    .catch(err => {
                        self.sellError("Sell failed. " + (err.message || ''));
                    });
                return false;
            };

           self.toggleTxnHistory = function() {
    var currentlyVisible = self.txnHistoryVisible();
    self.txnHistoryVisible(!currentlyVisible);
    if (!currentlyVisible) {
        var user = JSON.parse(sessionStorage.getItem("user") || "{}");
        fetch('http://localhost:4569/transactions/' + user.userId, {
            method: 'GET'
        })
        .then(res => res.json())
        .then(data => self.transactionHistory(data || []));
    }
};

            self.disconnected = () => {};
            self.transitionCompleted = () => {};
        }
        return PortfolioViewModel;
    });