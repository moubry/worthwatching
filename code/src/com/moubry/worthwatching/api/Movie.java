package com.moubry.worthwatching.api;

import java.io.Serializable;

import android.util.Log;

import com.moubry.worthwatching.R;

public class Movie implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 6082474191213900823L;
    public int MovieID;
    public int RottenTomatoesID;
    
    public String IMDBID;
    public String AbridgedCast;
    public int AudienceScore;
    public int CriticsScore;
    public String CriticsConsensus;
    public String DVDReleaseDate;
    public String FormattedRuntime;
    public String GoogleShowtimesURL;
    public String MPAARating;
    public String PosterProfileURL;
    public String PosterThumbnailURL;
    public String PosterDetailedURL;
    public String PosterURL;
    public String RottenTomatoesURL;
    public String Synopsis;
    public String TheaterReleaseDate;
    public String Title;
    public String TitleWithYear;
    public int Year;
    
    public int getTomatoImageResource()
    {
        if(this.CriticsScore == -1)
            return R.drawable.blank;
            
        if(this.CriticsScore >= 60)
            return R.drawable.fresh;
        
        return R.drawable.rotten;
    }
    public int getAudienceTomatoImageResource()
    {
        if(this.AudienceScore == -1)
            return R.drawable.blank;
            
        if(this.AudienceScore >= 60)
            return R.drawable.popcorn;
            
        return R.drawable.badpopcorn;
    }
    public String getFormattedCriticsScore()
    {
        if(this.CriticsScore == -1)
            return "---";
        
        return this.CriticsScore + "%";
    }
    public String getFormattedAudienceScore()
    {
        if(this.AudienceScore == -1)
            return "---";
        
        return this.AudienceScore + "%";
    }
    public String getCriticsScoreDescription() {
        
        if(this.CriticsScore == -1)
            return null;
        
        if(this.CriticsScore >= 60)
            return "Fresh";
        
        return "Rotten";
    }
    
    public Integer getMPAARatingImageID()
    {   
        if(this.MPAARating == null || this.MPAARating.length() == 0)
          return null;
        
        if(this.MPAARating.equalsIgnoreCase("NC-17"))
            return R.drawable.mpaa_nc17;
        else if(this.MPAARating.equalsIgnoreCase("R"))
            return R.drawable.mpaa_r;
        else if(this.MPAARating.equalsIgnoreCase("PG-13"))
            return R.drawable.mpaa_pg13;
        else if(this.MPAARating.equalsIgnoreCase("PG"))
            return R.drawable.mpaa_pg;
        else if(this.MPAARating.equalsIgnoreCase("G"))
            return R.drawable.mpaa_g;
    
        return null;
    }
    
    public String getMPAARatingDescription() {
        if ((this.MPAARating == null) || (this.MPAARating.length() == 0))
            return "No MPAA rating";
        return "MPAA rated " + this.MPAARating;
    }
}
