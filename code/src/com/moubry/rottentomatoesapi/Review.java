package com.moubry.rottentomatoesapi;


public class Review {
	private String critic;
	private String publication;
	public String date;
	public String original_score;
	public String quote;
	public Links links;
	
	public String getCritic() {
		if (critic == null || critic.length() == 0)
			return this.publication;
		
		return this.critic;
	}
	
	public String getPublication() {
		if (critic == null || critic.length() == 0)
			return null;
		
		return this.publication;
		
	}
}
