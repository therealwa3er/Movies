package com.example.movies.data;

import com.example.movies.data.model.RepoMoviesResult;
import com.example.movies.ui.movieslist.MoviesFilterType;

public interface DataSource {

    RepoMoviesResult getFilteredMoviesBy(MoviesFilterType sortBy);
}