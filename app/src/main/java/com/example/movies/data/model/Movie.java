package com.example.movies.data.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String imageUrl;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("vote_average")
    private double userRating;

    @SerializedName("release_date")
    private String releaseDate;

}