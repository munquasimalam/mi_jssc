package com.medas.mi.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static DateFormat formatter = null;
	private static String  today = null;
	
	public static Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		System.out.println("new Timestamp(today.getTime()):"+new Timestamp(today.getTime()));
		return new Timestamp(today.getTime());

	}
	
	public static String  getCurrentDateAndTime() {
		 Date date = Calendar.getInstance().getTime();

//	        // Display a date in day, month, year format
//	        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//	        String today = formatter.format(date);
//	        System.out.println("Today : " + today);
//
//	        // Display date with day name in a short format
//	        formatter = new SimpleDateFormat("EEE, dd/MM/yyyy");
//	        today = formatter.format(date);
//	        System.out.println("Today : " + today);
//
//	        // Display date with a short day and month name
//	        formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
//	        today = formatter.format(date);
//	        System.out.println("Today : " + today);

	        // Formatting date with full day and month name and show time up to
	        // milliseconds with AM/PM
		    formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm:ss.SSS a");
	        today = formatter.format(date);
	      //  System.out.println("Today : " + today);
	        
	        return today;


	}

}
