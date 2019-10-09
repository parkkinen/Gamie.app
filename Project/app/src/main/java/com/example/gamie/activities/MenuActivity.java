package com.example.gamie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.gamie.AllGamesActivity;
import com.example.gamie.NewGamesActivity;
import com.example.gamie.R;
import com.example.gamie.UpcomingGamesActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.allGamesMenu:
                startActivity(new Intent(this, AllGamesActivity.class));
                return true;
            case R.id.recommendedGamesMenu:
                // startActivity(new Intent(this, null)); TODO: Create link once Recommended games activity is created
                return true;
            case R.id.upcomingGamesMenu:
                startActivity(new Intent(this, UpcomingGamesActivity.class));
                return true;
            case R.id.newGamesMenu:
                startActivity(new Intent(this, NewGamesActivity.class));
                return true;
            case R.id.preferencesMenu:
                // startActivity(new Intent(this, null)); TODO: Create link once Preferences activity is created
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
