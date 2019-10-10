package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;

import java.util.ArrayList;
import java.util.List;

public class RecommendedGamesActivity extends GridGamesActivity implements IGDBDataFetcher.OnGetGames {
    private List<Integer> gameIds = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageTitle.setText("Recommended games");
        gameIds.add(241);
        gameIds.add(9083);
        gameIds.add(1029);
        spinner.setVisibility(View.INVISIBLE);
        this.api.getRecommendedGames(this, null, gameIds, String.format("limit %d", GAMES_PER_PAGE), String.format("offset %d", page * 10));
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        gamesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterLeftSwipe() {
        this.api.getRecommendedGames(this, null, gameIds, String.format("limit %d", GAMES_PER_PAGE), String.format("offset %d", page * 10));
    }

    @Override
    public void afterRightSwipe() {
        this.api.getRecommendedGames(this, null, gameIds, String.format("limit %d", GAMES_PER_PAGE), String.format("offset %d", page * 10));
    }

    @Override
    public void afterApiError() {
        this.api.getRecommendedGames(this, null, gameIds, String.format("limit %d", GAMES_PER_PAGE), String.format("offset %d", page * 10));
    }
}
