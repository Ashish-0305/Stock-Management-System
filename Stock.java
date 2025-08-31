package com.example.demo;

import java.sql.Date;

public class Stock {
    private int stockId;
    private String stockName;
    private double stockPrice;
    private int stockVolume;
    private double listedPrice;
    private Date listedDate;
    private String listedExchange;

    
    public Stock() {
        super();
    }


	public Stock(int stockId, String stockName, double stockPrice, int stockVolume, double listedPrice, Date listedDate,
			String listedExchange) {
		super();
		this.stockId = stockId;
		this.stockName = stockName;
		this.stockPrice = stockPrice;
		this.stockVolume = stockVolume;
		this.listedPrice = listedPrice;
		this.listedDate = listedDate;
		this.listedExchange = listedExchange;
	}


	public int getStockId() {
		return stockId;
	}


	public void setStockId(int stockId) {
		this.stockId = stockId;
	}


	public String getStockName() {
		return stockName;
	}


	public void setStockName(String stockName) {
		this.stockName = stockName;
	}


	public double getStockPrice() {
		return stockPrice;
	}


	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}


	public int getStockVolume() {
		return stockVolume;
	}


	public void setStockVolume(int stockVolume) {
		this.stockVolume = stockVolume;
	}


	public double getListedPrice() {
		return listedPrice;
	}


	public void setListedPrice(double listedPrice) {
		this.listedPrice = listedPrice;
	}


	public Date getListedDate() {
		return listedDate;
	}


	public void setListedDate(Date listedDate) {
		this.listedDate = listedDate;
	}


	public String getListedExchange() {
		return listedExchange;
	}


	public void setListedExchange(String listedExchange) {
		this.listedExchange = listedExchange;
	}
}