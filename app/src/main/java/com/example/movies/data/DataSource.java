package com.example.movies.data;

import androidx.lifecycle.MutableLiveData;

import com.example.movies.data.model.Movie;
import com.example.movies.data.model.RepoMoviesResult;
import com.example.movies.ui.movieslist.MoviesFilterType;

public interface DataSource {

    MutableLiveData<Movie> getMovie(long movieId);

    RepoMoviesResult getFilteredMoviesBy(MoviesFilterType sortBy);
}