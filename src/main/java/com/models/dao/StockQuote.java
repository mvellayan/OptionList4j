package com.models.dao;

import java.util.List;

public class StockQuote {
	public String con_id;
	public String symbol;
	public String time;	
	public Double bid;	
	public Double ask;
	
	public double bid_size;
	public double ask_size;	
	public double last;
	public double last_size;	
	public String volume;
	public double hist_volatility;
	public double implied_volatility;
	
	public StockQuote(List<String> rec) {
		this.con_id = rec.get(0);
		this.time =  rec.get(2);
		this.bid =  (rec.get(3).equals("nan")) ? null : Double.parseDouble(rec.get(3));
		this.ask =  (rec.get(5).equals("nan")) ? null : Double.parseDouble(rec.get(5));
	}

	@Override
	public String toString() {
		return "StockQuote [con_id=" + con_id + ", symbol=" + symbol + ", time=" + time + ", bid=" + bid + ", ask="
				+ ask + ", bid_size=" + bid_size + ", ask_size=" + ask_size + ", last=" + last + " ]";
	}

}
