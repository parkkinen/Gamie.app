package com.example.gamie.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IGDBScreenshot {
    public Integer id;
    public String url;

    IGDBScreenshot(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.url = json.getString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<IGDBScreenshot> getScreenshots(JSONArray json) {

        List<IGDBScreenshot> screenshots = new ArrayList<>();

        for (int screenshot = 0; screenshot < json.length(); screenshot++) {
            try {
                screenshots.add(new IGDBScreenshot(json.getJSONObject(screenshot)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return screenshots;
    }
}
