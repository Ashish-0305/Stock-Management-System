package com.example.demo;

public class Portfolio{
	
	private int stockId;
    private String stockName;
    private double stockPrice;         
    private double listingPrice;       
    private int totalBought;
    private int totalSold;
    private int netHolding;            
    private double amountInvested;     
    private double amountEarnedFromSells; 
    private double currentValue;      
    private double profitLoss;          
    public Portfolio() {
		super();
	}
	public Portfolio(int stockId, String stockName, double stockPrice, double listingPrice, int totalBought,
			int totalSold, int netHolding, double amountInvested, double amountEarnedFromSells, double currentValue,
			double profitLoss) {
		super();
		this.stockId = stockId;
		this.stockName = stockName;
		this.stockPrice = stockPrice;
		this.listingPrice = listingPrice;
		this.totalBought = totalBought;
		this.totalSold = totalSold;
		this.netHolding = netHolding;
		this.amountInvested = amountInvested;
		this.amountEarnedFromSells = amountEarnedFromSells;
		this.currentValue = currentValue;
		this.profitLoss = profitLoss;
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
	public double getListingPrice() {
		return listingPrice;
	}
	public void setListingPrice(double listingPrice) {
		this.listingPrice = listingPrice;
	}
	public int getTotalBought() {
		return totalBought;
	}
	public void setTotalBought(int totalBought) {
		this.totalBought = totalBought;
	}
	public int getTotalSold() {
		return totalSold;
	}
	public void setTotalSold(int totalSold) {
		this.totalSold = totalSold;
	}
	public int getNetHolding() {
		return netHolding;
	}
	public void setNetHolding(int netHolding) {
		this.netHolding = netHolding;
	}
	public double getAmountInvested() {
		return amountInvested;
	}
	public void setAmountInvested(double amountInvested) {
		this.amountInvested = amountInvested;
	}
	public double getAmountEarnedFromSells() {
		return amountEarnedFromSells;
	}
	public void setAmountEarnedFromSells(double amountEarnedFromSells) {
		this.amountEarnedFromSells = amountEarnedFromSells;
	}
	public double getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}
	public double getProfitLoss() {
		return profitLoss;
	}
	public void setProfitLoss(double profitLoss) {
		this.profitLoss = profitLoss;
	}


}