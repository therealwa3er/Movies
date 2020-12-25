package com.example.movies.data;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.movies.data.api.MovieApiService;
import com.example.movies.data.model.Movie;
import com.example.movies.data.model.MoviesResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  A data source that uses the before/after keys returned in page requests.
 */
public class MoviePageKeyedDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE = 1;

    public MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    public MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    private final MovieApiService movieApiService;

    public MoviePageKeyedDataSource(MovieApiService movieApiService) {
        this.movieApiService = movieApiService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<Integer, Movie> callback) {

        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        // load data from API
        // but before that check filtering option
        Call<MoviesResponse> request = movieApiService.getPopularMovies(FIRST_PAGE);
        try {
            Response<MoviesResponse> response = request.execute();
            MoviesResponse data = response.body();
            List<Movie> movieList = data != null ? data.getMovies() : Collections.<Movie>emptyList();

            networkState.postValue(NetworkState.LOADED);
            initialLoad.postValue(NetworkState.LOADED);

            callback.onResult(movieList, null, FIRST_PAGE + 1);
        } catch (IOException e) {
            e.printStackTrace();
            NetworkState error = NetworkState.error(e.getMessage());
            networkState.postValue(error);
            initialLoad.postValue(error);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);

        // load data from API
        // but before that check filtering option
        Call<MoviesResponse> request = movieApiService.getPopularMovies(params.key);

        request.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse data = response.body();
                    List<Movie> movieList =
                            data != null ? data.getMovies() : Collections.<Movie>emptyList();

                    callback.onResult(movieList, params.key + 1);
                    networkState.postValue(NetworkState.LOADED);
                } else {

                    networkState.postValue(
                            NetworkState.error("error code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                networkState.postValue(
                        NetworkState.error(t != null ? t.getMessage() : "unknown error"));
            }
        });
    }
}