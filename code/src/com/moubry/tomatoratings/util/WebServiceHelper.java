package com.moubry.tomatoratings.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.moubry.tomatoratings.R;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

	
public class WebServiceHelper {

	public static final String TAG = "WebServiceHelper";
	
	public String result;
	public String errorMessage;
	
	private String endpoint;
	private Map<String, String> params;
	private Context context;
	
	public WebServiceHelper(Context ctx, String url, Map<String, String> params)
	{		
		this.context = ctx;
		this.endpoint = url;
		
		this.params = params;
		
//		if (this.params == null)
//			this.params = new HashMap<String, String>();
			
		String account = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
		
		Log.d(TAG, "jaccount before = " + account);
		
		if(account == null)
			account = "Unknown: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + ", " + android.os.Build.VERSION.RELEASE;
		
		Log.d(TAG, "jaccount = " + account);
		
		params.put("account", account);
		
		CallWebService();
	}
		
	public void CallWebService()
	{
		// Initialize error message.
		this.errorMessage = null;
		
		// Call original web service endpoint
		this.result = new WebService(this.endpoint).webGet("", params);
		
		if(this.result == null)
		{
			this.errorMessage = this.context.getString(R.string.connection_error);
			return;
		}
		
		Pattern patternForErrorMessage = Pattern.compile("<h1>Error Message:(.*)</h1>");
		Matcher matcherForErrorMessage = patternForErrorMessage.matcher(this.result.trim());
		
		if (matcherForErrorMessage.find() && matcherForErrorMessage.groupCount() > 0)
			this.errorMessage = matcherForErrorMessage.group(1).trim();
		
		Log.d(TAG, "Jami: " + result);
	}
}