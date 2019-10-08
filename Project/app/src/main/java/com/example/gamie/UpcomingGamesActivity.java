package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamie.adapters.GamesGridAdapter;
import com.example.gamie.adapters.GamesGridGestureListener;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpcomingGamesActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames, GamesGridGestureListener.OnGesture {
    private static final int GAMES_PER_PAGE = 6;
    private static final int MAX_PAGE = 15;
    private int page;

    private GridView upcomingGamesGrid;
    private GamesGridAdapter adapter;
    private List<IGDBGame> games = new ArrayList<>();
    private GestureDetectorCompat detector;
    private TextView pageText;

    private IGDBDataFetcher api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_games);
        page = 0;
        pageText = findViewById(R.id.upcomingGamesPageTw);
        pageText.setText(String.format("Page %d", page + 1));

        detector = new GestureDetectorCompat(this, new GamesGridGestureListener(this));
        api = new IGDBDataFetcher(this);
        adapter = new GamesGridAdapter(this, games);
        upcomingGamesGrid = findViewById(R.id.upcomingGamesGrid);
        upcomingGamesGrid.setAdapter(adapter);
        upcomingGamesGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return false;
            }
        });
        api.getGames(this, null, String.format("offset %d; limit %d", page * 10, GAMES_PER_PAGE));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        this.games.clear();
        this.games.addAll(games);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void leftSwipe() {
        if (page < MAX_PAGE) {
            page += 1;
            api.getGames(this, String.format("offset %d; limit %d", page * 10, GAMES_PER_PAGE));
            pageText.setText(String.format("Page %d", page + 1));
        } else {
            UpcomingGamesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UpcomingGamesActivity.this, String.format("There is currently no more than %d pages available!", MAX_PAGE + 1), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void rightSwipe() {
        if (page > 0) {
            page -= 1;
            api.getGames(this, String.format("offset %d; limit %d", page * 10, GAMES_PER_PAGE));
            pageText.setText(String.format("Page %d", page + 1));
        } else {
            UpcomingGamesActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UpcomingGamesActivity.this, "You can't browse back from first page!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void topSwipe() {}

    @Override
    public void bottomSwipe() {}
}
