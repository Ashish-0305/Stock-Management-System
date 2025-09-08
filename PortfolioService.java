package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {
    private final PortfolioDAO dao;

    public PortfolioService(PortfolioDAO dao) { this.dao = dao; }

    public List<Portfolio> getCustomerPortfolio(int custId) {
        return dao.getCustomerPortfolio(custId);
    }
}