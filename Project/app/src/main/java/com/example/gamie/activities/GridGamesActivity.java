package com.example.gamie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamie.R;
import com.example.gamie.UpcomingGamesActivity;
import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.adapters.GamesGridGestureListener;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;

import java.util.ArrayList;
import java.util.List;

public class GridGamesActivity extends AppCompatActivity implements GamesGridGestureListener.OnGesture {
    protected static final int GAMES_PER_PAGE = 6;
    protected static final int MAX_PAGE = 150 / GAMES_PER_PAGE;
    protected int page;

    protected GridView gamesGrid;
    protected GamesGridAdapter gamesGridAdapter;
    protected List<IGDBGame> games = new ArrayList<>();
    protected GestureDetectorCompat gridGestureDetector;
    protected TextView pageTitle;
    protected TextView pageText;
    protected TextView pageContent;

    protected IGDBDataFetcher api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_games);
        page = 0;
        pageText = findViewById(R.id.gridGamesPage);
        pageTitle = findViewById(R.id.gridGamesTitle);
        pageContent = findViewById(R.id.gridGamesContent);

        pageText.setText(String.format("Page %d", page + 1));

        gridGestureDetector = new GestureDetectorCompat(this, new GamesGridGestureListener(this));
        api = new IGDBDataFetcher(this, new IGDBDataFetcher.OnError() {
            @Override
            public void error(Exception e, String tag) {
                if (gamesGrid.isShown() && !pageContent.isShown()) {
                    gamesGrid.setVisibility(View.INVISIBLE);
                    pageContent.setText(String.format("An error occurred during an API call: %s", e.toString()));
                    pageContent.setVisibility(View.VISIBLE);
                }
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gridGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void leftSwipe() {
        if (page < MAX_PAGE) {
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

    @Override
    public void topSwipe() {}

    @Override
    public void bottomSwipe() {}
}