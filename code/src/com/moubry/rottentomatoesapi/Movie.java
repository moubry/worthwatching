package com.moubry.rottentomatoesapi;

import java.util.Collection;
import java.util.Iterator;

import com.moubry.tomatoratings.R;


public class Movie {

	public String id;
	public String title;
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
			return title;
		
		return title + " (" + year + ")";
	}

	public int getTomatoImageResource() {
		
		if(ratings.critics_score == -1)
		return R.drawable.blank;
		
		if(ratings.critics_score >= 60)
			return R.drawable.ripe;
		
		return R.drawable.rotten;
	}

	public int getAudienceTomatoImageResource() {
		if(ratings.audience_score == -1)
			return R.drawable.blank;
			
			if(ratings.audience_score >= 60)
				return R.drawable.ripe;
			
			return R.drawable.rotten;
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
				
		
		return sb.toString();
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
}
