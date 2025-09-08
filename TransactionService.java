package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionService {
    @Autowired
    TransactionDAO transactionDAO;

    public ResponseEntity<Object> addATransaction(Transaction newTransaction) {
        return transactionDAO.addATransaction(newTransaction);
    }

    // CRITICAL FIX: The method now directly returns the ResponseEntity from the DAO.
    public ResponseEntity<ArrayList<Transaction>> fetchAllTransactions() {
        return transactionDAO.fetchAllTransactions();
    }

    // CRITICAL FIX: The method now correctly returns ResponseEntity<ArrayList<Transaction>>.
    public ResponseEntity<ArrayList<Transaction>> fetchTransactionsByCustId(int custId) {
        return transactionDAO.fetchTransactionsByCustId(custId);
    }

    public ResponseEntity<Object> updateTransactionById(int txnId, Transaction updatedTransaction) {
        return transactionDAO.updateTransactionById(txnId, updatedTransaction);
    }

    public ResponseEntity<Object> patchTransactionById(int txnId, Transaction partialTransaction) {
        return transactionDAO.patchTransactionById(txnId, partialTransaction);
    }

    public ResponseEntity<Object> deleteATransactionById(int txnId) {
        return transactionDAO.deleteATransactionById(txnId);
    }
}
