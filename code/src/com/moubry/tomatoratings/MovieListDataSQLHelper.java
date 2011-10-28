package com.moubry.tomatoratings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class MovieListDataSQLHelper extends SQLiteOpenHelper {

	public static final String TAG = "MovieListDataSQLHelper";
	
	private static final String DATABASE_NAME = "movieLists.db";
	private static final int DATABASE_VERSION = 1;

	// Table name
	public static final String TABLE = "movieLists";

	// Columns
	public static final String TIME = "time";
	public static final String TITLE = "title";
	public static final String JSON = "json";

	public MovieListDataSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, "
				+ TIME + " integer, "
				+ TITLE + " varchar(25) not null, "
				+ JSON + " text not null);";
		Log.d(TAG, "onCreate: " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;	
		
        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Recreates the database with a new version
        onCreate(db);
	}
}