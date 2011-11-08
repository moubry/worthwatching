package com.moubry.worthwatching;

import android.content.SearchRecentSuggestionsProvider;
import com.moubry.worthwatching.R;

public class WWMovieTitleSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.moubry.worthwatching.WWMovieTitleSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public WWMovieTitleSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}