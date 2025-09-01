package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockControllerMS {

	@Autowired
	StockService ss;
	
	
	// for adding a new stock
	@RequestMapping(value="/stocks", method=RequestMethod.POST)
	public ResponseEntity<Object> addAStock(@RequestBody Stock newStock) {
		System.out.println("Adding a new stock called from the controller");
		return ss.addAStock(newStock);
	}
	
	@RequestMapping(value="/stocks", method=RequestMethod.GET)
	public ResponseEntity<Object> fetchAllStocks() {
		System.out.println("Fetching all stocks called from the controller");
		return ss.fetchAllStocks();
	}
	
	@RequestMapping(value = "/stocks/{stockId}", method = RequestMethod.GET)
    public ResponseEntity<Object> fetchStockById(@PathVariable("stockId") int stockId) {
        System.out.println("Fetching stock with ID: " + stockId + " called from the controller");
        return ss.fetchStockById(stockId);
    }
	
	@RequestMapping(value = "/stocks/{stockId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateStockById(@PathVariable("stockId") int stockId, @RequestBody Stock updatedStock) {
        System.out.println("Updating stock with ID: " + stockId + " called from the controller");
        return ss.updateStockById(stockId, updatedStock);
    }
	
	 @RequestMapping(value = "/stocks/{stockId}", method = RequestMethod.PATCH)
	    public ResponseEntity<Object> patchStockById(@PathVariable("stockId") int stockId, @RequestBody Stock partialStock) {
	        System.out.println("Partially updating stock with ID: " + stockId + " called from the controller");
	        return ss.patchStockById(stockId, partialStock);
	    }
	
	@RequestMapping(value="/stocks/{stockId}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteAStockById(@PathVariable("stockId") int sid) {
		System.out.println("Deleting a stock by id called from the controller");
		return ss.deleteAStockById(sid);
	}


}
