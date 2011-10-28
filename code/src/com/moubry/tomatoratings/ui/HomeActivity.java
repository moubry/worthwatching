package com.moubry.tomatoratings.ui;

import com.moubry.tomatoratings.R;
import com.moubry.tomatoratings.util.UIUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class HomeActivity extends BaseActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		getActivityHelper().setupActionBar(null);
		
		 // Attach event handlers
        this.findViewById(R.id.home_btn_box_office).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {        
//                if (UIUtils.isHoneycombTablet(this)) {
                  //  startActivity(new Intent(this, ScheduleMultiPaneActivity.class));
//                } else {
                    startActivity(new Intent(view.getContext(), MovieListBoxOfficeActivity.class));
//                }
            }
        });
        
        this.findViewById(R.id.home_btn_in_theaters).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {        
//                if (UIUtils.isHoneycombTablet(this)) {
                  //  startActivity(new Intent(this, ScheduleMultiPaneActivity.class));
//                } else {
                    startActivity(new Intent(view.getContext(), MovieListInTheatersActivity.class));
//                }
            }
        });
        
        this.findViewById(R.id.home_btn_opening).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {        
//                if (UIUtils.isHoneycombTablet(this)) {
                  //  startActivity(new Intent(this, ScheduleMultiPaneActivity.class));
//                } else {
                    startActivity(new Intent(view.getContext(), MovieListOpeningActivity.class));
//                }
            }
        });        
        
        this.findViewById(R.id.home_btn_dvds).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {        
//                if (UIUtils.isHoneycombTablet(this)) {
                  //  startActivity(new Intent(this, ScheduleMultiPaneActivity.class));
//                } else {
            		startActivity(new Intent(view.getContext(), MovieListDVDReleasesActivity.class));
//              }
            }
        });
        
        this.findViewById(R.id.home_btn_search).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {        
//                if (UIUtils.isHoneycombTablet(this)) {
                  //  startActivity(new Intent(this, ScheduleMultiPaneActivity.class));
//                } else {
            		startSearch(null, false, Bundle.EMPTY, false);
//                }
            }
        });
        
        this.findViewById(R.id.home_btn_settings).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {        
            	startActivity(new Intent(view.getContext(), SettingsActivity.class));
            }
        });
	}    	
}