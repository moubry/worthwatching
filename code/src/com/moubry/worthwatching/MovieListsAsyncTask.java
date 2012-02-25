package com.moubry.worthwatching;

import java.util.HashMap;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.util.WebServiceHelper;
import com.moubry.worthwatching.util.WebServiceRestHelper;

import android.content.Context;
import android.os.AsyncTask;

public class MovieListsAsyncTask extends AsyncTask<Void, Void, String> {
	private String listName;
	private Context context;
	private String errorMessage;
	public MovieListCallback callback;
	
	public MovieListsAsyncTask(Context context, String listName, MovieListCallback callback)
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
	
		WebServiceRestHelper h = new WebServiceRestHelper(context,
		        this.context.getString(R.string.api) + "/lists/popular",
				new HashMap<String, String>());

		errorMessage = h.errorMessage;

		return h.result;
	}
}
