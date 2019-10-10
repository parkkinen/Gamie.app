package com.example.gamie;

import android.os.Bundle;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;

import java.util.List;

public class UpcomingGamesActivity extends GridGamesActivity implements IGDBDataFetcher.OnGetGames {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageTitle.setText("Upcoming games");
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        gamesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterLeftSwipe() {
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void afterRightSwipe() {
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void afterPlatformChange(IGDBPlatform.PlatformType platformType) {
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }

    @Override
    public void afterApiError() {
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, platform);
    }
}
