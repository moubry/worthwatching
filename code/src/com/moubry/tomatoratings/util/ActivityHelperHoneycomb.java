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

package com.moubry.tomatoratings.util;

import com.moubry.tomatoratings.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

/**
 * An extension of {@link ActivityHelper} that provides Android 3.0-specific functionality for
 * Honeycomb tablets. It thus requires API level 11.
 */
public class ActivityHelperHoneycomb extends ActivityHelper {
    private Menu mOptionsMenu;

    protected ActivityHelperHoneycomb(Activity activity) {
        super(activity);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        // Do nothing in onPostCreate. ActivityHelper creates the old action bar, we don't
        // need to for Honeycomb.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
//        mActivity.getMenuInflater().inflate(R.menu.default_menu_items, menu);
////        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
////        
////        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
////        
////        Log.i("jami", "component name=:" +(searchManager.getSearchableInfo(mActivity.getComponentName()) == null));
////        
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
//        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the HOME / UP affordance. Since the app is only two levels deep
                // hierarchically, UP always just goes home.
                goHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void setupHomeActivity() {
        super.setupHomeActivity();
        // NOTE: there needs to be a content view set before this is called, so this method
        // should be called in onPostCreate.
        mActivity.getActionBar().setDisplayOptions(
                0,
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    }

    /** {@inheritDoc} */
    @Override
    public void setupSubActivity() {
        super.setupSubActivity();
        // NOTE: there needs to be a content view set before this is called, so this method
        // should be called in onPostCreate.
        mActivity.getActionBar().setDisplayOptions(
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO,
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
    }

    /**
     * No-op on Honeycomb. The action bar title always remains the same.
     */
    @Override
    public void setActionBarTitle(CharSequence title) {
    }

//    /** {@inheritDoc} */
//    @Override
//    public void setRefreshActionButtonCompatState(boolean refreshing) {
//        // On Honeycomb, we can set the state of the refresh button by giving it a custom
//        // action view.
//        if (mOptionsMenu == null) {
//            return;
//        }
//
//        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
//        if (refreshItem != null) {
//            if (refreshing) {
//                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
//            } else {
//                refreshItem.setActionView(null);
//            }
//        }
//    }
}