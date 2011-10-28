package com.moubry.tomatoratings;

import android.content.SearchRecentSuggestionsProvider;

public class MovieTitleSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.moubry.tomatoratings.MovieTitleSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MovieTitleSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}