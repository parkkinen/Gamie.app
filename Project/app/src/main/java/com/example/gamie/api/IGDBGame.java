package com.example.gamie.api;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IGDBGame implements Serializable {
    public Integer id = null;
    public String name = null;
    public Long created_at = null;
    public String slug = null;
    public String summary = null;
    public List<IGDBGenre> genres = null;
    public List<IGDBPlatform> platforms = null;
    public List<IGDBGameMode> gameModes = null;
    public List<IGDBScreenshot> screenshots = null;
    public List<Integer> similiarGames = null;
    public Double rating = null;
    public Double popularity = null;
    public IGDBScreenshot coverArt = null;

    IGDBGame(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
            this.created_at = json.getLong("created_at");

            if (json.has("slug")) {
                this.slug = json.getString("slug");
            }

            if (json.has("summary")) {
                this.summary = json.getString("summary");
            }

            if (json.has("rating")) {
                this.rating = json.getDouble("rating");
            }

            if (json.has("popularity")) {
                this.popularity = json.getDouble("popularity");
            }

            if (json.has("genres")) {
                this.genres = IGDBGenre.getGenres(json.getJSONArray("genres"));
            }

            if (json.has("platforms")) {
                this.platforms = IGDBPlatform.getPlatforms(json.getJSONArray("platforms"));
            }

            if (json.has("game_modes")) {
                this.gameModes = IGDBGameMode.getGameModes(json.getJSONArray("game_modes"));
            }


            if (json.has("screenshots")) {
                this.screenshots = IGDBScreenshot.getScreenshots(json.getJSONArray("screenshots"));
            }

            if (json.has("cover")) {
                JSONObject coverJson = json.getJSONObject("cover");
                this.coverArt = new IGDBScreenshot(coverJson);
            }

            if (json.has("similiar_games")) {
                JSONArray similiarGamesArr = json.getJSONArray("similiar_games");
                if (similiarGamesArr.length() > 0) {
                    this.similiarGames = new ArrayList<>();
                    for (int similiar = 0; similiar < similiarGamesArr.length(); similiar++) {
                        JSONObject similiarGame = similiarGamesArr.getJSONObject(similiar);
                        this.similiarGames.add(similiarGame.getInt("name"));
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    static List<IGDBGame> getGames(JSONArray gamesArray) {
        List<IGDBGame> games = new ArrayList<>();

        for (int game = 0; game < gamesArray.length(); game++) {
            try {
                games.add(new IGDBGame(gamesArray.getJSONObject(game)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return games;
    }
}
