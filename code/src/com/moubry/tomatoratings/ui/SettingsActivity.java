package com.moubry.tomatoratings.ui;

import com.moubry.tomatoratings.R;
import com.moubry.tomatoratings.MovieTitleSuggestionsProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.SearchRecentSuggestions;
import android.provider.Settings.Secure;

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {    
	    super.onCreate(savedInstanceState);       
	    addPreferencesFromResource(R.xml.preferences);   
	    
	    this.findPreference(getString(R.string.pref_key_about)).setSummary(getAppNameWithVersion());	    
	    this.findPreference(getString(R.string.pref_key_about)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {

				new AlertDialog.Builder( SettingsActivity.this )
	      	      .setTitle(getAppNameWithVersion())
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
				body.append(getAppNameWithVersion());
				
				body.append("\nDevice: ");
				body.append(android.os.Build.MANUFACTURER + " ");
				body.append(android.os.Build.MODEL + ", ");
				body.append(android.os.Build.VERSION.RELEASE);

				body.append("\nUID: " + Secure.getString(getContentResolver(), Secure.ANDROID_ID));

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
			        			   MovieTitleSuggestionsProvider.AUTHORITY, MovieTitleSuggestionsProvider.MODE);
			        		suggestions.clearHistory();
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
	
	public String getAppNameWithVersion()
	{
		PackageInfo mgr;
		try {
			mgr = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return getString(R.string.app_name);
		}
		
		return getString(R.string.app_name) + " " + mgr.versionName;
	}

}
