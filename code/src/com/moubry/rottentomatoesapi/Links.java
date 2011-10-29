package com.moubry.rottentomatoesapi;

import com.moubry.tomatoratings.R;
import java.io.Serializable;

public class Links implements Serializable {

	private static final long serialVersionUID = -7735857806597107698L;
	
	// For Movie Object
	public String self;
	public String alternate;
	public String reviews;
	public String cast;
	public String similar;
	
	// For Review Object
	public String review;
}
