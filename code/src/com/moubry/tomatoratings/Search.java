package com.moubry.tomatoratings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.moubry.rottentomatoesapi.Movie;
import com.moubry.rottentomatoesapi.MovieSearchResult;

import com.moubry.tomatoratings.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Search extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		getListView().getEmptyView().setVisibility(View.INVISIBLE);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			new SearchMoviesTask().execute(query);
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

	private class SearchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

		private final ProgressDialog dialog = new ProgressDialog(Search.this);
		private Boolean hasConnectionError = false;
		private int totalResults = -1;

		// can use UI thread here
		protected void onPreExecute() {
			this.totalResults = -1;
			this.hasConnectionError = false;
			this.dialog.setMessage("Loading. Please wait...");
			this.dialog.show();
		}

		// can use UI thread here
		protected void onPostExecute(final List<Movie> result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			Search.this.setListAdapter(new MovieAdapter(Search.this,
					R.layout.list_item, result));

			if(this.totalResults > 50)
			{
				Toast.makeText(getApplicationContext(), String.format("Only 50 of the %d results are shown.", this.totalResults), Toast.LENGTH_LONG).show();
			}
			
			if(hasConnectionError)
			{
				new AlertDialog.Builder( Search.this )
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

			ListView lv = Search.this.getListView();
			lv.setTextFilterEnabled(true);
			lv.getEmptyView().setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected List<Movie> doInBackground(String... query) {

			List<Movie> lstMovies = new ArrayList<Movie>();
			// Instantiate the Web Service Class with he URL of the web service
			// not that you must pass
			
		

			Log.i("Info: ", "before webservice");

			WebService webService = new WebService(
					"http://api.rottentomatoes.com/api/public/v1.0/movies.json");

			Log.i("Info: ", "after webservice");

			// Pass the parameters if needed , if not then pass dummy one as
			// follows
			Map<String, String> params = new HashMap<String, String>();
			params.put("apikey", "cfuvchhe98amz6vd9qc2s8m7");
			params.put("q", query[0]);
			params.put("page_limit", "50");

			Log.i("Info ", "Query: " + query[0]);

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
				MovieSearchResult result = new Gson().fromJson(response,
						MovieSearchResult.class);

				this.totalResults = result.total;
				Log.i("Info ", "Total " + String.valueOf(result.total));

				for (int i = 0; i < result.movies.length; i++) {
					lstMovies.add(result.movies[i]);
				}
			} catch (Exception e) {
				Log.d("Error: ", e.getMessage());
			}

			return lstMovies;
		}
	}
}