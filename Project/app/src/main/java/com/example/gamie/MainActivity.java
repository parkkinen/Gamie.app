package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamie.activities.MenuActivity;
import com.example.gamie.adapters.GamesPagerAdapter;
import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBPlatform;
import com.example.gamie.api.IGDBReleaseDate;
import com.example.gamie.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends MenuActivity implements IGDBDataFetcher.OnGetGames, IGDBDataFetcher.OnGetReleaseDates, IGDBDataFetcher.OnError {
    private IGDBDataFetcher api;
    private final String UPCOMING_TAG = "upcoming";
    private final String NEW_TAG = "new";
    private final String RECOMMENDED_TAG = "recommended";

    private TextView recommendedTitle;

    private GamesPagerAdapter adapter_ug;
    private GamesPagerAdapter adapter_nr;
    private GamesPagerAdapter adapter_rg;

    private ViewPager viewPager_ug;
    private ViewPager viewPager_nr;
    private ViewPager viewPager_rg;

    private CircleIndicator circleIndicator_ug;
    private CircleIndicator circleIndicator_nr;
    private CircleIndicator circleIndicator_rg;

    private List<IGDBGame> upcomingGames = new ArrayList<>();
    private List<IGDBGame> newGames = new ArrayList<>();
    private List<IGDBGame> recommendedGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new IGDBDataFetcher(this, this);

        recommendedTitle = findViewById(R.id.recommended_tv);

        viewPager_ug = findViewById(R.id.upcomingGames_vp);
        viewPager_nr = findViewById(R.id.newReleases_vp);
        viewPager_rg = findViewById(R.id.recommended_vp);

        circleIndicator_ug = findViewById(R.id.circleIndicator_ug);
        circleIndicator_nr = findViewById(R.id.circleIndicator_nr);
        circleIndicator_rg = findViewById(R.id.circleIndicator_rg);

        adapter_ug = new GamesPagerAdapter(this, upcomingGames);
        adapter_nr = new GamesPagerAdapter(this, newGames);
        adapter_rg = new GamesPagerAdapter(this, recommendedGames);

        viewPager_ug.setAdapter(adapter_ug);
        viewPager_nr.setAdapter(adapter_nr);
        viewPager_rg.setAdapter(adapter_rg);

        adapter_ug.registerDataSetObserver(circleIndicator_ug.getDataSetObserver());
        adapter_nr.registerDataSetObserver(circleIndicator_nr.getDataSetObserver());
        adapter_rg.registerDataSetObserver(circleIndicator_rg.getDataSetObserver());

        circleIndicator_ug.setViewPager(viewPager_ug);
        circleIndicator_nr.setViewPager(viewPager_nr);
        circleIndicator_rg.setViewPager(viewPager_rg);

        api.getUpcomingGames(this, UPCOMING_TAG, 10, 0, IGDBPlatform.PlatformType.PC);
        api.getNewGames(this, NEW_TAG, 10, 0, IGDBPlatform.PlatformType.PC);
        
        List<Integer> preferences = UserPreferences.getUserGamePrefences();
        if (preferences.size() > 0) {
            api.getRecommendedGames(this, RECOMMENDED_TAG, UserPreferences.getUserGamePrefences());
            viewPager_rg.setVisibility(View.VISIBLE);
            circleIndicator_rg.setVisibility(View.VISIBLE);
            recommendedTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void games(List<IGDBGame> games, String tag) {
        if (tag.equals(UPCOMING_TAG)) {
            upcomingGames.clear();
            upcomingGames.addAll(games);
            adapter_ug.notifyDataSetChanged();
        } else if (tag.equals(NEW_TAG)) {
            newGames.clear();
            newGames.addAll(games);
            adapter_nr.notifyDataSetChanged();
        } else if (tag.equals(RECOMMENDED_TAG)) {
            recommendedGames.clear();
            recommendedGames.addAll(games);
            adapter_rg.notifyDataSetChanged();
        }
    }


    @Override
    public void releaseDates(List<IGDBReleaseDate> releaseDates, String tag) {
        String whereGameIdOption = "where id = (";

        for (IGDBReleaseDate releaseDate : releaseDates) {
            if (releaseDate.gameId != null) {
                whereGameIdOption += releaseDate.gameId;
                if (releaseDates.indexOf(releaseDate) != releaseDates.size() - 1) {
                    whereGameIdOption += ",";
                }
            }
        }
        whereGameIdOption += "); limit 8";

        api.getGames(MainActivity.this, tag, whereGameIdOption);
    }

    @Override
    public void error(Exception e, String tag) {
        e.printStackTrace();
    }
}
