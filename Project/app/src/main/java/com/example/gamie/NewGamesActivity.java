package com.example.gamie;

import android.os.Bundle;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;

import java.util.List;

public class NewGamesActivity extends GridGamesActivity implements IGDBDataFetcher.OnGetGames {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageTitle.setText("New Games");
        this.api.getNewGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        gamesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterLeftSwipe() {
        this.api.getNewGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void afterRightSwipe() {
        this.api.getNewGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void afterPlatformChange(IGDBPlatform.PlatformType platformType) {
        this.api.getNewGames(this, null, GAMES_PER_PAGE, page * 10, platformType);
    }

    @Override
    public void afterApiError() {
        this.api.getNewGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }
}
