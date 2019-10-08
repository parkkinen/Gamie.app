package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBReleaseDate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames, IGDBDataFetcher.OnGetReleaseDates {
    private IGDBDataFetcher api;
    private final String UPCOMING_TAG = "upcoming";
    private final String NEW_TAG = "new";
    private final String RECOMMENDED_TAG = "recommended";

    private List<IGDBGame> upcomingGames = new ArrayList<>();
    private List<IGDBGame> newGames = new ArrayList<>();
    private List<IGDBGame> recommendedGames = new ArrayList<>();

    private ImageView upcomingGamesIV;
    private ImageView newReleasesIV;
    private ImageView recommendedIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new IGDBDataFetcher(this);
        upcomingGamesIV = findViewById(R.id.upcomingGames_iv);
        newReleasesIV = findViewById(R.id.newReleases_iv);
        recommendedIV = findViewById(R.id.recommended_iv);

        // Get upcoming games ids
        api.getReleaseDates(this, UPCOMING_TAG, String.format("where date > %d; sort date asc", System.currentTimeMillis()));
        api.getReleaseDates(this, NEW_TAG, String.format("where date < %d; sort date desc", System.currentTimeMillis()));
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        if (tag.equals(UPCOMING_TAG)) {
            this.upcomingGames = games;
        } else if (tag.equals(NEW_TAG)) {
            this.newGames = games;
        } else if (tag.equals(RECOMMENDED_TAG)) {
            this.recommendedGames = games;
        }
    }

    @Override
    public void releaseDates(List<IGDBReleaseDate> releaseDates, String tag) {
        String whereGameIdOption = "where id = (";

        for (IGDBReleaseDate releaseDate : releaseDates) {
            whereGameIdOption += releaseDate.gameId;
            if (releaseDates.indexOf(releaseDate) != releaseDates.size() - 1) {
                whereGameIdOption += ",";
            }
        }
        whereGameIdOption += ")";

        api.getGames(MainActivity.this, tag, whereGameIdOption);
    }
}
