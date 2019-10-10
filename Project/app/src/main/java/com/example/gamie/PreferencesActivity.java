package com.example.gamie;

import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamie.activities.MenuActivity;
import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends MenuActivity implements IGDBDataFetcher.OnGetGames, IGDBDataFetcher.OnError {
    public static final String FAVOURITE_GAMES = "favourite";
    public static final String SEARCH_GAMES = "search";

    private GridView gamesGrid;
    private GridView favoriteGrid;
    private GamesGridAdapter gamesGridAdapter;
    private GamesGridAdapter favoriteGridAdapter;
    private List<IGDBGame> games = new ArrayList<>();
    private List<IGDBGame> favouriteGames = new ArrayList<>();
    private GestureDetectorCompat gridGestureDetector;
    private TextView pageTitle;
    private TextView pageText;
    private SearchView searchBar;
    private IGDBDataFetcher api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        api = new IGDBDataFetcher(this, this);

        //Init views
        pageTitle = findViewById(R.id.gridPrefTitle);
        searchBar = findViewById(R.id.prefSearchBar);
        gamesGrid = findViewById(R.id.gridPrefView);
        favoriteGrid = findViewById(R.id.favoriteGameGrid);

        //adapter
        gamesGridAdapter = new GamesGridAdapter (this, games);
        gamesGridAdapter.setOnInteraction(new GamesGridAdapter.OnInteraction() {
            @Override
            public void starClicked(int itemPos) {
                api.getFavouriteGames(PreferencesActivity.this, FAVOURITE_GAMES, "limit 50");
            }
        });
        favoriteGridAdapter = new GamesGridAdapter(this, favouriteGames);
        favoriteGridAdapter.setOnInteraction(new GamesGridAdapter.OnInteraction() {
            @Override
            public void starClicked(int itemPos) {
                favouriteGames.remove(itemPos);
                PreferencesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PreferencesActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                favoriteGridAdapter.notifyDataSetChanged();
                                gamesGridAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                api.getGames(PreferencesActivity.this, SEARCH_GAMES, "search \"" + query + "\"", "limit 4");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });


        gamesGrid.setAdapter(gamesGridAdapter);
        favoriteGrid.setAdapter(favoriteGridAdapter);

        api.getFavouriteGames(this, FAVOURITE_GAMES, "limit 50");
    }
    @Override
    public void games(List<IGDBGame> games, String tag) {
        if (tag.equals(SEARCH_GAMES)) {
            this.games.clear();
            this.games.addAll(games);
            gamesGridAdapter.notifyDataSetChanged();
        } else if (tag.equals(FAVOURITE_GAMES)) {
            this.favouriteGames.clear();
            this.favouriteGames.addAll(games);
            this.favoriteGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void error(Exception e, String tag) {
        Toast.makeText(getBaseContext(), String.format("An error occurred during an API call: %s", e.toString()), Toast.LENGTH_LONG).show();
    }
}