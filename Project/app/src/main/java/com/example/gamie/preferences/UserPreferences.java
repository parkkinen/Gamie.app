package com.example.gamie.preferences;

import android.content.SharedPreferences;

import com.example.gamie.GamieApplication;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    private static final String DELIMITER = ";";
    public static final String USER_GAME_PREFERENCE = "game_pref";

    public static List<Integer> getUserGamePrefences() {
        SharedPreferences sharedPreferences = GamieApplication.getInstance().getPreferences(GamieApplication.USER_PREFERENCES);
        return UserPreferences.parseGamePref(sharedPreferences.getString(USER_GAME_PREFERENCE, null));
    }

    public static void setUserGamePreferences(List<Integer> gamePrefs) {
        SharedPreferences.Editor sharedPreferences = GamieApplication.getInstance().getPreferences(GamieApplication.USER_PREFERENCES).edit();
        String newPrefs = UserPreferences.parseGamePref(gamePrefs);
        sharedPreferences.putString(USER_GAME_PREFERENCE, newPrefs);
        sharedPreferences.apply();
        sharedPreferences.commit();
    }

    public static void appendUserGamePreferences(Integer gamePref) {
        SharedPreferences sharedPreferences = GamieApplication.getInstance().getPreferences(GamieApplication.USER_PREFERENCES);
        SharedPreferences.Editor editor = GamieApplication.getInstance().getPreferences(GamieApplication.USER_PREFERENCES).edit();
        String newPrefs = sharedPreferences.getString(USER_GAME_PREFERENCE, "") + DELIMITER +  UserPreferences.parseGamePref(gamePref);
        editor.putString(USER_GAME_PREFERENCE, newPrefs);
        editor.apply();
        editor.commit();
    }

    private static List<Integer> parseGamePref(String gamePrefs) {
        List<Integer> gamePrefList = new ArrayList<>();

        if (gamePrefs != null) {
            String[] gamePrefArr = gamePrefs.split(DELIMITER);
            for (String gamePref : gamePrefArr) {
                try {
                    gamePrefList.add(Integer.parseInt(gamePref));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return gamePrefList;
    }

    private static String parseGamePref(List<Integer> gamePrefs) {
        String gamePrefsStr = "";

        if (gamePrefs != null) {
            for (Integer pref : gamePrefs) {
                if (gamePrefs.indexOf(pref) != 0) {
                    gamePrefsStr += String.format("%s%d", DELIMITER, pref);
                } else {
                    gamePrefsStr += String.format("%d", pref);
                }
            }
        }

        return gamePrefsStr;
    }

    private static String parseGamePref(Integer gamePref) {
        return UserPreferences.parseGamePref(new ArrayList<Integer>() {
            { add(gamePref); }
        });
    }
}
