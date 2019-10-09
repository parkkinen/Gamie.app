package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;
import com.example.gamie.api.IGDBReleaseDate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames {
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

        api.getUpcomingGames(this, UPCOMING_TAG, 10, 0, IGDBPlatform.PlatformType.PC);
        api.getNewGames(this, NEW_TAG, 10, 0, IGDBPlatform.PlatformType.PC);
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
}
