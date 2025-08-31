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
public class StockDAO extends JdbcDaoSupport {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init() {
        setDataSource(dataSource);
        System.out.println("StockDAO initialized with datasource: " + dataSource);
    }

    public JdbcTemplate giveJdbcTemplate() {
        return getJdbcTemplate();
    }

    public ResponseEntity<Object> addAStock(Stock newStock) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("INSERT INTO STOCKS (STOCK_ID, STOCK_NAME, STOCK_PRICE, STOCK_VOLUME, LISTED_PRICE, LISTED_DATE, LISTED_EXCHANGE) VALUES (?, ?, ?, ?, ?, ?, ?)",
                newStock.getStockId(), newStock.getStockName(), newStock.getStockPrice(), newStock.getStockVolume(),
                newStock.getListedPrice(), newStock.getListedDate(), newStock.getListedExchange());
        if (rowsAffected > 0)
            return ResponseEntity.status(201).body("Stock added successfully");
        else
            return ResponseEntity.status(500).body("Failed to add stock");
    }

    public ResponseEntity<ArrayList<Stock>> fetchAllStocks() {
        ArrayList<Stock> allFetchedStocks = new ArrayList<>();
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM STOCKS";
        List<Map<String, Object>> allStocks = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> st : allStocks) {
            try {
                Stock s = new Stock();

                Object stockIdObj = st.get("STOCK_ID");
                if (stockIdObj != null) {
                    s.setStockId(Integer.valueOf(stockIdObj.toString()));
                } else {
                    System.out.println("STOCK_ID is null for a row");
                    continue;
                }

                Object stockNameObj = st.get("STOCK_NAME");
                s.setStockName(stockNameObj != null ? stockNameObj.toString() : "");

                Object stockPriceObj = st.get("STOCK_PRICE");
                s.setStockPrice(stockPriceObj != null ? Double.valueOf(stockPriceObj.toString()) : 0.0);

                Object stockVolumeObj = st.get("STOCK_VOLUME");
                s.setStockVolume(stockVolumeObj != null ? Integer.valueOf(stockVolumeObj.toString()) : 0);

                Object listedPriceObj = st.get("LISTED_PRICE");
                s.setListedPrice(listedPriceObj != null ? Double.valueOf(listedPriceObj.toString()) : 0.0);

                // Debug and handle LISTED_DATE
                Object listedDateObj = st.get("LISTED_DATE");
                if (listedDateObj != null) {
                    System.out.println("LISTED_DATE value: " + listedDateObj + ", Type: " + listedDateObj.getClass().getName());
                    if (listedDateObj instanceof java.sql.Date) {
                        s.setListedDate((java.sql.Date) listedDateObj);
                    } else if (listedDateObj instanceof java.sql.Timestamp) {
                        s.setListedDate(new java.sql.Date(((java.sql.Timestamp) listedDateObj).getTime()));
                    } else {
                        System.out.println("Unsupported date type for LISTED_DATE: " + listedDateObj.getClass().getName());
                        s.setListedDate(null);
                    }
                } else {
                    System.out.println("LISTED_DATE is null for STOCK_ID: " + s.getStockId());
                    s.setListedDate(null);
                }

                Object listedExchangeObj = st.get("LISTED_EXCHANGE");
                s.setListedExchange(listedExchangeObj != null ? listedExchangeObj.toString() : "");

                allFetchedStocks.add(s);
            } catch (Exception e) {
                System.err.println("Error processing row: " + st);
                e.printStackTrace();
                continue;
            }
        }
        return ResponseEntity.status(200).body(allFetchedStocks);
    }

    public ResponseEntity<Object> fetchStockById(int stockId) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "SELECT * FROM STOCKS WHERE STOCK_ID = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, stockId);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("Stock with ID " + stockId + " not found");
        }
        Map<String, Object> st = result.get(0);
        try {
            Stock s = new Stock();

            Object stockIdObj = st.get("STOCK_ID");
            if (stockIdObj != null) {
                s.setStockId(Integer.valueOf(stockIdObj.toString()));
            }

            Object stockNameObj = st.get("STOCK_NAME");
            s.setStockName(stockNameObj != null ? stockNameObj.toString() : "");

            Object stockPriceObj = st.get("STOCK_PRICE");
            s.setStockPrice(stockPriceObj != null ? Double.valueOf(stockPriceObj.toString()) : 0.0);

            Object stockVolumeObj = st.get("STOCK_VOLUME");
            s.setStockVolume(stockVolumeObj != null ? Integer.valueOf(stockVolumeObj.toString()) : 0);

            Object listedPriceObj = st.get("LISTED_PRICE");
            s.setListedPrice(listedPriceObj != null ? Double.valueOf(listedPriceObj.toString()) : 0.0);

            // Debug and handle LISTED_DATE
            Object listedDateObj = st.get("LISTED_DATE");
            if (listedDateObj != null) {
                System.out.println("LISTED_DATE value: " + listedDateObj + ", Type: " + listedDateObj.getClass().getName());
                if (listedDateObj instanceof java.sql.Date) {
                    s.setListedDate((java.sql.Date) listedDateObj);
                } else if (listedDateObj instanceof java.sql.Timestamp) {
                    s.setListedDate(new java.sql.Date(((java.sql.Timestamp) listedDateObj).getTime()));
                } else {
                    System.out.println("Unsupported date type for LISTED_DATE: " + listedDateObj.getClass().getName());
                    s.setListedDate(null);
                }
            } else {
                System.out.println("LISTED_DATE is null for STOCK_ID: " + stockId);
                s.setListedDate(null);
            }

            Object listedExchangeObj = st.get("LISTED_EXCHANGE");
            s.setListedExchange(listedExchangeObj != null ? listedExchangeObj.toString() : "");

            return ResponseEntity.status(200).body(s);
        } catch (Exception e) {
            System.err.println("Error processing stock with ID " + stockId + ": " + st);
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing stock data for ID " + stockId);
        }
    }

    public ResponseEntity<Object> updateStockById(int stockId, Stock updatedStock) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        String sql = "UPDATE STOCKS SET STOCK_NAME = ?, STOCK_PRICE = ?, STOCK_VOLUME = ?, LISTED_PRICE = ?, LISTED_DATE = ?, LISTED_EXCHANGE = ? WHERE STOCK_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                updatedStock.getStockName(), updatedStock.getStockPrice(), updatedStock.getStockVolume(),
                updatedStock.getListedPrice(), updatedStock.getListedDate(), updatedStock.getListedExchange(), stockId);
        if (rowsAffected > 0) {
            return ResponseEntity.status(200).body("Stock updated successfully");
        } else {
            return ResponseEntity.status(404).body("Stock with ID " + stockId + " not found");
        }
    }

    public ResponseEntity<Object> patchStockById(int stockId, Stock partialStock) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        StringBuilder sql = new StringBuilder("UPDATE STOCKS SET ");
        List<Object> params = new ArrayList<>();
        boolean hasUpdates = false;

        if (partialStock.getStockName() != null) {
            sql.append("STOCK_NAME = ?");
            params.add(partialStock.getStockName());
            hasUpdates = true;
        }
        if (partialStock.getStockPrice() != 0.0) {
            if (hasUpdates) sql.append(", ");
            sql.append("STOCK_PRICE = ?");
            params.add(partialStock.getStockPrice());
            hasUpdates = true;
        }
        if (partialStock.getStockVolume() != 0) {
            if (hasUpdates) sql.append(", ");
            sql.append("STOCK_VOLUME = ?");
            params.add(partialStock.getStockVolume());
            hasUpdates = true;
        }
        if (partialStock.getListedPrice() != 0.0) {
            if (hasUpdates) sql.append(", ");
            sql.append("LISTED_PRICE = ?");
            params.add(partialStock.getListedPrice());
            hasUpdates = true;
        }
        if (partialStock.getListedDate() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("LISTED_DATE = ?");
            params.add(partialStock.getListedDate());
            hasUpdates = true;
        }
        if (partialStock.getListedExchange() != null) {
            if (hasUpdates) sql.append(", ");
            sql.append("LISTED_EXCHANGE = ?");
            params.add(partialStock.getListedExchange());
            hasUpdates = true;
        }

        if (!hasUpdates) {
            return ResponseEntity.status(400).body("No fields provided for update");
        }
        sql.append(" WHERE STOCK_ID = ?");
        params.add(stockId);
        int rowsAffected = jdbcTemplate.update(sql.toString(), params.toArray());
        if (rowsAffected > 0) {
            return ResponseEntity.status(200).body("Stock partially updated successfully");
        } else {
            return ResponseEntity.status(404).body("Stock with ID " + stockId + " not found");
        }
    }

    public ResponseEntity<Object> deleteAStockById(int sid) {
        JdbcTemplate jdbcTemplate = giveJdbcTemplate();
        int rowsAffected = jdbcTemplate.update("DELETE FROM STOCKS WHERE STOCK_ID = ?", sid);
        if (rowsAffected > 0)
            return ResponseEntity.status(200).body("Stock deleted successfully");
        else
            return ResponseEntity.status(404).body("Stock not found");
    }
}