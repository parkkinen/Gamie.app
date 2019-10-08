package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpcomingGamesActivity extends AppCompatActivity implements IGDBDataFetcher.OnGetGames {
    private static final int COLUMN_COUNT = 2;
    private static final int GAMES_PER_PAGE = 10;
    private GridLayout upcomingGamesGrid;
    private IGDBDataFetcher api;
    private int gameOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_games);
        api = new IGDBDataFetcher(this);
        upcomingGamesGrid = findViewById(R.id.upcomingGamesGrid);

        api.getGames(this, null, String.format("offset %d; limit %d", gameOffset, GAMES_PER_PAGE));
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        upcomingGamesGrid.removeAllViews();
        upcomingGamesGrid.setColumnCount(COLUMN_COUNT);
        upcomingGamesGrid.setRowCount(games.size() / COLUMN_COUNT);
        for (IGDBGame game : games) {
            ImageView gridImage = new ImageView(this);
            if (game.coverArt != null) {
                Picasso.with(this).load(game.coverArt.getImageUrl(IGDBScreenshot.IGDBScreenshotSize.SCREENSHOT_HUGE)).into(gridImage);
            } else {
                gridImage.setImageResource(R.drawable.placeholder_image);
            }
            gridImage.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
            gridImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            CardView cardView = new CardView(this);
            cardView.setRadius(10);
            cardView.addView(gridImage);
            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
            upcomingGamesGrid.addView(cardView, gridParam);
        }
    }
}
