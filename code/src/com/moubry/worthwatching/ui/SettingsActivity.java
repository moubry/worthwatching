package com.moubry.worthwatching.ui;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.moubry.worthwatching.WWMovieTitleSuggestionsProvider;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.util.AnalyticsUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.SearchRecentSuggestions;
import android.provider.Settings.Secure;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {
	
	private GoogleAnalyticsTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {    
	    super.onCreate(savedInstanceState);       

        addPreferencesFromResource(R.xml.preferences);  
	    
	    tracker = GoogleAnalyticsTracker.getInstance();
	    AnalyticsUtils.StartTrackingSession(getApplicationContext(), tracker);

	    this.findPreference(getString(R.string.pref_key_about)).setSummary(AnalyticsUtils.getAppNameWithVersion(getApplicationContext()));	    

	    this.findPreference(getString(R.string.pref_key_about)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {

				new AlertDialog.Builder( SettingsActivity.this )
	      	      .setTitle(AnalyticsUtils.getAppNameWithVersion(getApplicationContext()))
	      	      .setMessage( getString(R.string.flixster_notice) )
	      	      .show();
				
				return false;
			}
		});
	    
	    this.findPreference(getString(R.string.pref_key_feedback)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				StringBuilder body = new StringBuilder();
				body.append("\n\n----------------");
				
				body.append("\nVersion: ");
				body.append(AnalyticsUtils.getAppNameWithVersion(getApplicationContext()));
				
				body.append("\nDevice: ");
				body.append(android.os.Build.MANUFACTURER + " ");
				body.append(android.os.Build.MODEL + ", ");
				body.append(android.os.Build.VERSION.RELEASE);

				  Intent i = new Intent(Intent.ACTION_SEND);
				    i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.support_email_address) });
				    i.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " Feedback");
				    i.putExtra(android.content.Intent.EXTRA_TEXT, body.toString());
				    i.setType("plain/text");
				    startActivity(Intent.createChooser(i, "Send Email"));
				
				return false;
			}
		});
	    
	    this.findPreference(getString(R.string.pref_key_clear_search_history)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {

				new AlertDialog.Builder( SettingsActivity.this )
	      	      .setTitle("Clear")
	      	      .setMessage( getString(R.string.message_clear_history) )
	      	      .setCancelable(true)
	      	      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   SearchRecentSuggestions suggestions = new SearchRecentSuggestions(SettingsActivity.this,
			                       WWMovieTitleSuggestionsProvider.GetAuthority(Boolean.parseBoolean(getString(R.string.show_ads))),
			                       WWMovieTitleSuggestionsProvider.MODE);
			        		suggestions.clearHistory();
			        		
			        		tracker.trackEvent("ui_interaction", // category
			        						   "clear",          // action
			        						   "Search History", // label
			        						   0);               // value
			           }
	      	      })
				  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int id) {
						  dialog.cancel();
					}
				  })
	      	      .show();
				
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		tracker.trackPageView("/" + this.getLocalClassName()); 
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		tracker.stopSession();
	}
}
