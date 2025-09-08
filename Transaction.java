package com.example.demo;

import java.sql.Date;

public class Transaction {
    private int txnId;
    private int custId;
    private int stockId;
    private double txnPrice;
    private String txnType;
    private int qty;
    private java.util.Date txnDate;

    public Transaction() {
        super();
    }

    public Transaction(int txnId, int custId, int stockId, double txnPrice, String txnType, int qty, Date txnDate) {
        super();
        this.txnId = txnId;
        this.custId = custId;
        this.stockId = stockId;
        this.txnPrice = txnPrice;
        this.txnType = txnType;
        this.qty = qty;
        this.txnDate = txnDate;
    }

    public int getTxnId() {
        return txnId;
    }

    public void setTxnId(int txnId) {
        this.txnId = txnId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public double getTxnPrice() {
        return txnPrice;
    }

    public void setTxnPrice(double txnPrice) {
        this.txnPrice = txnPrice;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public java.util.Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(java.util.Date txnDate) {
        this.txnDate = txnDate;
    }
}
