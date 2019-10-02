package com.example.gamie.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IGDBGenre {
    public Integer id;
    public String name;

    IGDBGenre(JSONObject json) {
       try {
           this.id = json.getInt("id");
           this.name = json.getString("name");
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    static List<IGDBGenre> getGenres(JSONArray json) {

        List<IGDBGenre> genres = new ArrayList<>();

        for (int genre = 0; genre < json.length(); genre++) {
            try {
                genres.add(new IGDBGenre(json.getJSONObject(genre)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return genres;
    }
}
