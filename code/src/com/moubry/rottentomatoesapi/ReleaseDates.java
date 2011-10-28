package com.moubry.rottentomatoesapi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class ReleaseDates {

	public String theater;

	public String getTheaterReleaseDate() {

		if(theater == null || theater.length() == 0)
			return null;

		 DateFormat formatter, outputFormatter; 
		
		try {  

		 Date date;
		  formatter = new SimpleDateFormat("yyyy-MM-dd");
		  date = (Date)formatter.parse(theater);  

		  
		  outputFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
		  
		  return outputFormatter.format(date);
		  
		  } catch (ParseException e)
		  {
			 Log.e("Exception :", e.getMessage());  
		  }  
		 
		  
		  return null;

	}
	
}
