package com.moubry.rottentomatoesapi;

import com.moubry.worthwatching.R;
import java.io.Serializable;

public class Ratings implements Serializable {

	private static final long serialVersionUID = 6787523081534735338L;
	public int critics_score;
	public String critics_rating;
	public int audience_score;
	public String audience_rating;
	
	public String getFormattedCriticsScore()
	{
	  if(this.critics_score == -1)
		  return "---";
	  
	  return this.critics_score + "%";
	}

	public String getFormattedAudienceScore() {
		if(this.audience_score == -1)
			  return "---";
		  
		  return this.audience_score + "%";
		}
	
	public String getCriticsScoreDescription() {
		
		if(critics_score == -1)
			return null;
		
		if(critics_score >= 60)
			return "Fresh";
		
		return "Rotten";
	}
}
