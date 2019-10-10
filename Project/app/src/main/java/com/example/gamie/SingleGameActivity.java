package com.example.gamie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamie.activities.MenuActivity;
import com.example.gamie.adapters.GamesPagerAdapter;
import com.example.gamie.adapters.GamesScreenshotPagerAdapter;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBGameMode;
import com.example.gamie.api.IGDBGenre;
import com.example.gamie.api.IGDBPlatform;
import com.example.gamie.api.IGDBScreenshot;
import com.squareup.picasso.Picasso;

import me.relex.circleindicator.CircleIndicator;

public class SingleGameActivity extends MenuActivity {
    public static final String SINGLE_GAME_EXTRA = "single_game";
    public static final String PARENT_CLASS_EXTRA = "parent_class";

    private IGDBGame game;

    private TextView gameTitle;
    private TextView gameSummary;
    private TextView gamePlatforms;
    private TextView gameModes;
    private TextView gameGenres;
    private ImageView gameCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_game);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(SINGLE_GAME_EXTRA)) {
                game = (IGDBGame) extras.getSerializable(SINGLE_GAME_EXTRA);
                gameTitle = findViewById(R.id.singleGameTitle);
                gameSummary = findViewById(R.id.singleGameSummary);
                gamePlatforms = findViewById(R.id.singleGamePlatform);
                gameModes = findViewById(R.id.singleGameMode);
                gameGenres = findViewById(R.id.singleGameGenre);
                gameCover = findViewById(R.id.singleGameCover);

                gameTitle.setText(game.name);

                if (game.summary != null) {
                    gameSummary.setText(game.summary);
                }

                if (game.platforms != null && game.platforms.size() > 0) {
                    gamePlatforms.setText("");
                    for (IGDBPlatform platform : game.platforms) {
                        gamePlatforms.append(String.format("%s\n", platform.name));
                    }
                }

                if (game.gameModes != null && game.gameModes.size() > 0) {
                    gameModes.setText("");
                    for (IGDBGameMode mode: game.gameModes) {
                        gameModes.append(String.format("%s\n", mode.name));
                    }
                }
                if (game.genres != null && game.genres.size() > 0) {
                    gameGenres.setText("");
                    for (IGDBGenre genre: game.genres) {
                        gameGenres.append(String.format("%s\n", genre.name));
                    }
                }

                if (game.coverArt != null) {
                    Picasso.with(this).load(game.coverArt.getImageUrl(IGDBScreenshot.IGDBScreenshotSize.FHD)).into(gameCover);
                } else {
                    gameCover.setImageResource(R.drawable.placeholder_image);
                }

                if (game.screenshots != null && game.screenshots.size() > 0) {
                    TextView title = findViewById(R.id.singleGameScreenshotTitle);
                    ViewPager pager = findViewById(R.id.singleGameScreenshot);
                    CircleIndicator indicator = findViewById(R.id.singleGameScreenshotCircleIndicator);

                    GamesScreenshotPagerAdapter pagerAdapter = new GamesScreenshotPagerAdapter(this, game.screenshots);
                    pager.setAdapter(pagerAdapter);

                    indicator.setViewPager(pager);
                    pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
                    title.setVisibility(View.VISIBLE);
                    pager.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.VISIBLE);
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(this, "The game information could not be loaded. Returning to home.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityImpl();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityImpl();
    }

    private Intent getParentActivityImpl() {
        Intent parent = getIntent();
        if (parent.hasExtra(PARENT_CLASS_EXTRA)) {
            String className = parent.getStringExtra(PARENT_CLASS_EXTRA);
            try {
                return new Intent(this, Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new Intent(this, MainActivity.class);
    }
}
