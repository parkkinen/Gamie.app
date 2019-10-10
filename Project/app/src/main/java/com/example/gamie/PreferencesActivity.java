package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.activities.MenuActivity;
import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.adapters.GamesGridGestureListener;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.preferences.UserPreferences;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends MenuActivity implements IGDBDataFetcher.OnGetGames {

    private GridView gamesGrid;
    private GridView favoriteGrid;
    private GamesGridAdapter gamesGridAdapter;
    private GamesGridAdapter favoriteGridAdapter;
    private List<IGDBGame> games = new ArrayList<>();
    private List<IGDBGame> preferredGames = new ArrayList<>();
    private GestureDetectorCompat gridGestureDetector;
    private TextView pageTitle;
    private TextView pageText;
    private SearchView searchBar;
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
        favoriteGrid = findViewById(R.id.favoriteGameGrid);

        //adapter
        gamesGridAdapter = new GamesGridAdapter (this, games);
        favoriteGridAdapter = new GamesGridAdapter(this, preferredGames);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                api.getGames(PreferencesActivity.this,"search", "search \"" + query + "\"", "limit 4");
                return false;
            }
        });


        gamesGrid.setAdapter(gamesGridAdapter);
        favoriteGrid.setAdapter(favoriteGridAdapter);
        String where = "where id = (";
        List<Integer> prefs = UserPreferences.getUserGamePrefences();
        for (Integer gamePref : prefs) {
            if (prefs.indexOf(gamePref) != 0) {
                where += "," + gamePref;
            } else {
                where += gamePref;
            }
        }
        where += ")";
        api.getGames(this, "recommended", "limit 50", where);
    }
    @Override
    public void games(List<IGDBGame> games, String tag) {
        if (tag.equals("search")) {
            this.games.clear();
            this.games.addAll(games);
            gamesGridAdapter.notifyDataSetChanged();
        } else if (tag.equals("recommended")) {
            this.preferredGames.clear();
            this.preferredGames.addAll(games);
            this.favoriteGridAdapter.notifyDataSetChanged();
        }
    }
}