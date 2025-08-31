package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    TransactionDAO transactionDAO;

    public ResponseEntity<Object> addATransaction(Transaction newTransaction) {
        return transactionDAO.addATransaction(newTransaction);
    }

    public ResponseEntity<Object> fetchAllTransactions() {
        return ResponseEntity.status(200).body(transactionDAO.fetchAllTransactions());
    }

    public ResponseEntity<Object> fetchTransactionById(int txnId) {
        return transactionDAO.fetchTransactionById(txnId);
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