package com.moubry.rottentomatoesapi;

public class Ratings {

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
}
