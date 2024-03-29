/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moubry.worthwatching.ui;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.util.ActionBarActivity;
import com.moubry.worthwatching.util.AnalyticsUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A base activity that defers common functionality across app activities to an
 * {@link ActivityHelper}. This class shouldn't be used directly; instead,
 * activities should inherit from {@link BaseSinglePaneActivity} or
 * {@link BaseMultiPaneActivity}.
 */
public abstract class BaseActivity extends ActionBarActivity
{
    private boolean mAlternateTitle = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tracker = GoogleAnalyticsTracker.getInstance();
        AnalyticsUtils.StartTrackingSession(getApplicationContext(), tracker);

        findViewById(R.id.toggle_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlternateTitle) {
                    setTitle(R.string.app_name);
                } else {
                    setTitle(R.string.alternate_title);
                }
                mAlternateTitle = !mAlternateTitle;
            }
        });
        
    }

    public static AlertDialog createWhatsNewAlert(Context context)
    {
        final TextView message = new TextView(context);
        final SpannableString s = new SpannableString(context.getText(R.string.whats_new_message));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        message.setPadding(10, 10, 10, 10);
        message.setText(s);
        message.setLinkTextColor(context.getResources().getColor(R.color.blue));
        message.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        return new AlertDialog.Builder(context).setTitle(R.string.title_whats_new)
                .setCancelable(true).setPositiveButton("OK", null).setView(message).create();
    }

  //  final ActivityHelper             mActivityHelper = ActivityHelper.createInstance(this);

    protected GoogleAnalyticsTracker tracker;


    protected void showWhatsNewIfEnabled()
    {
        Bundle recdData = getIntent().getExtras();
        if ((recdData != null) && recdData.getBoolean("com.moubry.show_whats_new"))
        {
            BaseActivity.createWhatsNewAlert(this).show();
        }
    }

    protected void showAdsIfEnabled()
    {
        try
        {
            if (Boolean.parseBoolean(getString(R.string.show_ads)))
            {
                // Look up the AdView as a resource and load a request.
                AdView adView = (AdView) this.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest();
                
                adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
                
                adView.loadAd(adRequest);
            }
        }
        catch(Exception ex)
        {
            Log.e("BaseActivity", "ad exception. " + ex.getMessage());
        }
    }

    protected String getPageNameForTracker()
    {
        return "/" + this.getLocalClassName();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        tracker.trackPageView(getPageNameForTracker());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        tracker.stopSession();
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState)
//    {
//        super.onPostCreate(savedInstanceState);
//        mActivityHelper.onPostCreate(savedInstanceState);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        return mActivityHelper.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                getActionBarHelper().setRefreshActionItemState(false);
                            }
                        }, 1000);
                break;

            case R.id.menu_search:
                startSearch(null, false, Bundle.EMPTY, false);
                break;
                
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
                
            case R.id.menu_home:
                startActivity(new Intent(this, HomeActivity.class));
                break;                
        }
        return super.onOptionsItemSelected(item);
    }

//    /**
//     * Returns the {@link ActivityHelper} object associated with this activity.
//     */
//    protected ActivityHelper getActivityHelper()
//    {
//        return mActivityHelper;
//    }

//    /**
//     * Takes a given intent and either starts a new activity to handle it (the
//     * default behavior), or creates/updates a fragment (in the case of a
//     * multi-pane activity) that can handle the intent.
//     * 
//     * Must be called from the main (UI) thread.
//     */
//    public void openActivityOrFragment(Intent intent)
//    {
//        // Default implementation simply calls startActivity
//        startActivity(intent);
//    }

//    /**
//     * Converts an intent into a {@link Bundle} suitable for use as fragment
//     * arguments.
//     */
//    public static Bundle intentToFragmentArguments(Intent intent)
//    {
//        Bundle arguments = new Bundle();
//        if (intent == null)
//        {
//            return arguments;
//        }
//
//        final Uri data = intent.getData();
//        if (data != null)
//        {
//            arguments.putParcelable("_uri", data);
//        }
//
//        final Bundle extras = intent.getExtras();
//        if (extras != null)
//        {
//            arguments.putAll(intent.getExtras());
//        }
//
//        return arguments;
//    }
//
//    /**
//     * Converts a fragment arguments bundle into an intent.
//     */
//    public static Intent fragmentArgumentsToIntent(Bundle arguments)
//    {
//        Intent intent = new Intent();
//        if (arguments == null)
//        {
//            return intent;
//        }
//
//        final Uri data = arguments.getParcelable("_uri");
//        if (data != null)
//        {
//            intent.setData(data);
//        }
//
//        intent.putExtras(arguments);
//        intent.removeExtra("_uri");
//        return intent;
//    }
}