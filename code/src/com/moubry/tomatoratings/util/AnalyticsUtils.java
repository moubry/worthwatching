package com.moubry.tomatoratings.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.moubry.tomatoratings.R;

public class AnalyticsUtils {
	private static final int APP_VERSION_SLOT = 1;
	private static final String APP_VERSION_LABEL = "App Version";
	
	private static final int ANDROID_VERSION_SLOT = 2;
	private static final String ANDROID_VERSION_LABEL = "Android Version";
    
	private static final int DEVICE_SLOT = 3;
	private static final String DEVICE_LABEL = "Device";
	
	private static final int SCREEN_ORIENTATION_SLOT = 4;
	private static final String SCREEN_ORIENTATION_LABEL = "Screen Orientation";
	
	private static final int APP_STORE_SLOT = 5;
	private static final String APP_STORE_LABEL = "App Store";
	
    public static void StartTrackingSession(Context ctx, GoogleAnalyticsTracker tracker)
    {
	    tracker.startNewSession(ctx.getString(R.string.analytics_account), ctx);
//	    tracker.setDebug(true);
//	    tracker.setDryRun(true);

	    tracker.setCustomVar(APP_VERSION_SLOT, APP_VERSION_LABEL, getAppVersion(ctx), 2);
		tracker.setCustomVar(ANDROID_VERSION_SLOT, ANDROID_VERSION_LABEL, android.os.Build.VERSION.RELEASE, 2);
		tracker.setCustomVar(DEVICE_SLOT, DEVICE_LABEL, android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL, 1);
		tracker.setCustomVar(SCREEN_ORIENTATION_SLOT, SCREEN_ORIENTATION_LABEL, getOrientation(ctx), 2);
		tracker.setCustomVar(APP_STORE_SLOT, APP_STORE_LABEL, ctx.getString(R.string.app_store), 2);
    }

	private static String getOrientation(Context ctx)
	{
	    String orientation = "unknown";
	    
	    switch(ctx.getResources().getConfiguration().orientation){
	    case Configuration.ORIENTATION_LANDSCAPE:
	    	orientation = "landscape";
	    	break;
	    case Configuration.ORIENTATION_PORTRAIT:
	    	orientation = "portrait";
	    	break;
	    }
	    
	    return orientation;
	}
	
	private static String getAppVersion(Context ctx)
	{
		PackageInfo mgr;
		try {
			mgr = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return "unknown";
		}
		
		return mgr.versionName;
	}
}
