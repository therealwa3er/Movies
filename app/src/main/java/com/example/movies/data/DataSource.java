package com.example.movies.data;

import com.example.movies.data.model.RepoMoviesResult;

public interface DataSource {

    RepoMoviesResult getPopularMovies();
}