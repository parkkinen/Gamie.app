package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.adapters.GamesGridGestureListener;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;
import com.example.gamie.api.IGDBReleaseDate;

import java.util.ArrayList;
import java.util.List;

public class UpcomingGamesActivity extends GridGamesActivity implements IGDBDataFetcher.OnGetGames {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageTitle.setText("Upcoming games");
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, IGDBPlatform.PlatformType.PC);
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        gamesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterLeftSwipe() {
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, IGDBPlatform.PlatformType.PC);
    }

    public void afterRightSwipe() {
        this.api.getUpcomingGames(this, null, GAMES_PER_PAGE, page * 10, IGDBPlatform.PlatformType.PC);
    }
}
