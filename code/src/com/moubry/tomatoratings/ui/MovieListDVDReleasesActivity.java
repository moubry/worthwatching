package com.moubry.tomatoratings.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.moubry.rottentomatoesapi.Movie;
import com.moubry.tomatoratings.CategorizedListItem;
import com.moubry.tomatoratings.CategoryListItem;
import com.moubry.tomatoratings.MovieAdapter;
import com.moubry.tomatoratings.R;

public class MovieListDVDReleasesActivity extends MovieListBaseActivity {

	@Override
	protected String getListName() {
		
		return "/dvds/new_releases";
	}
	
	@Override
	protected void categorizeItemsAndSetList(List<CategorizedListItem> uncategorized) {
		List<CategorizedListItem> categorized = new ArrayList<CategorizedListItem>();

		
		Collections.sort(uncategorized, new CategorizedListItemComparableByDvdRelease());
		
		String category = "";
		String nextCategory;
		CategorizedListItem item;
		for (int i = 0; i < uncategorized.size(); i++) {
			item = uncategorized.get(i);
			nextCategory = ((Movie) item).release_dates.getDvdReleaseDate();
			
			// Only show ones with valid dvd release dates
			if(nextCategory != null && nextCategory.length() > 0)
			{
				if (!category.equals(nextCategory))
					categorized.add(new CategoryListItem(nextCategory));
	
				category = nextCategory;
	
				categorized.add(item);
			}
		}		

		getListView().setAdapter(new MovieAdapter(this, R.layout.list_item, categorized, false));
	}
	
	public class CategorizedListItemComparableByDvdRelease implements Comparator<CategorizedListItem>{
		 
	    @Override
	    public int compare(CategorizedListItem o1, CategorizedListItem o2) {
	    	String s1 = ((Movie) o1).release_dates.dvd;
	    	
	    	String s2 = ((Movie) o2).release_dates.dvd;
	    	
	    	if(s1 == null)
	    		s1 = "";
	    	
	    	if(s2 == null)
	    		s2 = "";
	    	
	    	return s1.compareTo(s2);
	    }
	}
}