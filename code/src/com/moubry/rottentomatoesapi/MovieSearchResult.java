package com.moubry.rottentomatoesapi;


public class MovieSearchResult {

	public int total;
	public Movie[] movies;
//	public String links;
//	public String link_template;

	public MovieSearchResult(){}

	public MovieSearchResult(int t)
	{
		this.total = t;		
	}
	
//	public MovieSearchResult(int t, Movie[] m, String l, String lt)
//	{
//		this.total = t;
//		this.movies = m;
//		this.links = l;
//		this.link_template = lt;	
//	}
	
}
