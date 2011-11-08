package com.moubry.worthwatching;

import java.util.HashMap;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.util.WebServiceHelper;

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
				this.context.getString(R.string.app_engine_url) + "/lists" + this.listName + ".json",
				new HashMap<String, String>());

		errorMessage = h.errorMessage;

		return h.result;
	}
}
