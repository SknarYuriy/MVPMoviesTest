package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    private long id;
    private String title;
    private String overview;
    private String posterPath;

    public Movie(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.title = jsonObject.getString("title");
            this.overview = jsonObject.getString("overview");
            this.posterPath = jsonObject.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
