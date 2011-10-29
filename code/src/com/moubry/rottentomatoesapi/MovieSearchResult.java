package com.moubry.rottentomatoesapi;

import com.moubry.tomatoratings.R;
import java.io.Serializable;

public class MovieSearchResult implements Serializable {

	private static final long serialVersionUID = -6150097190198062588L;
	public int total;
	public Movie[] movies;
	public Review[] reviews;

	public MovieSearchResult(){}
}
