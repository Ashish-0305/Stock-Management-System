package com.example.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:8000/")
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @GetMapping("/{custId}")
    public ResponseEntity<?> getPortfolio(@PathVariable int custId) {
        List<Portfolio> portfolio = service.getCustomerPortfolio(custId);
        if(portfolio == null || portfolio.isEmpty())
            return ResponseEntity.status(404).body("No holdings found for this customer.");
        return ResponseEntity.ok(portfolio);
    }
}