package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpcomingGamesActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames {
    private static final int GAMES_PER_PAGE = 10;
    private int gameOffset = 0;

    private GridView upcomingGamesGrid;
    private GamesGridAdapter adapter;
    private List<IGDBGame> games = new ArrayList<>();
    private IGDBDataFetcher api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_games);
        api = new IGDBDataFetcher(this);
        upcomingGamesGrid = findViewById(R.id.upcomingGamesGrid);
        adapter = new GamesGridAdapter(this, games);
        upcomingGamesGrid.setAdapter(adapter);
        api.getGames(this, String.format("offset %d; limit %d", gameOffset, GAMES_PER_PAGE));
    }

    @Override
    public void games(List<IGDBGame> games) {
        this.games.clear();
        this.games.addAll(games);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void error(String e) {
        Log.d("gamie", "Error occurred while getting games: " + e);
    }
}
