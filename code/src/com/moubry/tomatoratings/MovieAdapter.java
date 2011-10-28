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
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Movie> {

	int resource;
	String response;
	Context context;

	// Initialize adapter
	public MovieAdapter(Context context, int resource, List<Movie> items) {
		super(context, resource, items);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout movieView;
		
		// Get the current movie object
		Movie movie = getItem(position);

		// Inflate the view
		if (convertView == null) {
			movieView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, movieView, true);
		} else {
			movieView = (LinearLayout) convertView;
		}

		// Get the text boxes from the list_item.xml file
		ImageView movieTomato = (ImageView) movieView
				.findViewById(R.id.tomato_image);
		TextView movieRating = (TextView) movieView
				.findViewById(R.id.txtRating);
		TextView movieTitle = (TextView) movieView.findViewById(R.id.txtTitle);

		// Assign the appropriate data from our movie object above
		movieTomato.setImageResource(movie.getTomatoImageResource());
		movieRating.setText(movie.ratings.getFormattedCriticsScore());
		movieTitle.setText(movie.getTitleWithYear());

		return movieView;
	}
}