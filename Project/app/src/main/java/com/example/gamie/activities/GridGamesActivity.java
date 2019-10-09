package com.example.gamie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamie.R;
import com.example.gamie.SingleGameActivity;
import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.adapters.GamesGridGestureListener;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;

import java.util.ArrayList;
import java.util.List;

public class GridGamesActivity extends AppCompatActivity implements GamesGridGestureListener.OnGesture {
    protected static final int GAMES_PER_PAGE = 6;
    protected static final int MAX_PAGE = 150 / GAMES_PER_PAGE;
    protected int lastPage = 0;
    protected int page = 0;

    protected IGDBPlatform.PlatformType lastPlatform;
    protected IGDBPlatform.PlatformType platform;

    protected GridView gamesGrid;
    protected GamesGridAdapter gamesGridAdapter;
    protected List<IGDBGame> games = new ArrayList<>();
    protected GestureDetectorCompat gridGestureDetector;
    protected TextView pageTitle;
    protected TextView pageText;
    protected Spinner spinner;
    protected List<String> spinnerItems = new ArrayList<>();

    protected IGDBDataFetcher api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_games);
        pageText = findViewById(R.id.gridGamesPage);
        pageTitle = findViewById(R.id.gridGamesTitle);
        spinner = findViewById(R.id.gridGamesSpinner);

        for (IGDBPlatform.PlatformType type : IGDBPlatform.PlatformType.values()) {
            spinnerItems.add(type.toString());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setSelection(i);
                lastPlatform = platform;
                platform = IGDBPlatform.PlatformType.getType((String)spinner.getSelectedItem());
                lastPage = page;
                page = 0;
                pageText.setText(String.format("Page %d", page + 1));
                afterPlatformChange(platform);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        spinner.setSelection(0);
        platform = IGDBPlatform.PlatformType.getType((String)spinner.getSelectedItem());

        pageText.setText(String.format("Page %d", page + 1));

        gridGestureDetector = new GestureDetectorCompat(this, new GamesGridGestureListener(this));
        api = new IGDBDataFetcher(this, new IGDBDataFetcher.OnError() {
            @Override
            public void error(Exception e, String tag) {
                Toast.makeText(getBaseContext(), String.format("An error occurred during an API call: %s", e.toString()), Toast.LENGTH_LONG).show();

                page = lastPage;
                platform = lastPlatform;
                pageText.setText(String.format("Page %d", page + 1));
                spinner.setSelection(spinnerItems.indexOf(lastPlatform.toString()));
                afterApiError();
            }
        });
        gamesGridAdapter = new GamesGridAdapter(this, games);
        gamesGrid = findViewById(R.id.gridGamesView);
        gamesGrid.setAdapter(gamesGridAdapter);
        gamesGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gridGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        gamesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IGDBGame game = games.get(i);
                Intent singleGameIntent = new Intent(GridGamesActivity.this, SingleGameActivity.class);
                singleGameIntent.putExtra(SingleGameActivity.SINGLE_GAME_EXTRA, game);
                startActivity(singleGameIntent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gridGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void leftSwipe() {
        if (page < MAX_PAGE) {
            lastPage = page;
            lastPlatform = platform;
            page += 1;
            this.afterLeftSwipe();
            pageText.setText(String.format("Page %d", page + 1));
        } else {
            GridGamesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GridGamesActivity.this, String.format("There is currently no more than %d pages available!", MAX_PAGE + 1), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Performed after the page has been swiped to the left and page logic has been applied (Usually point to fetch next pages data)
    public void afterLeftSwipe() {}

    @Override
    public void rightSwipe() {
        if (page > 0) {
            lastPage = page;
            lastPlatform = platform;
            page -= 1;
            this.afterRightSwipe();
            pageText.setText(String.format("Page %d", page + 1));
        } else {
            GridGamesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GridGamesActivity.this, "You can't browse back from first page!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Performed after the page has been swiped to the right and page logic has been applied (Usually point to fetch previous pages data)
    public void afterRightSwipe() {}

    // Performed once the user changes the platform type from spinner menu
    // platformType: given platforms type as integer
    public void afterPlatformChange(IGDBPlatform.PlatformType platformType) {}

    // Performed after the api has caught an error. Good place to retrieve data from previous page / platform selections.
    public void afterApiError() {}

    @Override
    public void topSwipe() {}

    @Override
    public void bottomSwipe() {}
}