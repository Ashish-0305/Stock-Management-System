package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportGenerationController {
    private final ReportGenerationService ReportGenerationService;

    public ReportGenerationController(ReportGenerationService ReportGenerationService) {
        this.ReportGenerationService = ReportGenerationService;
    }

    // 1. Customer assets with transaction type
    @GetMapping("/customer-assets-details")
    public ResponseEntity<List<Map<String, Object>>> customerAssetsDetails() {
        return ResponseEntity.ok(ReportGenerationService.customerAssetsWithTxnType());
    }

    // 2. Customer with maximum asset value
    @GetMapping("/customer-max-asset")
    public ResponseEntity<Map<String, Object>> customerMaxAsset() {
        return ResponseEntity.ok(ReportGenerationService.customerWithMaxAssetValue());
    }

    // 3. Customer with minimum asset value
    @GetMapping("/customer-min-asset")
    public ResponseEntity<Map<String, Object>> customerMinAsset() {
        return ResponseEntity.ok(ReportGenerationService.customerWithMinAssetValue());
    }

    // 4. Stock transacted most
    @GetMapping("/stock-most-transacted")
    public ResponseEntity<Map<String, Object>> stockMostTransacted() {
        return ResponseEntity.ok(ReportGenerationService.mostTransactedStock());
    }

    // 5. Stock transacted least
    @GetMapping("/stock-least-transacted")
    public ResponseEntity<Map<String, Object>> stockLeastTransacted() {
        return ResponseEntity.ok(ReportGenerationService.leastTransactedStock());
    }

    // 6. Stocks never transacted
    @GetMapping("/stocks-never-transacted")
    public ResponseEntity<List<Map<String, Object>>> stocksNeverTransacted() {
        return ResponseEntity.ok(ReportGenerationService.stocksNeverTransacted());
    }

    // 7. All distinct transacted stocks
    @GetMapping("/stocks-transacted-all")
    public ResponseEntity<List<Map<String, Object>>> stocksTransactedAll() {
        return ResponseEntity.ok(ReportGenerationService.allTransactedStocks());
    }

    // 8. Stock with highest price
    @GetMapping("/stock-highest-price")
    public ResponseEntity<List<Map<String, Object>>> stockHighestPrice() {
        return ResponseEntity.ok(ReportGenerationService.highestPricedStock());
    }

    // 9. Stock with lowest price
    @GetMapping("/stock-lowest-price")
    public ResponseEntity<List<Map<String, Object>>> stockLowestPrice() {
        return ResponseEntity.ok(ReportGenerationService.lowestPricedStock());
    }

    // 10. Customer(s) who bought the lowest priced stock
    @GetMapping("/customer-lowest-priced-stock")
    public ResponseEntity<List<Map<String, Object>>> customerLowestPricedStock() {
        return ResponseEntity.ok(ReportGenerationService.customerWithLowestPricedStock());
    }

    // 11. All customer details
    @GetMapping("/customers")
    public ResponseEntity<List<Map<String, Object>>> allCustomers() {
        return ResponseEntity.ok(ReportGenerationService.allCustomers());
    }

    // 12. All transaction details
    @GetMapping("/transactions")
    public ResponseEntity<List<Map<String, Object>>> allTransactions() {
        return ResponseEntity.ok(ReportGenerationService.allTransactions());
    }

    // 13. Most common transaction type (sell/buy)
    @GetMapping("/most-common-txn-type")
    public ResponseEntity<Map<String, Object>> mostCommonTxnType() {
        return ResponseEntity.ok(ReportGenerationService.mostCommonTxnType());
    }

    // 14. Total assets of all customers
    @GetMapping("/total-assets")
    public ResponseEntity<Map<String, Object>> totalAssets() {
        return ResponseEntity.ok(ReportGenerationService.totalAssets());
    }
}