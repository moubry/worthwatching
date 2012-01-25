package com.moubry.worthwatching;

import android.content.SearchRecentSuggestionsProvider;

public class WWMovieTitleSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY_PAID = "com.moubry.worthwatching.WWMovieTitleSuggestionsProvider";
    public final static String AUTHORITY_FREE = "com.moubry.tomatoratings.MovieTitleSuggestionsProvider";
    
    public final static int MODE = DATABASE_MODE_QUERIES;

    public WWMovieTitleSuggestionsProvider() {
        setupSuggestions(AUTHORITY_PAID, MODE);
    }
    
    public static String GetAuthority(Boolean isFreeVersion){
        if(isFreeVersion)
          return AUTHORITY_FREE;
        
        return AUTHORITY_PAID;
    }
}