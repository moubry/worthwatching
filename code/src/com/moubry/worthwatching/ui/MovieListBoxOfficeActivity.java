package com.moubry.worthwatching.ui;

import java.util.List;

import com.moubry.worthwatching.CategorizedListItem;
import com.moubry.worthwatching.MovieAdapter;
import com.moubry.worthwatching.R;

public class MovieListBoxOfficeActivity extends MovieListBaseActivity {

	@Override
	protected String getListName() {
		
		return "/movies/box_office";
	}
	
	@Override
	protected void categorizeItemsAndSetList(List<CategorizedListItem> uncategorized) {
	
		//getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, uncategorized, false));
	}
}