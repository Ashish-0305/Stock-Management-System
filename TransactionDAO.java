package com.example.demo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
public class TransactionDAO extends JdbcDaoSupport {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init() {
        setDataSource(dataSource);
        System.out.println("TransactionDAO initialized with datasource: " + dataSource);
    }

    public JdbcTemplate giveJdbcTemplate() {
        return getJdbcTemplate();
    }

    public ResponseEntity<Object> addATransaction(Transaction newTransaction) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("INSERT INTO TRANSACTIONS (TXN_ID, CUST_ID, STOCK_ID, TXN_PRICE, TXN_TYPE, QTY, TXN_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)",
                newTransaction.getTxnId(), newTransaction.getCustId(), newTransaction.getStockId(), 
                newTransaction.getTxnPrice(), newTransaction.getTxnType(), newTransaction.getQty(), newTransaction.getTxnDate());
        if (rowsAffected > 0)
            return ResponseEntity.status(201).body("Transaction added successfully");
        else
            return ResponseEntity.status(500).body("Failed to add transaction");
    }

    public ResponseEntity<ArrayList<Transaction>> fetchAllTransactions() {
        ArrayList<Transaction> allFetchedTransactions = new ArrayList<>();
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM TRANSACTIONS";
        List<Map<String, Object>> allTransactions = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> txn : allTransactions) {
            try {
                Transaction t = new Transaction();

                Object txnIdObj = txn.get("TXN_ID");
                if (txnIdObj != null) {
                    t.setTxnId(Integer.valueOf(txnIdObj.toString()));
                } else {
                    System.out.println("TXN_ID is null for a row");
                    continue; // Skip this row or handle as needed
                }

                Object custIdObj = txn.get("CUST_ID");
                t.setCustId(custIdObj != null ? Integer.valueOf(custIdObj.toString()) : 0);

                Object stockIdObj = txn.get("STOCK_ID");
                t.setStockId(stockIdObj != null ? Integer.valueOf(stockIdObj.toString()) : 0);

                Object txnPriceObj = txn.get("TXN_PRICE");
                t.setTxnPrice(txnPriceObj != null ? Double.valueOf(txnPriceObj.toString()) : 0.0);

                Object txnTypeObj = txn.get("TXN_TYPE");
                t.setTxnType(txnTypeObj != null ? txnTypeObj.toString() : "");

                Object qtyObj = txn.get("QTY");
                t.setQty(qtyObj != null ? Integer.valueOf(qtyObj.toString()) : 0);

                Object txnDateObj = txn.get("TXN_DATE");
                t.setTxnDate(txnDateObj instanceof Date ? (Date) txnDateObj : null);

                allFetchedTransactions.add(t);
            } catch (Exception e) {
                System.err.println("Error processing row: " + txn);
                e.printStackTrace();
                continue; // Skip problematic rows
            }
        }
        return ResponseEntity.status(200).body(allFetchedTransactions);
    }

    public ResponseEntity<Object> fetchTransactionById(int txnId) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM TRANSACTIONS WHERE TXN_ID = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, txnId);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("Transaction with ID " + txnId + " not found");
        }
        Map<String, Object> txn = result.get(0);
        try {
            Transaction t = new Transaction();

            Object txnIdObj = txn.get("TXN_ID");
            if (txnIdObj != null) {
                t.setTxnId(Integer.valueOf(txnIdObj.toString()));
            }

            Object custIdObj = txn.get("CUST_ID");
            t.setCustId(custIdObj != null ? Integer.valueOf(custIdObj.toString()) : 0);

            Object stockIdObj = txn.get("STOCK_ID");
            t.setStockId(stockIdObj != null ? Integer.valueOf(stockIdObj.toString()) : 0);

            Object txnPriceObj = txn.get("TXN_PRICE");
            t.setTxnPrice(txnPriceObj != null ? Double.valueOf(txnPriceObj.toString()) : 0.0);

            Object txnTypeObj = txn.get("TXN_TYPE");
            t.setTxnType(txnTypeObj != null ? txnTypeObj.toString() : "");

            Object qtyObj = txn.get("QTY");
            t.setQty(qtyObj != null ? Integer.valueOf(qtyObj.toString()) : 0);

            Object txnDateObj = txn.get("TXN_DATE");
            t.setTxnDate(txnDateObj instanceof Date ? (Date) txnDateObj : null);

            return ResponseEntity.status(200).body(t);
        } catch (Exception e) {
            System.err.println("Error processing transaction with ID " + txnId + ": " + txn);
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing transaction data for ID " + txnId);
        }
    }

    public ResponseEntity<Object> updateTransactionById(int txnId, Transaction updatedTransaction) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "UPDATE TRANSACTIONS SET CUST_ID = ?, STOCK_ID = ?, TXN_PRICE = ?, TXN_TYPE = ?, QTY = ?, TXN_DATE = ? WHERE TXN_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                updatedTransaction.getCustId(), updatedTransaction.getStockId(), updatedTransaction.getTxnPrice(),
                updatedTransaction.getTxnType(), updatedTransaction.getQty(), updatedTransaction.getTxnDate(), txnId);
        if (rowsAffected > 0) {
            return ResponseEntity.status(200).body("Transaction updated successfully");
        } else {
            return ResponseEntity.status(404).body("Transaction with ID " + txnId + " not found");
        }
    }

    public ResponseEntity<Object> patchTransactionById(int txnId, Transaction partialTransaction) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        StringBuilder sql = new StringBuilder("UPDATE TRANSACTIONS SET ");
        List<Object> params = new ArrayList<>();
        boolean hasUpdates = false;

        if (partialTransaction.getCustId() != 0) {
            sql.append("CUST_ID = ?");
            params.add(partialTransaction.getCustId());
            hasUpdates = true;
        }
        if (partialTransaction.getStockId() != 0) {
            if (hasUpdates) sql.append(", ");
            sql.append("STOCK_ID = ?");
            params.add(partialTransaction.getStockId());
            hasUpdates = true;
        }
        if (partialTransaction.getTxnPrice() != 0.0) {
            if (hasUpdates) sql.append(", ");
            sql.append("TXN_PRICE = ?");
            params.add(partialTransaction.getTxnPrice());
            hasUpdates = true;
        }
        if (partialTransaction.getTxnType() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("TXN_TYPE = ?");
            params.add(partialTransaction.getTxnType());
            hasUpdates = true;
        }
        if (partialTransaction.getQty() != 0) {
            if (hasUpdates) sql.append(", ");
            sql.append("QTY = ?");
            params.add(partialTransaction.getQty());
            hasUpdates = true;
        }
        if (partialTransaction.getTxnDate() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("TXN_DATE = ?");
            params.add(partialTransaction.getTxnDate());
            hasUpdates = true;
        }

        if (!hasUpdates) {
            return ResponseEntity.status(400).body("No fields provided for update");
        }
        sql.append(" WHERE TXN_ID = ?");
        params.add(txnId);
        int rowsAffected = jdbcTemplate.update(sql.toString(), params.toArray());
        if (rowsAffected > 0) {
            return ResponseEntity.status(200).body("Transaction partially updated successfully");
        } else {
            return ResponseEntity.status(404).body("Transaction with ID " + txnId + " not found");
        }
    }

    public ResponseEntity<Object> deleteATransactionById(int txnId) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("DELETE FROM TRANSACTIONS WHERE TXN_ID = ?", txnId);
        if (rowsAffected > 0)
            return ResponseEntity.status(200).body("Transaction deleted successfully");
        else
            return ResponseEntity.status(404).body("Transaction not found");
    }
}