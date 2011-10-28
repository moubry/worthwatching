package com.moubry.tomatoratings.ui;

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
import com.moubry.tomatoratings.util.UIUtils;
import com.moubry.tomatoratings.util.WebService;
import com.moubry.tomatoratings.util.WebServiceHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;

import android.widget.ImageView;

public class MovieRatingActivity extends BaseActivity {

	private String imdbID;
	private String rottenTomatoesURL;
	private String rottenTomatoesID;

	public void loadData() {
		Bundle recdData = getIntent().getExtras();
		
		String critic_score_description = recdData.getString("com.moubry.critics_score_description");
		String critic_score = recdData.getString("com.moubry.rating");
		String audience_score = recdData.getString("com.moubry.audience_rating");
		String critic_tomato_image = recdData.getString("com.moubry.tomato_image");
		String audience_tomato_image = recdData.getString("com.moubry.audience_tomato_image");
		String consensus = recdData.getString("com.moubry.consensus");
        String mpaa_rating = recdData.getString("com.moubry.mpaa_rating_description");
        String mpaa_rating_image = recdData.getString("com.moubry.mpaa_rating_image");
		String cast = recdData.getString("com.moubry.cast");
		String runtime = recdData.getString("com.moubry.runtime");
		String release_date = recdData.getString("com.moubry.release_date");
		String synopsis = recdData.getString("com.moubry.synopsis");
		this.rottenTomatoesID = recdData.getString("com.moubry.rotten_tomatoes_id");
        this.rottenTomatoesURL = recdData.getString("com.moubry.rotten_tomatoes_url");
		this.imdbID = recdData.getString("com.moubry.imdb_id");

		((TextView) findViewById(R.id.android_tomatometer_score)).setText(critic_score);
		((TextView) findViewById(R.id.android_audience_tomatometer_score)).setText(audience_score);
		((ImageView) findViewById(R.id.tomato_image)).setImageResource(Integer.parseInt(critic_tomato_image));
		((ImageView) findViewById(R.id.tomato_image)).setContentDescription(critic_score_description);
		((ImageView) findViewById(R.id.audience_tomato_image)).setImageResource(Integer.parseInt(audience_tomato_image));
		((TextView) findViewById(R.id.android_consensus)).setText(consensus);
		
		
		ImageView mpaaView = (ImageView) findViewById(R.id.mpaa_rating);
		
		if(mpaa_rating_image == null)
		{	
			mpaaView.setVisibility(View.GONE);
			mpaaView.setContentDescription(null);
		}
		else
		{
			mpaaView.setContentDescription(mpaa_rating);
			mpaaView.setVisibility(View.VISIBLE);
			mpaaView.setImageResource(Integer.parseInt(mpaa_rating_image));
		}
		// org.apache.commons.lang.
		((TextView) findViewById(R.id.cast)).setText(cast);
		((TextView) findViewById(R.id.runtime)).setText(runtime);
		((TextView) findViewById(R.id.release_date)).setText(release_date);

		((TextView) findViewById(R.id.synopsis)).setText(synopsis);
		
		
		float posterImageWidth = getResources().getDimension(R.dimen.poster_image_width);
		
		Log.d("Jami", "jami width2222 = " + posterImageWidth);
		
	    if(posterImageWidth > 180)
	    	this.posterURL = recdData.getString("com.moubry.poster_original");
	    else if(posterImageWidth > 120)
	    	this.posterURL = recdData.getString("com.moubry.poster_detailed");
	    else if(posterImageWidth > 61)
	    	this.posterURL = recdData.getString("com.moubry.poster_profile");
	    else
	    	this.posterURL = recdData.getString("com.moubry.poster_thumbnail");

		TextView txtRT = (TextView) findViewById(R.id.rottentomatoes);

		if (rottenTomatoesURL != null && rottenTomatoesURL.length() > 0) {
			txtRT.setVisibility(View.VISIBLE);
			txtRT.setOnClickListener(new View.OnClickListener() {
				
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
			txtRT.setVisibility(View.INVISIBLE);
		}
		
		TextView txt = (TextView) findViewById(R.id.imdb);

		if (imdbID != null && imdbID.length() > 0) {
			txt.setVisibility(View.VISIBLE);
			txt.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("http://www.imdb.com/title/tt" + imdbID));
					startActivity(intent);
				}
			});
		} else {
			txt.setVisibility(View.INVISIBLE);
		}
	}

	private void loadPoster() {

		GetMoviePosterTask task = new GetMoviePosterTask();
		task.setPreferredWidth(getResources().getDimension(R.dimen.poster_image_width));
		task.setImageId(R.id.movie_poster);
		task.setImagePlaceholderId(R.id.movie_poster_placeholder);
		task.execute(this.posterURL);
	}

	private String posterURL;

	private void loadReviews() {

		String gae = "http://tomatoratings.moubry.com/movies/" + this.rottenTomatoesID + "/reviews.json";
		String rt = "http://api.rottentomatoes.com/api/public/v1.0/movies/" + this.rottenTomatoesID + "/reviews.json";
		
		
		GetTopCriticReviewsTask task = new GetTopCriticReviewsTask();
		task.setLayoutViewID(R.id.layout);
		task.setProgressID(R.id.progressBarLayout);
		task.setArrowID(R.drawable.arrow);
		task.setNoReviewsID(R.id.no_reviews);
		task.execute(gae, rt);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_movie_rating);
		
		Bundle recdData = getIntent().getExtras();
		String title = recdData.getString("com.moubry.title");

        getActivityHelper().setupActionBar(title);
        setTitle(title);

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

	private class GetTopCriticReviewsTask extends AsyncTask<String, Void, List<Review>> {

		private String errorMessage;

		private int progressID;
		private int arrowID;
        private int noReviewsID;
        
		// can use UI thread here
		protected void onPreExecute() {
			this.errorMessage = null;

			MovieRatingActivity.this.findViewById(this.noReviewsID).setVisibility(View.GONE);
			MovieRatingActivity.this.findViewById(this.progressID).setVisibility(View.VISIBLE);
		}

		public void setNoReviewsID(int noReviews) {
			this.noReviewsID = noReviews;
		}

		public void setArrowID(int arrow) {
			this.arrowID = arrow;
		}

		// can use UI thread here
		protected void onPostExecute(List<Review> result) {

			MovieRatingActivity.this.findViewById(this.progressID).setVisibility(View.GONE);

			if (errorMessage != null) {
				new AlertDialog.Builder(MovieRatingActivity.this)
						.setTitle("Error")
						.setMessage(errorMessage)
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

					LinearLayout reviewContainer = new LinearLayout(MovieRatingActivity.this);
					reviewContainer.setLayoutParams(fillWidth);
					reviewContainer.setOrientation(LinearLayout.VERTICAL);
					((LinearLayout) MovieRatingActivity.this.findViewById(this.layoutViewID)).addView(reviewContainer);
					
					LayoutParams l = new LayoutParams(LayoutParams.FILL_PARENT, 1);
					
					LinearLayout ruler = new LinearLayout(MovieRatingActivity.this);
					ruler.setLayoutParams(l);
					ruler.setBackgroundColor(0xFF737173);
					reviewContainer.addView(ruler);

					for (Review r : result) {

						LinearLayout reviewLayout = new LinearLayout(MovieRatingActivity.this);
						reviewLayout.setLayoutParams(fillWidth);
						reviewLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_item_pressed));
					    reviewLayout.setClickable(true);
					    reviewLayout.setFocusable(true);
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
						
						LinearLayout criticContainer = new LinearLayout(MovieRatingActivity.this);
						criticContainer.setLayoutParams(l3);
						criticContainer.setOrientation(LinearLayout.VERTICAL);
						criticContainer.setDuplicateParentStateEnabled(true);
					    reviewLayout.addView(criticContainer);
						
						LinearLayout criticLayout = new LinearLayout(MovieRatingActivity.this);
						criticLayout.setLayoutParams(wrap);
						criticLayout.setOrientation(LinearLayout.HORIZONTAL);
						criticLayout.setDuplicateParentStateEnabled(true);
						criticContainer.addView(criticLayout);
						
						TextView tv = new TextView(MovieRatingActivity.this);
						tv.setLayoutParams(wrap);
						tv.setText(r.getCritic());
						tv.setTextColor(getResources().getColorStateList(R.color.critic_text));
						tv.setDuplicateParentStateEnabled(true);
						tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
						criticLayout.addView(tv);
						
						if(r.getPublication() != null && r.getPublication().length() > 0)
						{
							TextView tv2 = new TextView(MovieRatingActivity.this);
							tv2.setLayoutParams(wrap);
							tv2.setText(", " + r.getPublication());
							tv2.setTextColor(getResources().getColorStateList(R.color.review_text));
							tv2.setDuplicateParentStateEnabled(true);
							criticLayout.addView(tv2);
						}

						tv = new TextView(MovieRatingActivity.this);
						tv.setLayoutParams(wrap);
						tv.setText(r.quote);
						tv.setTextColor(getResources().getColorStateList(R.color.review_text));
						tv.setDuplicateParentStateEnabled(true);
						criticContainer.addView(tv);
						
						
						LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 0);
						l2.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
						
						ImageView img = new ImageView(MovieRatingActivity.this);
						img.setLayoutParams(l2);
						img.setPadding(10, 0, 7, 0);
						
						if(r.links != null && r.links.review != null && r.links.review.length() > 0)
							img.setImageResource(this.arrowID);
						
						reviewLayout.addView(img);
						
						LinearLayout ruler2 = new LinearLayout(MovieRatingActivity.this);
						ruler2.setLayoutParams(l);
						ruler2.setBackgroundColor(0xFF737173);
						reviewContainer.addView(ruler2);
					}
				}
				else
					MovieRatingActivity.this.findViewById(this.noReviewsID).setVisibility(View.VISIBLE);
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

			Map<String, String> params = new HashMap<String, String>();
			params.put("review_type", "top_critic");
			
			WebServiceHelper h = new WebServiceHelper(
					MovieRatingActivity.this, 
					url[0], 
					url.length > 1 ? url[1] : url[0],
					params);

			this.errorMessage = h.errorMessage;
			
			if(h.errorMessage == null)
			{
				try {
					// Parse Response into our object
					MovieSearchResult result = new Gson().fromJson(h.result, MovieSearchResult.class);
		
					for (int i = 0; i < result.reviews.length; i++) {
						lstReviews.add(result.reviews[i]);
					}
				} catch (Exception e) {
					this.errorMessage = getString(R.string.unknown_service_error);
				}
			}

			return lstReviews;
		}
	}

	public static void AddMovieDataToIntent(Context context, Intent intent, Movie movie) {
		intent.putExtra("com.moubry.rating",
				movie.ratings.getFormattedCriticsScore());
		intent.putExtra("com.moubry.critics_score_description",
				movie.ratings.getCriticsScoreDescription());
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

		float posterImageWidth = context.getResources().getDimension(R.dimen.poster_image_width);
		
		Log.d("Jami", "jami width = " + posterImageWidth);
	
    	intent.putExtra("com.moubry.poster_original", movie.posters == null ? null : movie.posters.original);
    	intent.putExtra("com.moubry.poster_detailed", movie.posters == null ? null : movie.posters.detailed);
    	intent.putExtra("com.moubry.poster_profile", movie.posters == null ? null : movie.posters.profile);
    	intent.putExtra("com.moubry.poster_thumbnail", movie.posters == null ? null : movie.posters.thumbnail);
	    	
		intent.putExtra("com.moubry.title", movie.getTitle());
		intent.putExtra("com.moubry.rotten_tomatoes_id", movie.id);

		if(movie.mpaa_rating == null || movie.mpaa_rating.length() == 0)
			intent.putExtra("com.moubry.runtime", movie.getFormattedRuntime());
		else if(movie.getMPAARatingImageID() == null)
			intent.putExtra("com.moubry.runtime", movie.mpaa_rating + ", " + movie.getFormattedRuntime());
		else
		{
			intent.putExtra("com.moubry.runtime", movie.getFormattedRuntime());
			intent.putExtra("com.moubry.mpaa_rating_description", movie.getMPAARatingDescription());
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
		private float preferredWidth;
		
		public void setPreferredWidth(float f)
		{
			this.preferredWidth = f;
		}
		
		private Bitmap decodeFile(String url) {
			Bitmap b = null;

			try {
				URL ulrn = new URL(url);

				// Decode image size
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;

				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
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
				HttpURLConnection con2 = (HttpURLConnection) ulrn.openConnection();
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
		private int imageViewPlaceholderID;

		public void setImageId(int imageViewID) {
			this.imageViewID = imageViewID;
		}
		
		public void setImagePlaceholderId(int imageViewID) {
			this.imageViewPlaceholderID = imageViewID;
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null)
				setImage(imageViewID, imageViewPlaceholderID, result);
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

		private void setImage(int imageViewID2, int imageViewPlaceholderID2, Bitmap result) {

			Log.d("Jami", "jami preferred width = " + this.preferredWidth);
			
			if (result.getWidth() != (int)this.preferredWidth) {
				float factor = this.preferredWidth / result.getWidth();

				Log.i("Jami", "new height = " + String.valueOf((int) (result.getHeight() * factor)));
				Log.i("Jami", "factor = " + String.valueOf(factor));

				result = this.getResizedBitmap(result, (int) (result.getHeight() * factor), (int)this.preferredWidth);
			}

			MovieRatingActivity.current = result;

			ImageView img = ((ImageView) MovieRatingActivity.this.findViewById(imageViewID2));
			
			//img.setBackgroundDrawable(null);
			MovieRatingActivity.this.findViewById(imageViewPlaceholderID2).setVisibility(LinearLayout.GONE);
			img.setVisibility(LinearLayout.VISIBLE);
			img.setImageBitmap(result);			

			Log.i("Jami", "width = " + String.valueOf(result.getWidth()));
		}
	}
}
