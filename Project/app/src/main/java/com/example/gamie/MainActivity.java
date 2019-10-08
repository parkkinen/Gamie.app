package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBGameMode;
import com.example.gamie.api.IGDBGenre;
import com.example.gamie.api.IGDBPlatform;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames{
    private IGDBDataFetcher api;

    private List<IGDBGame> upcomingGames = new ArrayList<>();
    private List<IGDBGame> newGames = new ArrayList<>();
    private List<IGDBGame> recommendedGames = new ArrayList<>();

    private ImageView upcomingGames;
    private ImageView newReleases;
    private ImageView recommended;


    // 1.get id list of upcoming games
    // 2. get games base on the upcoming games list
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new IGDBDataFetcher(this);
        upcomingGames = findViewById(R.id.upcomingGames_iv);
        newReleases = findViewById(R.id.newReleases_iv);
        recommended = findViewById(R.id.recommended_iv);

        api.getGames(new IGDBDataFetcher.OnGetGames() {
            @Override
            public void games(List<IGDBGame> games) {
                this.
            }

            @Override
            public void error(String e) {

            }
        }, "limit 8");
    }

}
