package com.moubry.tomatoratings;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.moubry.rottentomatoesapi.Movie;
import com.moubry.tomatoratings.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.widget.ImageView;

public class MovieRating extends Activity {

	private String imdbID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_rating);

		Bundle recdData = getIntent().getExtras();

		String title = recdData.getString("com.moubry.title");
		String critic_score = recdData.getString("com.moubry.rating");
		String audience_score = recdData.getString("com.moubry.audience_rating");
		String critic_tomato_image = recdData.getString("com.moubry.tomato_image");
		String audience_tomato_image = recdData.getString("com.moubry.audience_tomato_image");
		String consensus = recdData.getString("com.moubry.consensus");

		String cast = recdData.getString("com.moubry.cast");
		String rating_runtime = recdData.getString("com.moubry.rating_runtime");
		String release_date = recdData.getString("com.moubry.release_date");
		String poster = recdData.getString("com.moubry.poster");
		String synopsis = recdData.getString("com.moubry.synopsis");

		imdbID = recdData.getString("com.moubry.imdb_id");

		((TextView) findViewById(R.id.title)).setText(title);
		((TextView) findViewById(R.id.android_tomatometer_score)).setText(critic_score);
		((TextView) findViewById(R.id.android_audience_tomatometer_score)).setText(audience_score);
		((ImageView) findViewById(R.id.tomato_image)).setImageResource(Integer.parseInt(critic_tomato_image));
		((ImageView) findViewById(R.id.audience_tomato_image)).setImageResource(Integer.parseInt(audience_tomato_image));
		((TextView) findViewById(R.id.android_consensus)).setText(consensus);

		((TextView) findViewById(R.id.cast)).setText(cast);
		((TextView) findViewById(R.id.rating_runtime)).setText(rating_runtime);
		((TextView) findViewById(R.id.release_date)).setText(release_date);

		((TextView) findViewById(R.id.synopsis)).setText(synopsis);

		ImageView img = (ImageView) findViewById(R.id.imdb);
		
		if (imdbID != null && imdbID.length() > 0) {
		    img.setVisibility(View.VISIBLE);	
			img.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("http://www.imdb.com/title/tt"
							+ imdbID));
					startActivity(intent);
				}
			});
		}
		else
		{
			img.setVisibility(View.INVISIBLE);
		}

		GetMoviePosterTask task = new GetMoviePosterTask();
		task.setImageId(R.id.movie_poster);
		task.execute(poster);
	}



	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		try
		{
		if(current != null && !current.isRecycled())
			current.recycle();
		}
		catch(Exception e)
		{
			Log.e("Jami", e.getMessage());
		}
	}

	public static void AddMovieDataToIntent(Intent intent, Movie movie) {
		intent.putExtra("com.moubry.rating", movie.ratings.getFormattedCriticsScore());
		intent.putExtra("com.moubry.audience_rating", movie.ratings.getFormattedAudienceScore());
		intent.putExtra("com.moubry.consensus", movie.critics_consensus);
		intent.putExtra("com.moubry.tomato_image", String.valueOf(movie.getTomatoImageResource()));
		intent.putExtra("com.moubry.audience_tomato_image", String.valueOf(movie.getAudienceTomatoImageResource()));
		intent.putExtra("com.moubry.imdb_id", movie.alternate_ids == null ? null : movie.alternate_ids.imdb);
		intent.putExtra("com.moubry.poster", movie.posters == null ? null : movie.posters.detailed);
		intent.putExtra("com.moubry.title", movie.title);

		StringBuilder sbRatingAndRuntime = new StringBuilder();
		sbRatingAndRuntime.append(movie.mpaa_rating);

		if (movie.getFormattedRuntime() != null && movie.getFormattedRuntime().length() > 0)
		{
			sbRatingAndRuntime.append(", ");
			sbRatingAndRuntime.append(movie.getFormattedRuntime());
		}
		
		intent.putExtra("com.moubry.rating_runtime", sbRatingAndRuntime.toString());
		intent.putExtra("com.moubry.cast", movie.getAbridgedCast());
		intent.putExtra("com.moubry.synopsis", movie.synopsis);
		intent.putExtra("com.moubry.release_date",movie.release_dates == null ? null : movie.release_dates.getTheaterReleaseDate());
	}

	private Bitmap current;

	private class GetMoviePosterTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... posterURL) {

			Bitmap bmp = null;
			try {
				Log.i("INfo ", "Poster: " + posterURL[0]);

				URL ulrn = new URL(posterURL[0]);
				HttpURLConnection con = (HttpURLConnection) ulrn
						.openConnection();
				InputStream is = con.getInputStream();
				bmp = BitmapFactory.decodeStream(is);
				is.close();

			} catch (Exception e) {
				Log.e("Error ", "Exception: " + e.getMessage());
			}

			return bmp;
		}

		private int imageViewID;

		public void setImageId(int imageViewID) {
			this.imageViewID = imageViewID;
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null)
				setImage(imageViewID, result);
			else
				Log.i("Info ", "Null Bitmap");
		}

		private void setImage(int imageViewID2, Bitmap result) {
			MovieRating.this.current = result;
			((ImageView) MovieRating.this.findViewById(imageViewID2))
					.setImageBitmap(result);
		}
	}
}
