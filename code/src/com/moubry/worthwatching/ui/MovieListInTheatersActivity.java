package com.moubry.worthwatching.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.moubry.rottentomatoesapi.Movie;
import com.moubry.rottentomatoesapi.MovieSearchResult;
import com.moubry.worthwatching.CategorizedListItem;
import com.moubry.worthwatching.CategoryListItem;
import com.moubry.worthwatching.MovieAdapter;
import com.moubry.worthwatching.MovieListAsyncTask;
import com.moubry.worthwatching.MovieListCallback;
import com.moubry.worthwatching.R;

public class MovieListInTheatersActivity extends MovieListBaseActivity {

	@Override
	protected String getListName() {

		return "/movies/in_theaters";
	}

	protected String getOpeningListName() {
		return "/movies/opening";
	}

	public void addOpeningDataToList(String jsonResult, boolean isNew, String errorMessage) {
		boolean isValid = true;
		List<CategorizedListItem> lstOpeningMovies = new ArrayList<CategorizedListItem>();
		List<CategorizedListItem> lstMovies = new ArrayList<CategorizedListItem>();


		this.findViewById(R.id.progressBarLayout).setVisibility(View.GONE);

		if ((errorMessage == null) && (jsonResult == null))
			errorMessage = getString(R.string.unknown_service_error);

		if (errorMessage != null) {
			isValid = false;

			new AlertDialog.Builder(this).setTitle("Error").setMessage(errorMessage)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "Alert Dialog = OK");
						}
					}).show();

			getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, lstMovies, false));

			return;
		}
		
		try {
			// Parse Response into our object
			MovieSearchResult movieSearch = new Gson().fromJson(jsonResult, MovieSearchResult.class);

			GregorianCalendar greg = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			for (int i = 0; i < Math.min(movieSearch.movies.length, 3); i++) {
				if(movieSearch.movies[i].release_dates.theater.compareTo(sdf.format(greg.getTime())) <= 0)
					lstOpeningMovies.add(movieSearch.movies[i]);
			}
		} catch (Exception e) {

			isValid = false;

			new AlertDialog.Builder(this).setTitle("Error")
					.setMessage(getString(R.string.unknown_service_error))
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "AlertDialog = OK");
						}
					}).show();
		}
  
		if (isValid) {
			this.m_cachedOpeningResult = jsonResult;

			if (isNew)
				addMovieListToDb(this.getOpeningListName(), jsonResult);
		}
		
		if(lstOpeningMovies.size() > 0)
		{
			lstMovies.add(new CategoryListItem("Released This Week"));
			lstMovies.addAll(lstOpeningMovies);

			if(this.m_inTheaters.size() > 0)
				lstMovies.add(new CategoryListItem("Also Playing"));
		}
		
		Collections.sort(this.m_inTheaters, new CategorizedListItemComparableByTheaterReleaseDesc());
		lstMovies.addAll(this.m_inTheaters);
		
		getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, lstMovies, false));
	}

	private String m_cachedOpeningResult;
	
	private List<CategorizedListItem> m_inTheaters;
	
	@Override
	protected void categorizeItemsAndSetList(List<CategorizedListItem> uncategorized) {
		this.findViewById(R.id.progressBarLayout).setVisibility(View.VISIBLE);
		
		this.m_inTheaters = uncategorized;

		if ((this.m_cachedOpeningResult = this.getMovieListFromCache(this.m_cachedOpeningResult, this.getOpeningListName())) != null) {
			addOpeningDataToList(m_cachedOpeningResult, false, null);
		} else {
			MovieListAsyncTask task = new MovieListAsyncTask(this, this.getOpeningListName(),
					new MovieListCallback() {

						@Override
						public void HandleJsonResult(String json, String errorMessage) {
							MovieListInTheatersActivity.this.addOpeningDataToList(json, true, errorMessage);
						}
					});

			task.execute();
		}
	}
	
	public class CategorizedListItemComparableByTheaterReleaseDesc implements Comparator<CategorizedListItem>{
		 
	    @Override
	    public int compare(CategorizedListItem o1, CategorizedListItem o2) {
	    	String s1 = ((Movie) o1).release_dates.theater;
	    	
	    	String s2 = ((Movie) o2).release_dates.theater;
	    	
	    	if(s1 == null)
	    		s1 = "";
	    	
	    	if(s2 == null)
	    		s2 = "";
	    	
	    	return s2.compareTo(s1);
	    }
	}

}