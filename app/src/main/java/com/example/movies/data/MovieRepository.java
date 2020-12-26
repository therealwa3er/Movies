package com.example.movies.data;


import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.movies.data.api.MovieApiService;
import com.example.movies.data.api.NetworkState;
import com.example.movies.data.model.Movie;
import com.example.movies.data.model.RepoMoviesResult;
import com.example.movies.data.paging.MovieDataSourceFactory;
import com.example.movies.data.paging.MoviePageKeyedDataSource;
import com.example.movies.utils.AppExecutors;

public class MovieRepository implements DataSource {

    private static final int PAGE_SIZE = 20;

    private final MovieApiService mMovieApiService;

    private final AppExecutors mExecutors;

    public MovieRepository(MovieApiService movieApiService,
                           AppExecutors executors) {
        mMovieApiService = movieApiService;
        mExecutors = executors;
    }

    @Override
    public RepoMoviesResult getPopularMovies() {

        MovieDataSourceFactory sourceFactory = new MovieDataSourceFactory(mMovieApiService);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        // Get the paged list
        LiveData<PagedList<Movie>> moviesPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        LiveData<NetworkState> networkState = Transformations.switchMap(sourceFactory.sourceLiveData,
                new Function<MoviePageKeyedDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(MoviePageKeyedDataSource input) {
                        return input.networkState;
                    }
                });

        return new RepoMoviesResult(
                moviesPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }
}