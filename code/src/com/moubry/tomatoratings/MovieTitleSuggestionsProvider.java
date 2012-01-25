package com.moubry.tomatoratings;

import android.content.SearchRecentSuggestionsProvider;
import com.moubry.worthwatching.WWMovieTitleSuggestionsProvider;

public class MovieTitleSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public MovieTitleSuggestionsProvider() {
        setupSuggestions(WWMovieTitleSuggestionsProvider.AUTHORITY_FREE, WWMovieTitleSuggestionsProvider.MODE);
    }
}