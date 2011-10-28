package com.moubry.rottentomatoesapi;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class ReleaseDates implements Serializable {

	public static final String TAG = "ReleaseDates";
	private static final long serialVersionUID = 329551203278076026L;
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
			 Log.e(TAG, "Exception :" + e.getMessage());  
		  }  
		 
		  
		  return null;

	}
	
	
}
