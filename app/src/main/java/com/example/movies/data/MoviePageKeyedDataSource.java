package com.example.movies.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.movies.data.model.Movie;

/**
 *  A data source that uses the before/after keys returned in page requests.
 */
public class MoviePageKeyedDataSource extends PageKeyedDataSource<Integer, Movie> {

    MutableLiveData<String> loadDataState = new MutableLiveData<>();

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        loadDataState.postValue("LOADING");

        // load data from API

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }
}