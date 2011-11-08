package com.moubry.worthwatching.ui;

import com.moubry.worthwatching.R;
import com.moubry.worthwatching.util.LicenseCheckActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends BaseActivity
{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);    
        
        
        
        String acceptedVersion = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.pref_key_accepted_terms_for_version), "");
        
        if (acceptedVersion.equals(getString(R.string.app_version)))
        {
            startNextActivity(false);
        }
        else
        {
            setContentView(R.layout.activity_terms);
        }
    }
    
    protected void startNextActivity(boolean showWhatsNew)
    {
        String whatsnew = getString(R.string.whats_new_message);
        
        if((whatsnew == null) || whatsnew.length() == 0)
            showWhatsNew = false;
        
        String activityID = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.pref_key_start_screen), "0");

        switch (Integer.parseInt(activityID))
        {
            case 1:
                startActivity(new Intent(this, MovieListDVDReleasesActivity.class).putExtra("com.moubry.show_whats_new", showWhatsNew));
                break;
            case 2:
                startActivity(new Intent(this, MovieListInTheatersActivity.class).putExtra("com.moubry.show_whats_new", showWhatsNew));
                break;
            case 3:
                startActivity(new Intent(this, MovieListUpcomingActivity.class).putExtra("com.moubry.show_whats_new", showWhatsNew));
                break;
            case 4:
                startActivity(new Intent(this, MovieListBoxOfficeActivity.class).putExtra("com.moubry.show_whats_new", showWhatsNew));
                break;
            case 0:
            default:
                startActivity(new Intent(this, HomeActivity.class).putExtra("com.moubry.show_whats_new", showWhatsNew));
                break;
        }

        finish();
    }
    
    public void cancel_click(View v)
    {
        finish();
    }
    
    public void agree_click(View v)
    {       
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
     
        prefEditor.putString(getString(R.string.pref_key_accepted_terms_for_version), getString(R.string.app_version));
        prefEditor.commit();
        
        startNextActivity(true);
    }

}