package com.example.movies.data;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.movies.data.api.MovieApiService;
import com.example.movies.data.model.Movie;
import com.example.movies.data.paging.MovieDataSourceFactory;
import com.example.movies.utils.AppExecutors;

public class MovieRepository implements DataSource {

    private static final int PAGE_SIZE = 20;

    private LiveData<PagedList<Movie>> moviesList;

    private final MovieApiService mMovieApiService;

    private final AppExecutors mExecutors;

    public MovieRepository(MovieApiService movieApiService,
                           AppExecutors executors) {
        mMovieApiService = movieApiService;
        mExecutors = executors;
    }

    @Override
    public LiveData<PagedList<Movie>> getPopularMovies() {

        MovieDataSourceFactory sourceFactory = new MovieDataSourceFactory(mMovieApiService);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        moviesList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        return moviesList;
    }
}