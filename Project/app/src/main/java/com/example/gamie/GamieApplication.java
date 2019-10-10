package com.example.gamie;

import android.app.Application;
import android.content.SharedPreferences;

public class GamieApplication extends Application {
    public static final String USER_PREFERENCES = "user_preferences";
    private static GamieApplication instance;

    public GamieApplication() {
        super();
    }

    public static GamieApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public SharedPreferences getPreferences(String key) {
        return getApplicationContext().getSharedPreferences(key, MODE_PRIVATE);
    }
}
