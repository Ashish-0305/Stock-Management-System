package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    public TransactionController() {
        System.out.println("TransactionController initialized");
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> addATransaction(@RequestBody Transaction newTransaction) {
        System.out.println("Adding a new transaction called from the controller");
        return transactionService.addATransaction(newTransaction);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public ResponseEntity<Object> fetchAllTransactions() {
        System.out.println("Fetching all transactions called from the controller");
        return transactionService.fetchAllTransactions();
    }

    @RequestMapping(value = "/transactions/{txnId}", method = RequestMethod.GET)
    public ResponseEntity<Object> fetchTransactionById(@PathVariable("txnId") int txnId) {
        System.out.println("Fetching transaction with ID: " + txnId + " called from the controller");
        return transactionService.fetchTransactionById(txnId);
    }

    @RequestMapping(value = "/transactions/{txnId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateTransactionById(@PathVariable("txnId") int txnId, @RequestBody Transaction updatedTransaction) {
        System.out.println("Updating transaction with ID: " + txnId + " called from the controller");
        return transactionService.updateTransactionById(txnId, updatedTransaction);
    }

    @RequestMapping(value = "/transactions/{txnId}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> patchTransactionById(@PathVariable("txnId") int txnId, @RequestBody Transaction partialTransaction) {
        System.out.println("Partially updating transaction with ID: " + txnId + " called from the controller");
        return transactionService.patchTransactionById(txnId, partialTransaction);
    }

    @RequestMapping(value = "/transactions/{txnId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteATransactionById(@PathVariable("txnId") int txnId) {
        System.out.println("Deleting a transaction by ID called from the controller");
        return transactionService.deleteATransactionById(txnId);
    }
}