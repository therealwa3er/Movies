package com.example.movies.data.paging;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.movies.data.api.MovieApiService;
import com.example.movies.data.api.NetworkState;
import com.example.movies.data.model.Movie;
import com.example.movies.data.model.MoviesResponse;
import com.example.movies.ui.movieslist.MoviesFilterType;

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

    private final MoviesFilterType sortBy;

    public RetryCallback retryCallback = null;

    public MoviePageKeyedDataSource(MovieApiService movieApiService, MoviesFilterType sortBy) {
        this.movieApiService = movieApiService;
        this.sortBy = sortBy;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Movie> callback) {

        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        // load data from API
        // but before that check filtering option
        Call<MoviesResponse> request;
        if (sortBy == MoviesFilterType.POPULAR) {
            request = movieApiService.getPopularMovies(FIRST_PAGE);
        } else {
            request = movieApiService.getTopRatedMovies(FIRST_PAGE);
        }

        // we execute sync since this is triggered by refresh
        try {
            Response<MoviesResponse> response = request.execute();
            MoviesResponse data = response.body();
            List<Movie> movieList = data != null ? data.getMovies() : Collections.<Movie>emptyList();

            retryCallback = null;
            networkState.postValue(NetworkState.LOADED);
            initialLoad.postValue(NetworkState.LOADED);

            callback.onResult(movieList, null, FIRST_PAGE + 1);
        } catch (IOException e) {

            e.printStackTrace();
            retryCallback = new RetryCallback() {
                @Override
                public void invoke() {
                    loadInitial(params, callback);
                }
            };
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
        Call<MoviesResponse> request;
        if (sortBy == MoviesFilterType.POPULAR) {
            request = movieApiService.getPopularMovies(params.key);
        } else {
            request = movieApiService.getTopRatedMovies(params.key);
        }

        request.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse data = response.body();
                    List<Movie> movieList =
                            data != null ? data.getMovies() : Collections.<Movie>emptyList();

                    retryCallback = null;
                    callback.onResult(movieList, params.key + 1);
                    networkState.postValue(NetworkState.LOADED);
                } else {
                    retryCallback = new RetryCallback() {
                        @Override
                        public void invoke() {
                            loadAfter(params, callback);
                        }
                    };
                    networkState.postValue(
                            NetworkState.error("error code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                retryCallback = new RetryCallback() {
                    @Override
                    public void invoke() {
                        loadAfter(params, callback);
                    }
                };
                networkState.postValue(
                        NetworkState.error(t != null ? t.getMessage() : "unknown error"));
            }
        });
    }

    public interface RetryCallback {
        void invoke();
    }
}