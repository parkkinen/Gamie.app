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
import java.util.ArrayList;
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
    public static final String RELEASEDATES_POSTFIX = "release_dates";
    public static final int MAX_OFFSET = 150;
    public static final int MAX_LIMIT = 50;

    public IGDBDataFetcher(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.onError = null;
    }

    public IGDBDataFetcher(Context context, OnError onError) {
        this.queue = Volley.newRequestQueue(context);
        this.onError = onError;
    }


    /* Public Interfaces */

    public interface OnGetGames {
        void games(List<IGDBGame> games, String tag);
    }

    public interface OnGetGenres {
        void genres(List<IGDBGenre> genres, String tag);
    }

    public interface OnGetGameModes {
        void gameModes(List<IGDBGameMode> gameModes, String tag);
    }

    public interface OnGetPlatforms {
        void platforms(List<IGDBPlatform> platforms, String tag);
    }

    public interface OnGetReleaseDates {
        void releaseDates(List<IGDBReleaseDate> releaseDates, String tag);
    }

    public interface OnError {
        void error(String error, String tag);
    }

    private OnError onError;

    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    private RequestQueue queue;

    /* Public methods */

    // Get games from IGDB api. Calls set OnRequest interfaces foundGames function once finished. Calls error if one occurred during search.
    // onGetGames: OnGetGames interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetGames interface and you want to return the data to correct object
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getGames(@Nullable OnGetGames onGetGames, @Nullable String tag, String... options) {
        String[] gamesOptions = this.combineOptions(new String[]{
                "fields *, game_modes.name, genres.name, screenshots.url, platforms.name, cover.url",
                "exclude aggregated_rating,aggregated_rating_count, updated_at, external_games"
        }, options);
        this.apiPost(IGDBDataFetcher.GAMES_POSTFIX,
                response -> {
                    if (onGetGames != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetGames.games(IGDBGame.getGames(jsonArr), tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (this.onError != null) {
                                this.onError.error(e.toString(), tag);
                            }
                        }
                    }
                } ,
                error -> {
                    error.printStackTrace();
                    if (this.onError != null) {
                        this.onError.error(error.toString(), tag);
                    }
                }, gamesOptions);
    }

    // Get genres from IGDB api. Calls given OnGetGenres interface once finished. Calls error if one occurred during search.
    // onGetGenres: OnGetGenres interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetGenres interface and you want to return the data to correct object
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getGenres(@Nullable OnGetGenres onGetGenres, @Nullable String tag, String... options) {
        String[] genreOptions = this.combineOptions(new String[]{"fields *"}, options);
        this.apiPost(IGDBDataFetcher.GENRES_POSTFIX,
                response -> {
                    if (onGetGenres != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetGenres.genres(IGDBGenre.getGenres(jsonArr), tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (this.onError != null) {
                                this.onError.error(e.toString(), tag);
                            }
                        }
                    }
                } ,
                error -> {
                    error.printStackTrace();
                    if (this.onError != null) {
                        this.onError.error(error.toString(), tag);
                    }
                }, genreOptions);
    }

    // Get GameModes from IGDB api. Calls given OnGetGameModes interface once finished. Calls error if one occurred during search.
    // onGetGameModes: OnGetGameModes interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetGameModes interface and you want to return the data to correct object
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getGameModes(@Nullable OnGetGameModes onGetGameModes, @Nullable String tag, String... options) {
        String[] gamesOptions = Stream.of(options, new String[] {
                "fields *"
        }).flatMap(Stream::of).toArray(String[]::new);
        this.apiPost(IGDBDataFetcher.GAMEMODES_POSTFIX,
                response -> {
                    if (onGetGameModes != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetGameModes.gameModes(IGDBGameMode.getGameModes(jsonArr), tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (this.onError != null) {
                                this.onError.error(e.toString(), tag);
                            }
                        }
                    }
                } ,
                error -> {
                    error.printStackTrace();
                    if (this.onError != null) {
                        this.onError.error(error.toString(), tag);
                    }
                }, gamesOptions);
    }

    // Get Platforms from IGDB api. Calls given OnGetPlatforms interface once finished. Calls error if one occurred during search.
    // onGetPlatforms: OnGetPlatforms interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetPlatforms interface and you want to return the data to correct object
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getPlatforms(@Nullable OnGetPlatforms onGetPlatforms, @Nullable String tag, String... options) {
        String[] platformOptions = this.combineOptions(new String[]{"fields *"}, options);
        this.apiPost(IGDBDataFetcher.PLATFORMS_POSTFIX,
                response -> {
                    if (onGetPlatforms != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetPlatforms.platforms(IGDBPlatform.getPlatforms(jsonArr), tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (this.onError != null) {
                                this.onError.error(e.toString(), tag);
                            }
                        }
                    }
                } ,
                error -> {
                    error.printStackTrace();
                    if (this.onError != null) {
                        this.onError.error(error.toString(), tag);
                    }
                }, platformOptions);
    }

    // Get Release dates from IGDB api. Calls given OnGetReleaseDates interface once finished. Calls error if one occurred during search.
    // onGetReleaseDates: OnGetReleaseDates interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetReleaseDates interface and you want to return the data to correct object
    // options: optional strings passed to the query which can be used to filter the result. (Follow IGDB documentation) eg. "where id = 0;"
    public void getReleaseDates(@Nullable OnGetReleaseDates onGetReleaseDates, @Nullable String tag, String... options) {
        String[] rdOptions = this.combineOptions(new String[]{"fields *"}, options);
        this.apiPost(IGDBDataFetcher.RELEASEDATES_POSTFIX,
                response -> {
                    if (onGetReleaseDates != null) {
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            onGetReleaseDates.releaseDates(IGDBReleaseDate.getReleaseDates(jsonArr), tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (this.onError != null) {
                                this.onError.error(e.toString(), tag);
                            }
                        }
                    }
                } ,
                error -> {
                    error.printStackTrace();
                    if (this.onError != null) {
                        this.onError.error(error.toString(), tag);
                    }
                }, rdOptions);
    }

    // Gets upcoming games from IGDB api by first performing query to search game ids that are upcoming. Then the ids are concatenated into an games query.
    // onGetGames: OnGetGames interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetGames interface and you want to return the data to correct object
    // limit: amount of upcoming games to fetch
    // offset: how much to offset the query from newest to oldest
    public void getUpcomingGames(@Nullable OnGetGames onGetGames, @Nullable String tag, int limit, int offset, IGDBPlatform.PlatformType platformType) {
        IGDBDataFetcher fetcher = this;
        this.getReleaseDates(new OnGetReleaseDates() {
            @Override
            public void releaseDates(List<IGDBReleaseDate> releaseDates, String tag) {
                String whereGameIdOption = "where id = (";

                for (IGDBReleaseDate releaseDate : releaseDates) {
                    if (releaseDate.gameId != null) {
                        whereGameIdOption += releaseDate.gameId;
                        if (releaseDates.indexOf(releaseDate) != releaseDates.size() - 1) {
                            whereGameIdOption += ",";
                        }
                    }
                }
                whereGameIdOption += ")";
                fetcher.getGames(onGetGames, tag, whereGameIdOption);
            }
        }, tag, String.format("where date > %d & platform = %d; sort date desc", System.currentTimeMillis(), platformType.getType()), String.format("offset %d", offset), String.format("limit %d", limit));
    }

    // Gets games that have their release date set close to present from IGDB api by first performing query to search game ids that are upcoming. Then the ids are concatenated into an games query.
    // onGetGames: OnGetGames interface defines what will be done with the result
    // tag: tag is an string used for identifying which query result was called (Mainly used if one class uses single OnGetGames interface and you want to return the data to correct object
    // limit: amount of upcoming games to fetch
    // offset: how much to offset the query from newest to oldest
    public void getNewGames(@Nullable OnGetGames onGetGames, @Nullable String tag, int limit, int offset, IGDBPlatform.PlatformType platformType) {
        IGDBDataFetcher fetcher = this;
        this.getReleaseDates(new OnGetReleaseDates() {
            @Override
            public void releaseDates(List<IGDBReleaseDate> releaseDates, String tag) {
                String whereGameIdOption = "where id = (";

                for (IGDBReleaseDate releaseDate : releaseDates) {
                    if (releaseDate.gameId != null) {
                        whereGameIdOption += releaseDate.gameId;
                        if (releaseDates.indexOf(releaseDate) != releaseDates.size() - 1) {
                            whereGameIdOption += ",";
                        }
                    }
                }
                whereGameIdOption += ")";
                fetcher.getGames(onGetGames, tag, whereGameIdOption);
            }
        }, tag, String.format("where date < %d & platform = %d; sort date asc", System.currentTimeMillis(), platformType.getType()), String.format("offset %d", offset), String.format("limit %d", limit));
    }

    /* Private methods */

    private void apiPost(String endpoint, Response.Listener<String> onResponse, Response.ErrorListener onError, String... options) {
        String body = "";
        for (String option : options) {
            body += option + ";";
        }
        final String finalBody = body;
        StringRequest request = new StringRequest(Request.Method.POST, IGDBDataFetcher.API_URL + endpoint, onResponse, onError
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
        queue.add(request);
    }

    private String[] combineOptions(String[] options, String... optionals) {
        return Stream.of(optionals, options).flatMap(Stream::of).toArray(String[]::new);
    }
}


