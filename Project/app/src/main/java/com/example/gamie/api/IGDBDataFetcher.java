package com.example.gamie.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.res.TypedArrayUtils;

import com.android.volley.AuthFailureError;
import com.example.gamie.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class IGDBDataFetcher {
    public static final String API_URL = "https://api-v3.igdb.com/";
    public static final String GAMES_POSTFIX = "games";
    public static final String GENRES_POSTFIX = "genres";
    public static final String GAMEMODES_POSTFIX = "game_modes";
    public static final String PLATFORMS_POSTFIX = "platforms";

    /* Public Interfaces */

    public interface OnGetGames {
        void games(List<IGDBGame> games);
        void error(String e);
    }

    public interface OnGetGenres {
        void genres(List<IGDBGenre> genres);
        void error(String e);
    }

    public interface OnGetGameModes {
        void gameModes(List<IGDBGameMode> gameModes);
        void error(String e);
    }

    public interface OnGetPlatforms {
        void platforms(List<IGDBPlatform> platforms);
        void error(String e);
    }

    public IGDBDataFetcher(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    private RequestQueue queue;

    /* Public methods */

    // Get games from IGDB api. Calls set OnRequest interfaces foundGames function once finished. Calls error if one occurred during search.
    // onGetGames: OnGetGames interface defines what will be done with the result
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getGames(@Nullable OnGetGames onGetGames, String... options) {
        String[] gamesOptions = Stream.of(options, new String[] {
                "fields *, game_modes.name, genres.name, screenshots.url, platforms.name, cover.url",
                "exclude aggregated_rating,aggregated_rating_count, updated_at, external_games"
        }).flatMap(Stream::of).toArray(String[]::new);
        this.apiPost(IGDBDataFetcher.GAMES_POSTFIX,
                response -> {
                    if (onGetGames != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetGames.games(IGDBGame.getGames(jsonArr));
                        } catch (Exception e) {
                            onGetGames.error(e.toString());
                        }
                    }
                } ,
                error -> {
                    if (onGetGames != null) {
                        onGetGames.error(error.toString());
                    }
                }, gamesOptions);
    }

    // Get genres from IGDB api. Calls given OnGetGenres interface once finished. Calls error if one occurred during search.
    // onGetGenres: OnGetGenres interface defines what will be done with the result
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getGenres(@Nullable OnGetGenres onGetGenres, String... options) {
        String[] gamesOptions = Stream.of(options, new String[] {
                "fields *"
        }).flatMap(Stream::of).toArray(String[]::new);
        this.apiPost(IGDBDataFetcher.GENRES_POSTFIX,
                response -> {
                    if (onGetGenres != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetGenres.genres(IGDBGenre.getGenres(jsonArr));
                        } catch (Exception e) {
                            onGetGenres.error(e.toString());
                        }
                    }
                } ,
                error -> {
                    if (onGetGenres != null) {
                        onGetGenres.error(error.toString());
                    }
                }, gamesOptions);
    }

    // Get GameModes from IGDB api. Calls given OnGetGameModes interface once finished. Calls error if one occurred during search.
    // onGetGameModes: OnGetGameModes interface defines what will be done with the result
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getGameModes(@Nullable OnGetGameModes onGetGameModes, String... options) {
        String[] gamesOptions = Stream.of(options, new String[] {
                "fields *"
        }).flatMap(Stream::of).toArray(String[]::new);
        this.apiPost(IGDBDataFetcher.GAMEMODES_POSTFIX,
                response -> {
                    if (onGetGameModes != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetGameModes.gameModes(IGDBGameMode.getGameModes(jsonArr));
                        } catch (Exception e) {
                            onGetGameModes.error(e.toString());
                        }
                    }
                } ,
                error -> {
                    if (onGetGameModes != null) {
                        onGetGameModes.error(error.toString());
                    }
                }, gamesOptions);
    }

    // Get Platforms from IGDB api. Calls given OnGetPlatforms interface once finished. Calls error if one occurred during search.
    // onGetPlatforms: OnGetPlatforms interface defines what will be done with the result
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getPlatforms(@Nullable OnGetPlatforms onGetPlatforms, String... options) {
        String[] gamesOptions = Stream.of(options, new String[] {
                "fields *"
        }).flatMap(Stream::of).toArray(String[]::new);
        this.apiPost(IGDBDataFetcher.PLATFORMS_POSTFIX,
                response -> {
                    if (onGetPlatforms != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetPlatforms.platforms(IGDBPlatform.getPlatforms(jsonArr));
                        } catch (Exception e) {
                            onGetPlatforms.error(e.toString());
                        }
                    }
                } ,
                error -> {
                    if (onGetPlatforms != null) {
                        onGetPlatforms.error(error.toString());
                    }
                }, gamesOptions);
    }

    /* Private methods */

    private void apiPost(String endpoint, Response.Listener<String> onReponse, Response.ErrorListener onError, String... options) {
        String body = "";
        for (String option : options) {
            body += option + ";";
        }
        final String finalBody = body;
        StringRequest getRequest = new StringRequest(Request.Method.POST, IGDBDataFetcher.API_URL + endpoint, onReponse, onError
        ) {
            @Override
            public String getBodyContentType() {
                return "text/plain; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user-key", BuildConfig.USER_KEY);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return finalBody == null ? null : finalBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(getRequest);
    }
}


