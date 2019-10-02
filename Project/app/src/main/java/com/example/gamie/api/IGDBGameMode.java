package com.example.gamie.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class IGDBGameMode {
    public Integer id;
    public String name;

    IGDBGameMode(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<IGDBGameMode> getGameModes(JSONArray json) {

        List<IGDBGameMode> gameModes = new ArrayList<>();

        for (int gameMode = 0; gameMode < json.length(); gameMode++) {
            try {
                gameModes.add(new IGDBGameMode(json.getJSONObject(gameMode)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gameModes;
    }
}
