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

	public String result;
	public String errorMessage;
	
	private String endpoint;
	private String endpointBackup;
	private Map<String, String> params;
	private Context context;
	
	public WebServiceHelper(Context ctx, String url, String backupUrl, Map<String, String> params)
	{		
		this.context = ctx;
		this.endpoint = url;
		this.endpointBackup = backupUrl;
		
		this.params = params;
		
//		if (this.params == null)
//			this.params = new HashMap<String, String>();
			
		String account = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
		
		Log.d("Jami", "jaccount before = " + account);
		
		if(account == null)
			account = "Unknown: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + ", " + android.os.Build.VERSION.RELEASE;
		
		Log.d("Jami", "jaccount = " + account);
		
		params.put("account", account);
		
		CallWebService();
	}
		
	public void CallWebService()
	{
		// Initialize error message.
		this.errorMessage = null;
		
		this.result = new WebService(this.endpoint).webGet("", params);
		
		// try the backup url
		if ((this.result == null) || (this.result.startsWith("<h1>")))
		{
			Log.d("Jami", "using backup search url");
			params.put("apikey", this.context.getString(R.string.apikey));
			this.result = new WebService(this.endpointBackup).webGet("", params);
		}
		

		Pattern pattern = Pattern.compile("^<h1>Error Message:(.*)</h1>$");
		Matcher matcher = pattern.matcher(result);

		if(this.result == null)
			this.errorMessage = this.context.getString(R.string.connection_error);
		else if (matcher.find() && matcher.groupCount() > 1)
			this.errorMessage = matcher.group(1).trim();
		else if (result.startsWith("<h1>"))
			this.errorMessage = this.context.getString(R.string.unknown_service_error);
		
		Log.d("Jami", "Jami: " + result);
	}
}