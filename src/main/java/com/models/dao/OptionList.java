package com.models.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OptionList {
	
	public String con_id;
	public String symbol;
	public String expiry;
	public LocalDate expiryLD;
	public long expiryL;
	public Double strike;
	public String right;
	public static DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public OptionList(List<String> rec) {
		this.con_id = rec.get(0);
		this.symbol =  rec.get(1);
		this.strike =  (rec.get(3).equals("nan")) ? null : Double.parseDouble(rec.get(3));
		this.expiry =  rec.get(2);
		this.right = rec.get(4);
		this.expiryLD = LocalDate.parse(expiry, dFormatter);
		this.expiryL = Long.parseLong(expiry);
	}

	@Override
	public String toString() {
		return "OptionList [con_id=" + con_id + ", symbol=" + symbol + ", expiry=" + expiry + ", strike=" + strike
				+ ", right=" + right + "]";
	}


}
