package com.moubry.rottentomatoesapi;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.moubry.tomatoratings.R;


public class Movie implements Serializable {

	private static final long serialVersionUID = -8266063318647942475L;
	public String id;
	private String title;
	public String year;
	private String critics_consensus;
	public Ratings ratings;
	public Posters posters;
	public AlternateIDs alternate_ids;
	public String runtime;
	private String synopsis;
	public CastMember[] abridged_cast;
	public String mpaa_rating;
	public ReleaseDates release_dates;
	public Links links;
	
	public String getTitle(){
		return decodeUnicode(this.title);		
	}
	
	public Integer getMPAARatingImageID()
	{	
		Log.i("Jami", "MPAA Rating upper: " + this.mpaa_rating.toUpperCase());
		
		if(this.mpaa_rating == null || this.mpaa_rating.length() == 0)
		  return null;
		
		if(this.mpaa_rating.equalsIgnoreCase("NC-17"))
			return R.drawable.mpaa_nc17;
		else if(this.mpaa_rating.equalsIgnoreCase("R"))
			return R.drawable.mpaa_r;
		else if(this.mpaa_rating.equalsIgnoreCase("PG-13"))
		{
			Log.i("Jami", "PG 13 matched");
			return R.drawable.mpaa_pg13;
		}
		else if(this.mpaa_rating.equalsIgnoreCase("PG"))
			return R.drawable.mpaa_pg;
		else if(this.mpaa_rating.equalsIgnoreCase("G"))
			return R.drawable.mpaa_g;
	
		return null;
	}
	
	public String getCriticsConsensus()
	{
		if(this.critics_consensus == null || this.critics_consensus.length() == 0)
			return "Not available.";
			
		return this.critics_consensus; 
	}
	
	public String getSynopsis()
	{
		if(this.synopsis == null || this.synopsis.length() == 0)
			return "Not available.";
			
		return this.synopsis; 
	}
	
	public String getFormattedRuntime()
	{		
		if(this.runtime == null || this.runtime.length() == 0)
			return null;
		
		StringBuilder sbRating = new StringBuilder();
		
		int totalMinutes = Integer.parseInt(this.runtime);

		int minutes = totalMinutes % 60;
		int hours = (totalMinutes - minutes) / 60;
		
		if(hours> 0)
		{
			sbRating.append(hours);
			sbRating.append(hours > 1 ? " hours" : " hour");
			
			if(minutes > 0)
				sbRating.append(" ");
		}
		
		if(minutes>0)
		{
			sbRating.append(minutes);
			sbRating.append(minutes > 1 ? " minutes" : " minute");
		}
		
		return sbRating.toString();
	 }
	
	
	public String getTitleWithYear()
	{
		if (year == null || year.length() == 0)
			return this.getTitle();
		
		return this.getTitle() + " (" + year + ")";
	}
	
	public int getTomatoImageResource() {
		
		if(ratings.critics_score == -1)
		return R.drawable.blank;
		
		if(ratings.critics_score >= 60)
			return R.drawable.fresh;
		
		return R.drawable.rotten;
	}

	public int getAudienceTomatoImageResource() {
		if(ratings.audience_score == -1)
			return R.drawable.blank;
			
			if(ratings.audience_score >= 60)
				return R.drawable.popcorn;
			
			return R.drawable.badpopcorn;
			}

	public String getAbridgedCast() {
		
		StringBuilder sb = new StringBuilder();
		Boolean isFirst = true;
		
		for (CastMember c : this.abridged_cast)
		{
			if(isFirst)
				isFirst = false;	
			else
				sb.append(", ");
			
			sb.append(c.name);
		}

		return decodeUnicode(sb.toString());
	}
	
	public String decodeUnicode(String orig)
	{
		StringBuffer result = new StringBuffer();
		Pattern p = Pattern.compile("#(\\d{5})");		
		Matcher m = p.matcher(orig);

		while (m.find()) {
			
			Log.i("Jami", "GROUP 1: " + m.group(1));
			
		    m.appendReplacement(result, String.valueOf((char)Integer.parseInt(m.group(1))));
		}
		
		m.appendTail(result);
		
		return result.toString();
	}
	
	static String join(Collection<?> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<?> iter = s.iterator();
	     while (iter.hasNext()) {
	         builder.append(iter.next());
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }

	public String getMPAARatingDescription() {
		if ((this.mpaa_rating == null) || (this.mpaa_rating.length() == 0))
			return "No MPAA rating";
		return "MPAA rated " + this.mpaa_rating;
	}
}
