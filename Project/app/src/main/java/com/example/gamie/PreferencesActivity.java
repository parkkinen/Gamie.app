package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.adapters.GamesGridGestureListener;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames {

    protected GridView gamesGrid;
    public GamesGridAdapter gamesGridAdapter;
    protected List<IGDBGame> games = new ArrayList<>();
    protected GestureDetectorCompat gridGestureDetector;
    protected TextView pageTitle;
    protected TextView pageText;
    protected SearchView searchBar;
    private IGDBDataFetcher api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        api = new IGDBDataFetcher(this);

        //Init views
        pageText = findViewById(R.id.gridPrefPage);
        pageTitle = findViewById(R.id.gridPrefTitle);
        searchBar = findViewById(R.id.prefSearchBar);
        gamesGrid = findViewById(R.id.gridPrefView);

        //adapter
        gamesGridAdapter = new GamesGridAdapter (this, R.id.gridPrefView, games);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                api.getGames(PreferencesActivity.this,null, "search \"" + query + "\"", "limit 4");
                return false;
            }
        });


        gamesGrid.setAdapter(gamesGridAdapter);
    }
    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        gamesGridAdapter.notifyDataSetChanged();
    }
}