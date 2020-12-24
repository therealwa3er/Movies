package com.example.movies.data;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.movies.data.model.Movie;

public interface DataSource {

    LiveData<PagedList<Movie>> getPopularMovies();
}