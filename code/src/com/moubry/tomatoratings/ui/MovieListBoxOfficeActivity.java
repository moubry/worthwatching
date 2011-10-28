package com.moubry.tomatoratings.ui;

import java.util.List;

import com.moubry.tomatoratings.CategorizedListItem;
import com.moubry.tomatoratings.MovieAdapter;
import com.moubry.tomatoratings.R;

public class MovieListBoxOfficeActivity extends MovieListBaseActivity {

	@Override
	protected String getListName() {
		
		return "/movies/box_office";
	}
	
	@Override
	protected void categorizeItemsAndSetList(List<CategorizedListItem> uncategorized) {
	
		getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, uncategorized, false));
	}
}