package com.moubry.tomatoratings;

import java.util.HashMap;
import com.moubry.tomatoratings.util.WebServiceHelper;
import android.content.Context;
import android.os.AsyncTask;

public class MovieListAsyncTask extends AsyncTask<Void, Void, String> {
	private String listName;
	private Context context;
	private String errorMessage;
	public MovieListCallback callback;
	
	public MovieListAsyncTask(Context context, String listName, MovieListCallback callback)
	{
		this.listName = listName;
		this.context = context;
		this.callback = callback;	
	}

	protected void onPostExecute(String result) {
		callback.HandleJsonResult(result, this.errorMessage);
	}

	@Override
	protected String doInBackground(Void... unused) {
	
		WebServiceHelper h = new WebServiceHelper(context,
				"http://worthwatching.moubry.com/lists" + this.listName + ".json",
				new HashMap<String, String>());

		errorMessage = h.errorMessage;

		return h.result;
	}
}
