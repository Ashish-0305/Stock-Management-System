package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PortfolioDAO {
    private final JdbcTemplate jdbcTemplate;

    public PortfolioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Portfolio> getCustomerPortfolio(int custId) {
        String sql = """
            SELECT
              s.STOCK_ID,
              s.STOCK_NAME,
              s.STOCK_PRICE,
              s.LISTING_PRICE,
              SUM(CASE WHEN t.TXN_TYPE = 'BUY' THEN t.QTY ELSE 0 END) AS TOTAL_BOUGHT,
              SUM(CASE WHEN t.TXN_TYPE = 'SELL' THEN t.QTY ELSE 0 END) AS TOTAL_SOLD,
              (SUM(CASE WHEN t.TXN_TYPE = 'BUY' THEN t.QTY ELSE 0 END)
               - SUM(CASE WHEN t.TXN_TYPE = 'SELL' THEN t.QTY ELSE 0 END)) AS NET_HOLDING,
              SUM(CASE WHEN t.TXN_TYPE = 'BUY' THEN t.QTY * t.TXN_PRICE ELSE 0 END) AS AMOUNT_INVESTED,
              SUM(CASE WHEN t.TXN_TYPE = 'SELL' THEN t.QTY * t.TXN_PRICE ELSE 0 END) AS AMOUNT_EARNED_FROM_SELLS
            FROM TRANSACTIONS t
            JOIN STOCKS s ON t.STOCK_ID = s.STOCK_ID
            WHERE t.CUST_ID = ?
            GROUP BY s.STOCK_ID, s.STOCK_NAME, s.STOCK_PRICE, s.LISTING_PRICE
            HAVING (SUM(CASE WHEN t.TXN_TYPE = 'BUY' THEN t.QTY ELSE 0 END)
                    - SUM(CASE WHEN t.TXN_TYPE = 'SELL' THEN t.QTY ELSE 0 END)) > 0
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Portfolio dto = new Portfolio();
            dto.setStockId(rs.getInt("STOCK_ID"));
            dto.setStockName(rs.getString("STOCK_NAME"));
            dto.setStockPrice(rs.getDouble("STOCK_PRICE"));
            dto.setListingPrice(rs.getDouble("LISTING_PRICE"));
            dto.setTotalBought(rs.getInt("TOTAL_BOUGHT"));
            dto.setTotalSold(rs.getInt("TOTAL_SOLD"));
            dto.setNetHolding(rs.getInt("NET_HOLDING"));
            dto.setAmountInvested(rs.getDouble("AMOUNT_INVESTED"));
            dto.setAmountEarnedFromSells(rs.getDouble("AMOUNT_EARNED_FROM_SELLS"));

            double currentValue = dto.getNetHolding() * dto.getStockPrice();
            dto.setCurrentValue(currentValue);

            double profitLoss = currentValue + dto.getAmountEarnedFromSells() - dto.getAmountInvested();
            dto.setProfitLoss(profitLoss);

            return dto;
        }, custId);
    }
}