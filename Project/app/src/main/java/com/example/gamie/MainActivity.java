package com.example.gamie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.gamie.api.IGDBDataFetcher;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBGameMode;
import com.example.gamie.api.IGDBGenre;
import com.example.gamie.api.IGDBPlatform;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, UpcomingGamesActivity.class));
    }

    // 1. get id list of upcoming games
    // 2. get games based on the upcoming games list
}
