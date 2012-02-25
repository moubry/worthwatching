package com.moubry.worthwatching.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import com.moubry.worthwatching.CategorizedListItem;
import com.moubry.worthwatching.MovieAdapter;
import com.moubry.worthwatching.WWMovieTitleSuggestionsProvider;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.api.Movie;
import com.moubry.worthwatching.util.LicenseCheckActivity;
import com.moubry.worthwatching.util.WebServiceHelper;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends LicenseCheckActivity {
	
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
				WWMovieTitleSuggestionsProvider.GetAuthority(Boolean.parseBoolean(getString(R.string.show_ads))),
				WWMovieTitleSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(mQuery, null);
        
        Log.d(TAG, "mquery = " + mQuery);
        
        final CharSequence title = String.format("%s '%s'", getString(R.string.title_search_query), mQuery);
//        getActivityHelper().setActionBarTitle(title);
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
//        getActivityHelper().setupActionBar(null);
        handleIntent(getIntent());
        
        showAdsIfEnabled();
        checkLicense();
	}
	
	public ListView getListView()
	{
		return (ListView)this.findViewById(R.id.list);
	}

	private class SearchMoviesTask extends AsyncTask<String, Void, List<com.moubry.worthwatching.api.Movie>> {

		private int progressID;
		private String errorMessage;
		
		public void setProgressID(int i)
		{
			this.progressID = i;
		}
		
		// can use UI thread here
		protected void onPreExecute() {
			this.errorMessage = null;
			SearchActivity.this.findViewById(this.progressID).setVisibility(View.VISIBLE);
		}

		// can use UI thread here
		protected void onPostExecute(final List<com.moubry.worthwatching.api.Movie> result) {
			
			SearchActivity.this.findViewById(this.progressID).setVisibility(View.GONE);

			SearchActivity.this.getListView().setAdapter(new MovieAdapter(SearchActivity.this,
					R.layout.list_item, result, true));

			if(result.size() == 50)
			{
				Toast.makeText(getApplicationContext(), "Only 50 of the results are shown.", Toast.LENGTH_LONG).show();
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
			lv.getEmptyView().setVisibility(View.GONE);
			}
		}

		@Override
		protected List<com.moubry.worthwatching.api.Movie> doInBackground(String... query) {
			
			List<com.moubry.worthwatching.api.Movie> lstMovies = new ArrayList<com.moubry.worthwatching.api.Movie>();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("query", query[0]);
			params.put("count", "50");

			WebServiceHelper h = new WebServiceHelper(
					SearchActivity.this, 
					SearchActivity.this.getString(R.string.api) + "/movies/search",
					params);
			
			this.errorMessage = h.errorMessage;
			
			if(h.errorMessage == null)
			{
				try {
				    // Parse Response into our object
		            JsonParser parser = new JsonParser();
		            JsonArray array = parser.parse(h.result).getAsJsonArray();
		            
		            for (int i = 0; i < array.size(); i++) {
		                lstMovies.add(new Gson().fromJson(array.get(i), com.moubry.worthwatching.api.Movie.class));
		            }
				} catch (Exception e) {
					errorMessage = getString(R.string.unknown_service_error);
				}
			}

			return lstMovies;
		}
	}
}