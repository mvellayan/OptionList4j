package com.models.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.models.dao.OptionList;

public class OptionUtils {


	/**
	 * @param quoteTimeDate
	 * @param noWeeks
	 * @param optionsHM
	 * @return
	 */
	public static Set<String> getEpiryList(String quoteTimeDate, int noWeeks, HashMap<String, OptionList> optionsHM) {

		Set<String> allExpiryList = new TreeSet<String>();
		Set<String> expiryList = new TreeSet<String>();
		long quoteTimeDatei = Long.parseLong(quoteTimeDate.substring(0,8));

		for (OptionList ol : optionsHM.values()) {
			allExpiryList.add(ol.expiry);
		}

		Iterator<String> it = allExpiryList.iterator();
		while (it.hasNext() && expiryList.size() < noWeeks) {
			String nxt = it.next();
			long nxti = Long.parseLong(nxt);
			if (nxti >= quoteTimeDatei) {
				expiryList.add(nxt);
			}
		}

		return (expiryList);
	}

	public static ArrayList<OptionList> getContractIds(String quoteTimeDate, int noWeeks,
			HashMap<String, OptionList> optionsHM) {

		Set<String> expryList = getEpiryList(quoteTimeDate, noWeeks, optionsHM);
		ArrayList<OptionList> returnList = new ArrayList<OptionList>();

		for (OptionList ol : optionsHM.values()) {
			if (expryList.contains(ol.expiry))
				returnList.add(ol);
		}
		return (returnList);
	}

	public static ArrayList<Double> getComputedComponents(String quoteTime, double stockQuote, double optionQuote, double strike, String expiry) {

		double tv, iv, theta;
		ArrayList<Double> retValue = new ArrayList<Double>();
		
		if (stockQuote <= 0) {
			throw new RuntimeException("No Quote");
		}

		iv = (stockQuote - strike > 0) ? stockQuote - strike : 0;
		tv = optionQuote - iv;
		long dur = DateUtils.getMinsToExpire(quoteTime, expiry);
		if ((dur == 0) | (tv == 0))
			theta = 0;
		else
			theta = (tv / dur) * 10000; // 100 = cents, 100 = basis point
		
		retValue.add(tv);
		retValue.add(iv);
		retValue.add(theta);
		return retValue;

	}


}
