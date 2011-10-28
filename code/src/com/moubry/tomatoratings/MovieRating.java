package com.moubry.tomatoratings;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import com.moubry.rottentomatoesapi.Movie;
import com.moubry.rottentomatoesapi.MovieSearchResult;
import com.moubry.rottentomatoesapi.Review;
import com.moubry.tomatoratings.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.ImageView;

public class MovieRating extends Activity {

	private String imdbID;
	private String rottenTomatoesURL;
	private String reviewsURL;

	public void loadData() {
		Bundle recdData = getIntent().getExtras();
		
		String title = recdData.getString("com.moubry.title");
		String critic_score = recdData.getString("com.moubry.rating");
		String audience_score = recdData.getString("com.moubry.audience_rating");
		String critic_tomato_image = recdData.getString("com.moubry.tomato_image");
		String audience_tomato_image = recdData.getString("com.moubry.audience_tomato_image");
		String consensus = recdData.getString("com.moubry.consensus");
        String mpaa_rating_image = recdData.getString("com.moubry.mpaa_rating_image");
		String cast = recdData.getString("com.moubry.cast");
		String runtime = recdData.getString("com.moubry.runtime");
		String release_date = recdData.getString("com.moubry.release_date");
		String synopsis = recdData.getString("com.moubry.synopsis");
		this.reviewsURL = recdData.getString("com.moubry.reviews_url");
        this.rottenTomatoesURL = recdData.getString("com.moubry.rotten_tomatoes_url");
		this.imdbID = recdData.getString("com.moubry.imdb_id");


		((TextView) findViewById(R.id.title)).setText(title);
		((TextView) findViewById(R.id.android_tomatometer_score)).setText(critic_score);
		((TextView) findViewById(R.id.android_audience_tomatometer_score)).setText(audience_score);
		((ImageView) findViewById(R.id.tomato_image)).setImageResource(Integer.parseInt(critic_tomato_image));
		((ImageView) findViewById(R.id.audience_tomato_image)).setImageResource(Integer.parseInt(audience_tomato_image));
		((TextView) findViewById(R.id.android_consensus)).setText(consensus);
		
		if(mpaa_rating_image == null)
			findViewById(R.id.mpaa_rating).setVisibility(View.GONE);
		else
		{
			findViewById(R.id.mpaa_rating).setVisibility(View.VISIBLE);
			((ImageView) findViewById(R.id.mpaa_rating)).setImageResource(Integer.parseInt(mpaa_rating_image));
		}
		// org.apache.commons.lang.
		((TextView) findViewById(R.id.cast)).setText(cast);
		((TextView) findViewById(R.id.runtime)).setText(runtime);
		((TextView) findViewById(R.id.release_date)).setText(release_date);

		((TextView) findViewById(R.id.synopsis)).setText(synopsis);

		this.posterURL = recdData.getString("com.moubry.poster");

		ImageView imgRT = (ImageView) findViewById(R.id.rottentomatoes);

		if (rottenTomatoesURL != null && rottenTomatoesURL.length() > 0) {
			imgRT.setVisibility(View.VISIBLE);
			imgRT.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(rottenTomatoesURL));
					startActivity(intent);
				}
			});
			
			
			findViewById(R.id.movie_poster).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(rottenTomatoesURL));
					startActivity(intent);
				}
			});
			
			findViewById(R.id.android_scores_container).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(rottenTomatoesURL));
					startActivity(intent);					
				}
			});
		} else {
			imgRT.setVisibility(View.INVISIBLE);
		}
		
		ImageView img = (ImageView) findViewById(R.id.imdb);

		if (imdbID != null && imdbID.length() > 0) {
			img.setVisibility(View.VISIBLE);
			img.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("http://www.imdb.com/title/tt" + imdbID));
					startActivity(intent);
				}
			});
		} else {
			img.setVisibility(View.INVISIBLE);
		}
	}

	private void loadPoster() {

		GetMoviePosterTask task = new GetMoviePosterTask();
		task.setImageId(R.id.movie_poster);
		task.execute(this.posterURL);
	}

	private String posterURL;

	private void loadReviews() {

		GetTopCriticReviewsTask task = new GetTopCriticReviewsTask();
		task.setLayoutViewID(R.id.layout);
		task.setProgressID(R.id.progressBarLayout);
		task.setArrowID(R.drawable.arrow);
		task.setNoReviewsID(R.id.no_reviews);
		task.execute(this.reviewsURL);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_rating);

		loadData();
		loadPoster();
		loadReviews();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			if (current != null && !current.isRecycled())
				current.recycle();
		} catch (Exception e) {
			Log.e("Jami", e.getMessage());
		}
	}

	private class GetTopCriticReviewsTask extends
			AsyncTask<String, Void, List<Review>> {

		private boolean hasConnectionError = false;

		private int progressID;
		private int arrowID;
        private int noReviewsID;
        
		// can use UI thread here
		protected void onPreExecute() {
			this.hasConnectionError = false;

			MovieRating.this.findViewById(this.noReviewsID).setVisibility(View.GONE);
			MovieRating.this.findViewById(this.progressID).setVisibility(View.VISIBLE);

		}

		public void setNoReviewsID(int noReviews) {
			this.noReviewsID = noReviews;
			
		}

		public void setArrowID(int arrow) {

			this.arrowID = arrow;
		}

		// can use UI thread here
		protected void onPostExecute(List<Review> result) {

			MovieRating.this.findViewById(this.progressID).setVisibility(View.GONE);

			// MovieRating.this.setListAdapter(new
			// MovieAdapter(MovieRating.this,
			// R.layout.list_item, result));

			// MovieRating.this.setListAdapter(new MyExpandableListAdapter());

			Log.i("INfo: ", String.valueOf(result.size()));

			if (hasConnectionError) {
				new AlertDialog.Builder(MovieRating.this)
						.setTitle("Internet Connection Required")
						.setMessage(
								"Tomato Ratings requires an internet connection.")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Log.d("AlertDialog", "Positive");
									}
								}).show();
			} else {

				if (result.size() > 0) {

					LayoutParams wrap = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					LayoutParams fillWidth = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

					LinearLayout reviewContainer = new LinearLayout(MovieRating.this);
					reviewContainer.setLayoutParams(fillWidth);
					reviewContainer.setOrientation(LinearLayout.VERTICAL);
					((LinearLayout) MovieRating.this.findViewById(this.layoutViewID)).addView(reviewContainer);
					
					LayoutParams l = new LayoutParams(LayoutParams.FILL_PARENT, 1);
					
					LinearLayout ruler = new LinearLayout(MovieRating.this);
					ruler.setLayoutParams(l);
					ruler.setBackgroundColor(0xFF737173);
					reviewContainer.addView(ruler);

					for (Review r : result) {

						LinearLayout reviewLayout = new LinearLayout(MovieRating.this);
						reviewLayout.setLayoutParams(fillWidth);
						
					    reviewLayout.setOrientation(LinearLayout.HORIZONTAL);
						reviewLayout.setPadding(10, 10, 0, 10);
						
						if(r.links != null && r.links.review != null && r.links.review.length() > 0)
						{	
							reviewLayout.setTag(r.links.review);
							reviewLayout.setOnClickListener(new View.OnClickListener() {

					          @Override
					          public void onClick(View v) {
					        	Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.addCategory(Intent.CATEGORY_BROWSABLE);
								intent.setData(Uri.parse((String)v.getTag()));
								startActivity(intent);
					          }
					      });
						}
						
						reviewContainer.addView(reviewLayout);
						
						
						LinearLayout.LayoutParams l3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT,1);
						
						LinearLayout criticContainer = new LinearLayout(MovieRating.this);
						criticContainer.setLayoutParams(l3);
						criticContainer.setOrientation(LinearLayout.VERTICAL);
					    reviewLayout.addView(criticContainer);
						
						LinearLayout criticLayout = new LinearLayout(MovieRating.this);
						criticLayout.setLayoutParams(wrap);
						criticLayout.setOrientation(LinearLayout.HORIZONTAL);
						
						criticContainer.addView(criticLayout);
						
						TextView tv = new TextView(MovieRating.this);
						tv.setLayoutParams(wrap);
						tv.setText(r.getCritic());
						tv.setTextColor(0xFFE8E8E8);
						tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
						criticLayout.addView(tv);
						
						if(r.getPublication() != null && r.getPublication().length() > 0)
						{
							TextView tv2 = new TextView(MovieRating.this);
							tv2.setLayoutParams(wrap);
							tv2.setText(", " + r.getPublication());
							criticLayout.addView(tv2);
						}

						tv = new TextView(MovieRating.this);
						tv.setLayoutParams(wrap);
						tv.setText(r.quote);
						criticContainer.addView(tv);
						
						
						LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 0);
						l2.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
						
						ImageView img = new ImageView(MovieRating.this);
						img.setLayoutParams(l2);
						img.setPadding(10, 0, 7, 0);
						
						if(r.links != null && r.links.review != null && r.links.review.length() > 0)
							img.setImageResource(this.arrowID);
						
						reviewLayout.addView(img);
						
						LinearLayout ruler2 = new LinearLayout(MovieRating.this);
						ruler2.setLayoutParams(l);
						ruler2.setBackgroundColor(0xFF737173);
						reviewContainer.addView(ruler2);
					}
				}
				else
					MovieRating.this.findViewById(this.noReviewsID).setVisibility(View.VISIBLE);
			}
		}

		private int layoutViewID;

		public void setLayoutViewID(int i) {
			this.layoutViewID = i;
		}

		public void setProgressID(int i) {
			this.progressID = i;
		}

		@Override
		protected List<Review> doInBackground(String... url) {

			List<Review> lstReviews = new ArrayList<Review>();

			// Instantiate the Web Service Class with he URL of the web service
			// not that you must pass

			Log.i("Info: ", "before webservice");

			WebService webService = new WebService(url[0]);

			Log.i("Info: ", "after webservice");

			// Pass the parameters if needed , if not then pass dummy one as
			// follows
			Map<String, String> params = new HashMap<String, String>();
			params.put("apikey", "cfuvchhe98amz6vd9qc2s8m7");
			params.put("review_type", "top_critic");

			// Get JSON response from server the "" are where the method name
			// would normally go if needed example
			// webService.webGet("getMoreAllerts", params);
			String response = webService.webGet("", params);

			Log.i("Info: ", "after webservice webGet");

			if (response == null) {
				this.hasConnectionError = true;
				return lstReviews;
			}

			try {
				// Parse Response into our object
				MovieSearchResult result = new Gson().fromJson(response,
						MovieSearchResult.class);

				for (int i = 0; i < result.reviews.length; i++) {
					lstReviews.add(result.reviews[i]);
				}
			} catch (Exception e) {
				Log.d("Error: ", e.getMessage());
			}

			return lstReviews;
		}
	}

	public static void AddMovieDataToIntent(Intent intent, Movie movie) {
		intent.putExtra("com.moubry.rating",
				movie.ratings.getFormattedCriticsScore());
		intent.putExtra("com.moubry.audience_rating",
				movie.ratings.getFormattedAudienceScore());
		intent.putExtra("com.moubry.consensus", movie.getCriticsConsensus());
		intent.putExtra("com.moubry.tomato_image",
				String.valueOf(movie.getTomatoImageResource()));
		intent.putExtra("com.moubry.audience_tomato_image",
				String.valueOf(movie.getAudienceTomatoImageResource()));
		intent.putExtra("com.moubry.imdb_id",
				movie.alternate_ids == null ? null : movie.alternate_ids.imdb);
		intent.putExtra("com.moubry.rotten_tomatoes_url", movie.links == null ? null : movie.links.alternate);
		intent.putExtra("com.moubry.poster", movie.posters == null ? null
				: movie.posters.detailed);
		intent.putExtra("com.moubry.title", movie.getTitle());
		intent.putExtra("com.moubry.reviews_url", movie.links == null ? null
				: movie.links.reviews);

		if(movie.mpaa_rating == null || movie.mpaa_rating.length() == 0)
			intent.putExtra("com.moubry.runtime", movie.getFormattedRuntime());
		else if(movie.getMPAARatingImageID() == null)
			intent.putExtra("com.moubry.runtime", movie.mpaa_rating + ", " + movie.getFormattedRuntime());
		else
		{
			intent.putExtra("com.moubry.runtime", movie.getFormattedRuntime());
			intent.putExtra("com.moubry.mpaa_rating_image", String.valueOf(movie.getMPAARatingImageID()));
		}
		
		intent.putExtra("com.moubry.cast", movie.getAbridgedCast());
		intent.putExtra("com.moubry.synopsis", movie.getSynopsis());
		intent.putExtra(
				"com.moubry.release_date",
				movie.release_dates == null ? null : movie.release_dates
						.getTheaterReleaseDate());
	}

	private static Bitmap current;

	private class GetMoviePosterTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... posterURL) {
			Bitmap bmp = null;
			try {
				Log.i("INfo ", "Poster: " + posterURL[0]);
				bmp = decodeFile(posterURL[0]);
			} catch (Exception e) {
				Log.e("Error ", "Exception: " + e.getMessage());
			}

			return bmp;
		}

		private final int IMAGE_MAX_WIDTH = 180;
		private final int IMAGE_MAX_HEIGHT = 266;

		private Bitmap decodeFile(String url) {
			Bitmap b = null;

			try {
				URL ulrn = new URL(url);

				// Decode image size
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;

				HttpURLConnection con = (HttpURLConnection) ulrn
						.openConnection();
				con.connect();

				InputStream fis1 = con.getInputStream();
				BitmapFactory.decodeStream(fis1, null, o);
				fis1.close();
				con.disconnect();

				int scale = 1;
				// if (o.outHeight > IMAGE_MAX_WIDTH || o.outWidth >
				// IMAGE_MAX_WIDTH) {
				// scale = (int) Math.pow(2, (int)
				// Math.round(Math.log(IMAGE_MAX_WIDTH / (double)
				// Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
				// }

				// scale = 2;

				// Decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();

				o2.inSampleSize = scale;
				HttpURLConnection con2 = (HttpURLConnection) ulrn
						.openConnection();
				con2.connect();
				InputStream fis = con2.getInputStream();
				b = BitmapFactory.decodeStream(fis, null, o2);
				fis.close();
				con2.disconnect();
			} catch (Exception e) {
				Log.e("Jami", e.getMessage());
			}
			return b;
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

		public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

			int width = bm.getWidth();

			int height = bm.getHeight();

			float scaleWidth = ((float) newWidth) / width;

			float scaleHeight = ((float) newHeight) / height;

			// create a matrix for the manipulation

			Matrix matrix = new Matrix();

			// resize the bit map

			matrix.postScale(scaleWidth, scaleHeight);

			// recreate the new Bitmap

			Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, false);

			return resizedBitmap;

		}

		private void setImage(int imageViewID2, Bitmap result) {

			if (result.getWidth() != 180) {
				float factor = 180f / result.getWidth();

				Log.i("Jami",
						"new height = "
								+ String.valueOf((int) (result.getHeight() * factor)));
				Log.i("Jami", "factor = " + String.valueOf(factor));

				result = this.getResizedBitmap(result,
						(int) (result.getHeight() * factor), 180);
			}

			MovieRating.current = result;

			((ImageView) MovieRating.this.findViewById(imageViewID2))
					.setImageBitmap(result);

			Log.i("Jami", "width = " + String.valueOf(result.getWidth()));
		}
	}

	/**
	 * A simple adapter which maintains an ArrayList of photo resource Ids. Each
	 * photo is displayed as an image. This adapter supports clearing the list
	 * of photos and adding a new photo.
	 * 
	 */
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		// Sample data set. children[i] contains the children (String[]) for
		// groups[i].
		private String[] groups = { "People Names", "Dog Names", "Cat Names",
				"Fish Names" };
		private String[][] children = {
				{ "Arnold", "Barry", "Chuck", "David" },
				{ "Ace", "Bandit", "Cha-Cha", "Deuce" },
				{ "Fluffy", "Snuggles" }, { "Goldy", "Bubbles" } };

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(MovieRating.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());
			return textView;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}
}
