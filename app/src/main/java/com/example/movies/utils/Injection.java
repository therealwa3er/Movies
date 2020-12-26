package com.example.movies.utils;

import com.example.movies.data.MovieRepository;
import com.example.movies.data.api.ApiClient;

public class Injection {

    public static MovieRepository provideMovieRepository() {
        return new MovieRepository(ApiClient.getInstance(), AppExecutors.getInstance());
    }
}