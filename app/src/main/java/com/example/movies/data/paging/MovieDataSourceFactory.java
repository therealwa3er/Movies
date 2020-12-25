package com.example.movies.data.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.movies.data.api.MovieApiService;
import com.example.movies.data.model.Movie;

/**
 * data source factory to see the last created data source.
 */

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<MoviePageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    private final MovieApiService movieApiService;

    public MovieDataSourceFactory(MovieApiService movieApiService) {
        this.movieApiService = movieApiService;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MoviePageKeyedDataSource movieDataSource = new MoviePageKeyedDataSource(movieApiService);
        sourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}