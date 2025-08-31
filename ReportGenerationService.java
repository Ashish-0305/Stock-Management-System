package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ReportGenerationService {
    private final ReportGeneration ReportGeneration;

    public ReportGenerationService(ReportGeneration ReportGeneration) {
        this.ReportGeneration = ReportGeneration;
    }

    public List<Map<String, Object>> customerAssetsWithTxnType() {
        return ReportGeneration.customerAssetsWithTxnType();
    }

    public Map<String, Object> customerWithMaxAssetValue() {
        return ReportGeneration.customerWithMaxAssetValue();
    }

    public Map<String, Object> customerWithMinAssetValue() {
        return ReportGeneration.customerWithMinAssetValue();
    }

    public Map<String, Object> mostTransactedStock() {
        return ReportGeneration.mostTransactedStock();
    }

    public Map<String, Object> leastTransactedStock() {
        return ReportGeneration.leastTransactedStock();
    }

    public List<Map<String, Object>> stocksNeverTransacted() {
        return ReportGeneration.stocksNeverTransacted();
    }

    public List<Map<String, Object>> allTransactedStocks() {
        return ReportGeneration.allTransactedStocks();
    }

    public List<Map<String, Object>> highestPricedStock() {
        return ReportGeneration.highestPricedStock();
    }

    public List<Map<String, Object>> lowestPricedStock() {
        return ReportGeneration.lowestPricedStock();
    }

    public List<Map<String, Object>> customerWithLowestPricedStock() {
        return ReportGeneration.customerWithLowestPricedStock();
    }

    public List<Map<String, Object>> allCustomers() {
        return ReportGeneration.allCustomers();
    }

    public List<Map<String, Object>> allTransactions() {
        return ReportGeneration.allTransactions();
    }

    public Map<String, Object> mostCommonTxnType() {
        return ReportGeneration.mostCommonTxnType();
    }

    public Map<String, Object> totalAssets() {
        return ReportGeneration.totalAssets();
    }
}