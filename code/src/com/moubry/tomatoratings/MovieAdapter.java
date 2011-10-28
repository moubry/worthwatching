package com.moubry.tomatoratings;

import java.util.List;

import com.moubry.tomatoratings.R;

import com.moubry.rottentomatoesapi.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<CategorizedListItem> {

	private boolean showYearInTitle;
	private LayoutInflater vi;

	// Initialize adapter
	public MovieAdapter(Context context, int resource, List<CategorizedListItem> items, boolean showYear) {
		super(context, 0, items);
		this.showYearInTitle = showYear;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		// Get the current movie object
		final CategorizedListItem item = getItem(position);
		
		if(item != null)
		{
			if(item.isSection()){
				
				CategoryListItem si = (CategoryListItem)item;
				v = vi.inflate(R.layout.list_item_category, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);
				
				final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			}
			else
			{
				Movie movie = (Movie)item;
				v = vi.inflate(R.layout.list_item, null);
		
				// Get the text boxes from the list_item.xml file
				final ImageView movieTomato = (ImageView) v.findViewById(R.id.tomato_image);
				final TextView movieRating = (TextView) v.findViewById(R.id.txtRating);
				final ImageView movieAudTomato = (ImageView) v.findViewById(R.id.audience_tomato_image);
				final TextView movieAudRating = (TextView) v.findViewById(R.id.txtAudienceRating);
				final TextView movieTitle = (TextView) v.findViewById(R.id.txtTitle);
		
				// Assign the appropriate data from our movie object above
				movieTomato.setImageResource(movie.getTomatoImageResource());
				movieRating.setText(movie.ratings.getFormattedCriticsScore());
				movieAudTomato.setImageResource(movie.getAudienceTomatoImageResource());
				movieAudRating.setText(movie.ratings.getFormattedAudienceScore());
				
				if(this.showYearInTitle)
					movieTitle.setText(movie.getTitleWithYear());
				else
					movieTitle.setText(movie.getTitle());
			}
		}
			
		return v;
	}
}