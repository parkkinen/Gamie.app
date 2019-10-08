package com.example.gamie.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IGDBScreenshot {
    public Integer id;
    public String url;

    public enum IGDBScreenshotSize {
        COVER_SMALL("t_cover_small"),
        SCREENSHOT_MED("t_screenshot_med"),
        COVER_BIG("t_cover_big"),
        LOGO_MED("t_logo_med"),
        SCREENSHOT_BIG("t_screenshot_big"),
        SCREENSHOT_HUGE("t_screenshot_huge"),
        THUMB("t_thumb"),
        MICRO("t_micro"),
        HD("t_720p"),
        FHD("t_1080p");

        private String size;

        IGDBScreenshotSize(String screenSize) {
            this.size = screenSize;
        }

        public String getSize() {
            return size;
        }
    }

    IGDBScreenshot(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.url = json.getString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageUrl(IGDBScreenshotSize size) {
        String url = this.url.substring(2);
        url.replace(IGDBScreenshotSize.THUMB.getSize(), size.getSize());
        return String.format("https://%s", url);
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
