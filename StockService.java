package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

	// StockService depends on StockDAO
	@Autowired
	StockDAO stockDAO;
	
	public ResponseEntity<Object> addAStock(Stock newStock) {
		return stockDAO.addAStock(newStock);
	}
	
	public ResponseEntity<Object> fetchAllStocks() {
		return ResponseEntity.status(200).body(stockDAO.fetchAllStocks());
	}
	
	public ResponseEntity<Object> fetchStockById(int stockId) {
        return stockDAO.fetchStockById(stockId);
    }
	
	public ResponseEntity<Object> updateStockById(int stockId, Stock updatedStock) {
        return stockDAO.updateStockById(stockId, updatedStock);
    }
	
	public ResponseEntity<Object> patchStockById(int stockId, Stock partialStock) {
        return stockDAO.patchStockById(stockId, partialStock);
    }
	
	public ResponseEntity<Object> deleteAStockById(int sid) {
		return stockDAO.deleteAStockById(sid);
	}

}
