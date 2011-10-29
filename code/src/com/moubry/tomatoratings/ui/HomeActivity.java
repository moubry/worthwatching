package com.moubry.tomatoratings.ui;

import com.moubry.tomatoratings.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
                    startActivity(new Intent(view.getContext(), MovieListUpcomingActivity.class));
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
        

        if (getIntent().getExtras().getBoolean("com.moubry.show_whats_new"))
        {
            BaseActivity.createWhatsNewAlert(this).show();
        }
	}    	
}