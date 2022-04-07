package com.models;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Models {

	public static String modelTitles[]  = {
	               "1: In the money (ITM) > 1  TV [ 2.3, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] ",
	               "2: Out of the money (OTM) by > 1.  TV [ 2.3, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] ",
	               "3: Out of the money (OTM) by > 2.  TV [ 2.3, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] ",
	               "4: Out of the money (OTM) by > 3.  TV [ 2.3, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] ",
	               "5: Out of the money (OTM) by > 4.  TV [ 2.3, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] ",
	               "6: Out of the money (OTM) by > 1.  TV [ 2.8, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] ",
	               "7: Out of the money (OTM) by > 1.  TV [ 1.8, INV ]  TV [ -inf, inf ]  Theta [ 1 - inf ]\nTV [ -inf, 1 ]  & net [ 0, inf ] "
	};
	
	public static ArrayList<Integer> modelNumbers = new ArrayList<Integer>(); 
	
	static { 
		modelNumbers.add(1);
		modelNumbers.add(2);
		modelNumbers.add(3);
		modelNumbers.add(4);
		modelNumbers.add(5);
		modelNumbers.add(6);
		modelNumbers.add(7);
	}
	
	public static int getModelCount() {
		return modelTitles.length;
	}
	
	public static String getDescription(int model_no) {
	    return modelTitles[model_no-1];
	}
	
	public static boolean open_position(int model_no, double open_stock_bid, double open_stock_ask, double open_option_bid, 
			double open_option_ask, double open_tv, double open_iv, double open_theta, double strike) {
		
		boolean retValue = false;
		switch (model_no) {		
			case 1: 
				retValue = (open_tv > 2.3) & (open_theta > 1) & (strike- 1 ) > open_stock_ask;
		        break;
			case 2:
				retValue = (open_tv > 2.3) & (open_theta > 1) & (strike + 1) < open_stock_ask;
		        break;
			case 3:
				retValue = (open_tv > 2.3) & (open_theta > 1) & (strike + 2) < open_stock_ask;
		        break;
			case 4:
				retValue = (open_tv > 2.3) & (open_theta > 1) & (strike + 3) < open_stock_ask;
		        break;
			case 5:
				retValue = (open_tv > 2.3) & (open_theta > 1) & (strike + 4) < open_stock_ask;
		        break;
			case 6:
				retValue = (open_tv > 2.8) & (open_theta > 1) & (strike+1) < open_stock_ask;
		        break;
			case 7:
				retValue = (open_tv > 1.8) & (open_theta > 1) & (strike+1) < open_stock_ask;
		        break;
	
		    default :
		        System.out.println(model_no + " " + open_stock_bid + " " + open_stock_ask + " " + open_option_bid + " " + open_option_ask 
		        		+ " " + open_tv + " " +  open_iv + " " + open_theta + " " + strike);
		        assert false: "Unexpected model number in open_position";
		} // end switch
	
	return (retValue);
	
	}


	public static boolean close_position(Integer model_no, double close_tv, double close_iv, double close_theta, double strike, 
			double net_stock, double net_option) {

	    if (modelNumbers.contains(model_no)) {
	        return (close_tv < 1 & (net_stock + net_option > 0));
	    } else {
	        System.out.println("parameters:  " + model_no + " " + close_tv + " " +  close_iv + " " +  close_theta + " " +  strike + " " +  net_stock + " " +  net_option);
	        assert false: "Unexpected model number in close_position";
	    }
	    return false;
	}
	
}