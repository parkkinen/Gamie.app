package com.example.gamie.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IGDBPlatform {
    public Integer id;
    public String name;

    public enum PlatformType {
        CONSOLE(1),
        ARCADE(2),
        PC(6),
        PS4(48),
        XBOX(49);

        private int type;

        PlatformType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

    IGDBPlatform(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<IGDBPlatform> getPlatforms(JSONArray json) {

        List<IGDBPlatform> platforms = new ArrayList<>();

        for (int platform = 0; platform < json.length(); platform++) {
            try {
                platforms.add(new IGDBPlatform(json.getJSONObject(platform)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return platforms;
    }
}
