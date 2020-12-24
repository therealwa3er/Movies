package com.example.movies.data.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApiService {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies();

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies();

}
