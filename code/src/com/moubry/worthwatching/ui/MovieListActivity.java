package com.moubry.worthwatching.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import com.moubry.worthwatching.CategorizedListItem;
import com.moubry.worthwatching.CategoryListItem;
import com.moubry.worthwatching.MovieAdapter;
import com.moubry.worthwatching.MovieListAsyncTask;
import com.moubry.worthwatching.MovieListCallback;
import com.moubry.worthwatching.MovieListDataSQLHelper;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.api.Movie;
import com.moubry.worthwatching.util.LicenseCheckActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MovieListActivity extends LicenseCheckActivity {

	public static final String TAG = "MovieListActivity";

	MovieListDataSQLHelper movieListData;
	private String m_cachedResult;
	
	private int m_ListID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);
			
		Bundle recdData = getIntent().getExtras();
        
        this.m_ListID = recdData.getInt("com.moubry.listid");

//		getActivityHelper().setupActionBar(getTitle());

		getListView().setEmptyView(this.findViewById(R.id.empty));
		getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				// Get the item that was clicked
				Movie movie = (Movie) parent.getItemAtPosition(position);

				Intent anotherActivityIntent = new Intent(v.getContext(), MovieRatingActivity.class);
				MovieRatingActivity.AddMovieDataToIntent(MovieListActivity.this,
						anotherActivityIntent, movie);
				startActivity(anotherActivityIntent);
			}
		});

		movieListData = new MovieListDataSQLHelper(this);

		Log.d(TAG, "before list");
		
        showWhatsNewIfEnabled();
        showAdsIfEnabled();
        checkLicense();
	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.list_menu_items, menu);
//		return true;
//	}

	@Override
	public void onDestroy() {
		movieListData.close();

		super.onDestroy();
	}

	protected void addMovieListToDb(String listName, String json) {
		SQLiteDatabase db = movieListData.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MovieListDataSQLHelper.TIME, new GregorianCalendar().getTimeInMillis());
		values.put(MovieListDataSQLHelper.TITLE, listName);
		values.put(MovieListDataSQLHelper.JSON, json);

		SQLiteStatement dbCountQuery;
		dbCountQuery = db.compileStatement(String.format("select count(*) from %s where %s = '%s'",
				MovieListDataSQLHelper.TABLE, MovieListDataSQLHelper.TITLE, listName));

		if (dbCountQuery.simpleQueryForLong() == 0) {
			db.insert(MovieListDataSQLHelper.TABLE, null, values);
		} else {
			db.update(MovieListDataSQLHelper.TABLE, values,
					String.format("%s = '%s'", MovieListDataSQLHelper.TITLE, listName), null);
		}
	}

	protected String getMovieListFromCache(String cachedResult, String listName) {
		
		if (cachedResult != null) {
			Log.i(TAG, listName + ": Getting result from cache");
			return cachedResult;
		}

		GregorianCalendar greg = new GregorianCalendar();

		greg.set(GregorianCalendar.HOUR_OF_DAY, 0);
		greg.set(GregorianCalendar.MINUTE, 0);
		greg.set(GregorianCalendar.SECOND, 0);
		greg.set(GregorianCalendar.MILLISECOND, 0);

		SQLiteDatabase db = movieListData.getReadableDatabase();
		Cursor cursor = db.query(MovieListDataSQLHelper.TABLE,
				new String[] { MovieListDataSQLHelper.JSON },
				String.format("%s = '%s'", MovieListDataSQLHelper.TITLE, listName)
						+ " AND " + MovieListDataSQLHelper.TIME + " > " + greg.getTimeInMillis(),
				null, null, null, null);

		startManagingCursor(cursor);

		if (cursor.moveToFirst()) {
			cachedResult = cursor.getString(0);

			Log.d(TAG, listName + ": Getting result from database.");

			return cachedResult;
		}

		return null;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		savedInstanceState.putString("result", this.m_cachedResult);

		Log.d(TAG, "Saving my state");

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		Log.d(TAG, "Restoring my state");

		this.m_cachedResult = savedInstanceState.getString("result");
	}

	public ListView getListView() {
		return (ListView) this.findViewById(R.id.list);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		Log.d(TAG, "on resume");

		this.getListView().getEmptyView().setVisibility(View.INVISIBLE);
		
		this.findViewById(R.id.progressBarLayout).setVisibility(View.VISIBLE);
		
		if ((this.m_cachedResult = this.getMovieListFromCache(this.m_cachedResult, this.getListName())) != null) {
			bindDataToList(m_cachedResult, false, null);
		} else {
			MovieListAsyncTask task = new MovieListAsyncTask(this, this.m_ListID, new MovieListCallback() {
				
				@Override
				public void HandleJsonResult(String json, String errorMessage) {
					MovieListActivity.this.bindDataToList(json, true, errorMessage);
				}
			});

			task.execute();
		}
	}

	public void bindDataToList(String jsonResult, boolean isNew, String errorMessage) {
		boolean isValid = true;
		List<com.moubry.worthwatching.api.Movie> lstMovies = new ArrayList<com.moubry.worthwatching.api.Movie>();
		
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
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(jsonResult).getAsJsonArray();
            
            for (int i = 0; i < array.size(); i++) {
                lstMovies.add(new Gson().fromJson(array.get(i), com.moubry.worthwatching.api.Movie.class));
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
			this.m_cachedResult = jsonResult;

			if (isNew)
				addMovieListToDb(this.getListName(), jsonResult);
		}
		
		
		getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, lstMovies, false));
		//this.categorizeItemsAndSetList(lstMovies);

	}

	protected void categorizeItemsAndSetList(List<CategorizedListItem> uncategorized) {
		
//		List<CategorizedListItem> categorized = new ArrayList<CategorizedListItem>();
//		
//		Collections.sort(uncategorized, new CategorizedListItemComparableByTheaterRelease());
//		
//		String category = "";
//		String nextCategory;
//		CategorizedListItem item;
//		for (int i = 0; i < uncategorized.size(); i++) {
//			item = uncategorized.get(i);
//			nextCategory = ((Movie) item).release_dates.getTheaterReleaseDate();
//			
//			// Only show ones with valid theater release dates
//			if(nextCategory != null && nextCategory.length() > 0)
//			{
//				if (!category.equals(nextCategory))
//					categorized.add(new CategoryListItem(nextCategory));
//	
//				category = nextCategory;
//	
//				categorized.add(item);
//			}
//		}
//		
//		getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, categorized, false));
	}

	protected String getListName(){return "ListID" + this.m_ListID;}
	
//	public class CategorizedListItemComparableByTheaterRelease implements Comparator<CategorizedListItem>{
//		 
//	    @Override
//	    public int compare(CategorizedListItem o1, CategorizedListItem o2) {
//	    	String s1 = ((Movie) o1).release_dates.theater;
//	    	
//	    	String s2 = ((Movie) o2).release_dates.theater;
//	    	
//	    	if(s1 == null)
//	    		s1 = "";
//	    	
//	    	if(s2 == null)
//	    		s2 = "";
//	    	
//	    	return s1.compareTo(s2);
//	    }
//	}
}