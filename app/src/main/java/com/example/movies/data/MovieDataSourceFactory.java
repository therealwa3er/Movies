package com.example.movies.data;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.movies.data.model.Movie;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    MutableLiveData<MoviePageKeyedDataSource> moviesDataSourceLiveData = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Movie> create() {
        return null;
    }
}