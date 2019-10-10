package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;
import com.example.gamie.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class RecommendedGamesActivity extends GridGamesActivity implements IGDBDataFetcher.OnGetGames {
    private List<Integer> gamePreferences = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageTitle.setText("Recommended games");
        spinner.setVisibility(View.INVISIBLE);
        gamePreferences = UserPreferences.getUserGamePrefences();
        attemptGettingRecommendations();
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        gamesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterLeftSwipe() {
        attemptGettingRecommendations();
    }

    @Override
    public void afterRightSwipe() {
        attemptGettingRecommendations();
    }

    @Override
    public void afterApiError() {
        attemptGettingRecommendations();
    }

    private void attemptGettingRecommendations() {
        if (gamePreferences.size() > 0) {
            this.api.getRecommendedGames(this, null, gamePreferences, String.format("limit %d", GAMES_PER_PAGE), String.format("offset %d", page * 10));
        } else {
            RecommendedGamesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RecommendedGamesActivity.this, "You don't currently have any recommendations set on your profile.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
