package com.moubry.tomatoratings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.moubry.tomatoratings.R;

import com.google.gson.Gson;
import com.moubry.rottentomatoesapi.Movie;
import com.moubry.rottentomatoesapi.MovieSearchResult;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Main extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
		Log.i("Jami","before list");
		
		
		
		
	}
//	
//	@Override
//	public void onSaveInstanceState(Bundle savedInstanceState) {
//		
//	  savedInstanceState.putLong("LastRefreshDate", this.lastRefresh.getTime().getTime());
//
//	  
//	  Log.i("Jami", String.format("Saving date %d-%d-%d %d:%d", this.lastRefresh.getTime().getYear(), this.lastRefresh.getTime().getMonth(), this.lastRefresh.getTime().getDate(), this.lastRefresh.getTime().getHours(), this.lastRefresh.getTime().getMinutes()));
//	  
//	  super.onSaveInstanceState(savedInstanceState);	
//	}
	
	
//	@Override
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//	  super.onRestoreInstanceState(savedInstanceState);
//	  // Restore UI state from the savedInstanceState.
//	  // This bundle has also been passed to onCreate.
//	  
//	  Date lastRefreshDate = new Date(savedInstanceState.getLong("LastRefreshDate"));
//	  
//	  this.lastRefresh = new GregorianCalendar(
//			  lastRefreshDate.getYear(), 
//			  lastRefreshDate.getMonth(), 
//			  lastRefreshDate.getDate(),
//			  0, 0, 0);
//	
//	  Log.i("Jami", String.format("Restoring %d-%d-%d", lastRefreshDate.getYear(), lastRefreshDate.getMonth(), lastRefreshDate.getDate()));
//	  
//	}
	
	private GregorianCalendar lastRefresh;
	
	private boolean needsRefresh()
	{
		if(lastRefresh == null)
		{
			Log.i("Jami", "lastRefresh == null");
			return true;
		}
		
		  GregorianCalendar today = new GregorianCalendar(
				  new Date().getYear(), 
				  new Date().getMonth(),
				  new Date().getDate(),
				  0, 0, 0);
		  

		  lastRefresh.set(Calendar.HOUR, 0);
		  lastRefresh.set(Calendar.MINUTE, 0);
		  lastRefresh.set(Calendar.SECOND, 0);
		  lastRefresh.set(Calendar.MILLISECOND, 0);
		  
		  //Log.i("Jami", String.format("Today: %d-%d-%d", today);
		  Log.i("Jami", String.format("Today %d-%d-%d: %d", today.get(Calendar.MONTH), today.get(Calendar.DATE), today.get(Calendar.YEAR), today.get(Calendar.SECOND)));
		  
		  Log.i("Jami", String.format("Last Refresh %d-%d-%d: %d", lastRefresh.get(Calendar.MONTH), lastRefresh.get(Calendar.DATE), lastRefresh.get(Calendar.YEAR), lastRefresh.get(Calendar.SECOND)));
		  Log.i("Jami", String.valueOf(lastRefresh.compareTo(today) ));

		  Log.i("Jami", String.valueOf(today.compareTo(lastRefresh) ));
		  
		  return lastRefresh.compareTo(today) == 1;
	}

	
	@Override
	protected void onResume() {
		super.onResume();

		Log.i("Jami","on resume");
		
		if(this.needsRefresh())
		{
			getListView().getEmptyView().setVisibility(View.INVISIBLE);
	
			GetTopMoviesTask task = new GetTopMoviesTask();
			task.setProgressID(R.id.progressBarLayout);
			task.execute();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Movie movie = (Movie) this.getListAdapter().getItem(position);

		Intent anotherActivityIntent = new Intent(this, MovieRating.class);
		MovieRating.AddMovieDataToIntent(anotherActivityIntent, movie);
		startActivity(anotherActivityIntent);

	}
	
	private void SetLastRefresh()
	{
		Date date = new Date();
		
		this.lastRefresh = new GregorianCalendar(
				date.getYear(),
				date.getMonth(),
				date.getDate(),
				0, 0, 0);
	}

	private class GetTopMoviesTask extends AsyncTask<Void, Void, List<Movie>> {


		private int progressID;
		private boolean hasConnectionError = false;
		
		public void setProgressID(int i)
		{
			this.progressID = i;
		}

		// can use UI thread here
		protected void onPreExecute() {
			this.hasConnectionError = false;
			
			Main.this.findViewById(this.progressID).setVisibility(View.VISIBLE);
		}

		// can use UI thread here
		protected void onPostExecute(List<Movie> result) {
			
			Main.this.findViewById(this.progressID).setVisibility(View.GONE);

			Main.this.setListAdapter(new MovieAdapter(Main.this,
					R.layout.list_item, result));

			Log.i("INfo: ", String.valueOf(result.size()));
			
			if(hasConnectionError)
			{
				Main.this.SetLastRefresh();
				Main.this.lastRefresh.add(GregorianCalendar.DATE, -1);
				
				new AlertDialog.Builder( Main.this )
                .setTitle( "Internet Connection Required" )
                .setMessage( "Tomato Ratings requires an internet connection." )
                .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Positive");
                    }
                })
                .show();
			}
			else
			{
				ListView lv = Main.this.getListView();
				lv.setTextFilterEnabled(true);
				lv.getEmptyView().setVisibility(View.VISIBLE);
				
				Main.this.SetLastRefresh();
			}
			
		}

		@Override
		protected List<Movie> doInBackground(Void... unused) {

			List<Movie> lstMovies = new ArrayList<Movie>();
			
			// Instantiate the Web Service Class with he URL of the web service
			// not that you must pass

			Log.i("Info: ", "before webservice");

			WebService webService = new WebService(
					"http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json");

			Log.i("Info: ", "after webservice");

			// Pass the parameters if needed , if not then pass dummy one as
			// follows
			Map<String, String> params = new HashMap<String, String>();
			params.put("apikey", "cfuvchhe98amz6vd9qc2s8m7");

			// Get JSON response from server the "" are where the method name
			// would normally go if needed example
			// webService.webGet("getMoreAllerts", params);
			String response = webService.webGet("", params);

			Log.i("Info: ", "after webservice webGet");

			
			if (response == null)
			{
				this.hasConnectionError = true;
				return lstMovies;
			}
			
			try {
				// Parse Response into our object
				MovieSearchResult result = new Gson().fromJson(response, MovieSearchResult.class);

				for (int i = 0; i < result.movies.length; i++) {
					lstMovies.add(result.movies[i]);
				}
			} catch (Exception e) {
				Log.d("Error: ", e.getMessage());
			}

			return lstMovies;
		}
	}

	public void fireSearch(View view) {
		onSearchRequested();
	}
}
