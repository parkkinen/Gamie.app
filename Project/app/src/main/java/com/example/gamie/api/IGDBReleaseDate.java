package com.example.gamie.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IGDBReleaseDate {
    public Integer id;
    public Long date;
    public Integer gameId;

    IGDBReleaseDate(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.date = json.getLong("date");
            this.gameId = json.getInt("game");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<IGDBReleaseDate> getReleaseDates(JSONArray gamesByDateArray) {
        List<IGDBReleaseDate> gamesByDate = new ArrayList<>();

        for (int game = 0; game < gamesByDateArray.length(); game++) {
            try {
                gamesByDate.add(new IGDBReleaseDate(gamesByDateArray.getJSONObject(game)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gamesByDate;

    }
}
