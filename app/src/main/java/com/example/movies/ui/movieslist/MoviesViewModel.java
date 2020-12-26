package com.example.movies.ui.movieslist;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.movies.R;
import com.example.movies.data.MovieRepository;
import com.example.movies.data.api.NetworkState;
import com.example.movies.data.model.Movie;
import com.example.movies.data.model.RepoMoviesResult;

public class MoviesViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    private LiveData<RepoMoviesResult> repoMoviesResult;

    private LiveData<PagedList<Movie>> pagedList;

    private LiveData<NetworkState> networkState;
    private MutableLiveData<MoviesFilterType> sortBy = new MutableLiveData<>();

    public MoviesViewModel(final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        // By default show popular movies
        sortBy.setValue(MoviesFilterType.POPULAR);
        repoMoviesResult = Transformations.map(sortBy, new Function<MoviesFilterType, RepoMoviesResult>() {
            @Override
            public RepoMoviesResult apply(MoviesFilterType sort) {
                return movieRepository.getFilteredMoviesBy(sort);
            }
        });
        pagedList = Transformations.switchMap(repoMoviesResult,
                new Function<RepoMoviesResult, LiveData<PagedList<Movie>>>() {
                    @Override
                    public LiveData<PagedList<Movie>> apply(RepoMoviesResult input) {
                        return input.data;
                    }
                });
        networkState = Transformations.switchMap(repoMoviesResult,
                new Function<RepoMoviesResult, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(RepoMoviesResult input) {
                        return input.networkState;
                    }
                });
    }

    LiveData<PagedList<Movie>> getPagedList() {
        return pagedList;
    }

    LiveData<NetworkState> getNetWorkState() {
        return networkState;
    }

    MoviesFilterType getCurrentSorting() {
        return sortBy.getValue();
    }

    void setSortMoviesBy(int id) {
        MoviesFilterType sort = null;
        switch (id) {
            case R.id.action_popular_movies: {
                // check if already selected. no need to request API
                if (sortBy.getValue() == MoviesFilterType.POPULAR)
                    return;

                sort = MoviesFilterType.POPULAR;
                break;
            }
            case R.id.action_top_rated: {
                if (sortBy.getValue() == MoviesFilterType.TOP_RATED)
                    return;

                sort = MoviesFilterType.TOP_RATED;
                break;
            }
        }
        sortBy.setValue(sort);
    }

    // retries any failed requests.
    void retry() {
        repoMoviesResult.getValue().sourceLiveData.getValue().retryCallback.invoke();
    }
}