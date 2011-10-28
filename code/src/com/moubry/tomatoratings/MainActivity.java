package com.moubry.tomatoratings;

import com.moubry.tomatoratings.R;
import com.moubry.tomatoratings.ui.HomeActivity;
import com.moubry.tomatoratings.ui.MovieListBoxOfficeActivity;
import com.moubry.tomatoratings.ui.MovieListDVDReleasesActivity;
import com.moubry.tomatoratings.ui.MovieListInTheatersActivity;
import com.moubry.tomatoratings.ui.MovieListUpcomingActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String activityID = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_key_start_screen), "0");
		
		switch(Integer.parseInt(activityID))
		{
			case 1: 
				startActivity(new Intent(this, MovieListDVDReleasesActivity.class));
				break;
			case 2:
				startActivity(new Intent(this, MovieListInTheatersActivity.class));
				break;
			case 3:
				startActivity(new Intent(this, MovieListUpcomingActivity.class));
				break;	
			case 4:
				startActivity(new Intent(this, MovieListBoxOfficeActivity.class));
				break;					
			case 0:
			default:
				startActivity(new Intent(this, HomeActivity.class));
				break;
		}
		
		finish();		
	}
}