package com.moubry.tomatoratings.ui;

import java.util.ArrayList;
import java.util.List;

import com.moubry.rottentomatoesapi.Movie;
import com.moubry.tomatoratings.CategorizedListItem;
import com.moubry.tomatoratings.CategoryListItem;
import com.moubry.tomatoratings.MovieAdapter;
import com.moubry.tomatoratings.R;

public class MovieListUpcomingActivity extends MovieListBaseActivity {

	@Override
	protected String getListName() {

		return "/movies/upcoming";
	}

	@Override
	protected void categorizeItemsAndSetList(List<CategorizedListItem> uncategorized) {
		List<CategorizedListItem> categorized = new ArrayList<CategorizedListItem>();

		String category = "";
		String nextCategory;
		CategorizedListItem item;
		for (int i = 0; i < uncategorized.size(); i++) {
			item = uncategorized.get(i);
			nextCategory = ((Movie) item).release_dates.getTheaterReleaseDate();

			if (!category.equals(nextCategory))
				categorized.add(new CategoryListItem(nextCategory));

			category = nextCategory;

			categorized.add(item);
		}

		super.categorizeItemsAndSetList(categorized);
	}
}