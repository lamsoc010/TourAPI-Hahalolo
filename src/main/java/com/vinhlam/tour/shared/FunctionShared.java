package com.vinhlam.tour.shared;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class FunctionShared {
	
//	Check String is Date
	public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

//	Check String is Number
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
//	Convert price by currency
	public static Double convertCurrencyPrice(String currencyCurrent, double price, String currencyNew) {
		double priceNew = 0;
		if(!currencyNew.equalsIgnoreCase("USD") || !currencyNew.equalsIgnoreCase("VND")) {
			priceNew = price;
		}
		if(!currencyCurrent.equals(currencyNew)) {
			if(currencyCurrent.equalsIgnoreCase("VND") && currencyNew.equalsIgnoreCase("USD")) {
				priceNew = price/23555;
			}
			if(currencyCurrent.equalsIgnoreCase("USD") && currencyNew.equalsIgnoreCase("VND")) {
				priceNew = price * 23555;
			} 
		} else {
			priceNew = price;
		}
		
//		Convert price to BigDecimal
		int scale = 2;
		BigDecimal tempBig = convertNumberToBigDecimal(priceNew, scale);
		return Double.parseDouble(tempBig.toString());
	}
	
	public static BigDecimal convertNumberToBigDecimal(double price, int scale) {
		BigDecimal tempBig = new BigDecimal(Double.toString(price));
		tempBig = tempBig.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
		return tempBig;
	}
}
