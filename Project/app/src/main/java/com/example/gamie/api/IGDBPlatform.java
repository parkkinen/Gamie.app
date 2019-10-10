package com.example.gamie.api;

import android.drm.DrmStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IGDBPlatform implements Serializable {
    public Integer id;
    public String name;

    public enum PlatformCategory {
        CONSOLE(1),
        ARCADE(2),
        PLATFORM(3),
        OPERATING_SYSTEM(4),
        PORTABLE_CONSOLE(5),
        PC(6);

        private int category;

        PlatformCategory(int category) {
            this.category = category;
        }

        public int getCategory() {
            return this.category;
        }
    }

    public enum PlatformType {
        PC(6) { public String toString() { return "Pc"; }},
        PS4(48) { public String toString() { return "Playstation 4"; }},
        XBOX_ONE(49) { public String toString() { return "Xbox One"; }},
        MAC(14) { public String toString() { return "Mac"; }},
        NINTENDO_SWITCH(130) { public String toString() { return "Nintendo Switch"; }},
        NEW_NINTENDO_3DS(137) { public String toString() { return "Nintendo 3DS"; }};


        private int type;

        PlatformType(int type) { this.type = type; }

        public int getType() { return this.type; }

        // Returns type as int from given type string. -1 if none found.
        static public PlatformType getType(String s) {
            for (PlatformType type : PlatformType.values()) {
                if (type.toString().equals(s)) {
                    return type;
                }
            }
            return PC;
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
