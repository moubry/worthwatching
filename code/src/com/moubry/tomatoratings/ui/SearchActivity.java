package com.moubry.tomatoratings.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.gson.Gson;
import com.moubry.rottentomatoesapi.Movie;
import com.moubry.rottentomatoesapi.MovieSearchResult;

import com.moubry.tomatoratings.R;
import com.moubry.tomatoratings.CategorizedListItem;
import com.moubry.tomatoratings.MovieAdapter;
import com.moubry.tomatoratings.MovieTitleSuggestionsProvider;
import com.moubry.tomatoratings.util.WebServiceHelper;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends BaseActivity {
	
	public static final String TAG = "SearchActivity";
    private String mQuery;

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
		mQuery = intent.getStringExtra(SearchManager.QUERY);
    	
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
				MovieTitleSuggestionsProvider.AUTHORITY, MovieTitleSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(mQuery, null);
        
        Log.d(TAG, "mquery = " + mQuery);
        
        final CharSequence title = String.format("%s '%s'", getString(R.string.title_search_query), mQuery);
        getActivityHelper().setActionBarTitle(title);
        setTitle(title);
		
		getListView().setEmptyView(this.findViewById(R.id.empty));
		
		getListView().setOnItemClickListener(new ListView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				// Get the item that was clicked
				Movie movie = (Movie) parent.getItemAtPosition(position);
				
				Intent anotherActivityIntent = new Intent(v.getContext(), MovieRatingActivity.class);
				MovieRatingActivity.AddMovieDataToIntent(SearchActivity.this, anotherActivityIntent, movie);
				startActivity(anotherActivityIntent);
			}
		});	
		
		getListView().getEmptyView().setVisibility(View.GONE);

		SearchMoviesTask task = new SearchMoviesTask();
		task.setProgressID(R.id.progressBarLayout);
		task.execute(mQuery);
    }
	
	/** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        getActivityHelper().setupActionBar(null);
        handleIntent(getIntent());
        
        showAdsIfEnabled();
	}
	
	public ListView getListView()
	{
		return (ListView)this.findViewById(R.id.list);
	}

	private class SearchMoviesTask extends AsyncTask<String, Void, List<CategorizedListItem>> {

		private int progressID;
		private String errorMessage;
		private int totalResults = -1;
		
		public void setProgressID(int i)
		{
			this.progressID = i;
		}
		
		// can use UI thread here
		protected void onPreExecute() {
			this.totalResults = -1;
			this.errorMessage = null;
			SearchActivity.this.findViewById(this.progressID).setVisibility(View.VISIBLE);
		}

		// can use UI thread here
		protected void onPostExecute(final List<CategorizedListItem> result) {
			
			SearchActivity.this.findViewById(this.progressID).setVisibility(View.GONE);

			SearchActivity.this.getListView().setAdapter(new MovieAdapter(SearchActivity.this,
					R.layout.list_item, result, true));

			if(this.totalResults > 50)
			{
				Toast.makeText(getApplicationContext(), String.format("Only 50 of the %d results are shown.", this.totalResults), Toast.LENGTH_LONG).show();
			}
			
			if(errorMessage != null)
			{
				new AlertDialog.Builder( SearchActivity.this )
                .setTitle( "Error" )
                .setMessage( errorMessage )
                .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "AlertDialog = OK");
                    }
                })
                .show();
			}
			else
			{

			ListView lv = SearchActivity.this.getListView();
			lv.setTextFilterEnabled(true);
			lv.getEmptyView().setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected List<CategorizedListItem> doInBackground(String... query) {
			
			List<CategorizedListItem> lstMovies = new ArrayList<CategorizedListItem>();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("q", query[0]);
			params.put("page_limit", "50");

			WebServiceHelper h = new WebServiceHelper(
					SearchActivity.this, 
					getString(R.string.app_engine_url)+ "/movies.json",
					params);
			
			this.errorMessage = h.errorMessage;
			
			if(h.errorMessage == null)
			{
				try {
					// Parse Response into our object
					MovieSearchResult result = new Gson().fromJson(h.result, MovieSearchResult.class);
		
					this.totalResults = result.total;
					Log.d(TAG, "Total " + String.valueOf(result.total));
		
					for (int i = 0; i < result.movies.length; i++) {
						lstMovies.add(result.movies[i]);
					}
				} catch (Exception e) {
					errorMessage = getString(R.string.unknown_service_error);
				}
			}

			return lstMovies;
		}
	}
}