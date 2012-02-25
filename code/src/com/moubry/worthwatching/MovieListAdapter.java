package com.moubry.worthwatching;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieListAdapter extends ArrayAdapter<com.moubry.worthwatching.api.List> {

	private LayoutInflater vi;

	// Initialize adapter
	public MovieListAdapter(Context context, int resource, List<com.moubry.worthwatching.api.List> items) {
		super(context, 0, items);
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		// Get the current list object
		final com.moubry.worthwatching.api.List item = getItem(position);
		
		if(item != null)
		{
			v = vi.inflate(R.layout.list_list_item, null);
	
			// Get the text boxes from the list_item.xml file
			final TextView movieRating = (TextView) v.findViewById(R.id.txtName);
			movieRating.setText(item.Name);
		}

		return v;
	}
}