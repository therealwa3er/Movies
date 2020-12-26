package com.example.movies.data.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.movies.data.api.MovieApiService;
import com.example.movies.data.model.Movie;
import com.example.movies.ui.movieslist.MoviesFilterType;

/**
 * data source factory to see the last created data source.
 */

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    public  MutableLiveData<MoviePageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    private final MovieApiService movieApiService;
    private final MoviesFilterType sortBy;

    public MovieDataSourceFactory(MovieApiService movieApiService, MoviesFilterType sortBy) {
        this.movieApiService = movieApiService;
        this.sortBy = sortBy;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MoviePageKeyedDataSource movieDataSource = new MoviePageKeyedDataSource(movieApiService, sortBy);
        sourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}