package com.example.movies.ui.movieslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.movies.data.MovieRepository;
import com.example.movies.data.api.NetworkState;
import com.example.movies.data.model.Movie;
import com.example.movies.data.model.RepoMoviesResult;

public class MoviesViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    private RepoMoviesResult repoMoviesResult;

    public MoviesViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        repoMoviesResult = movieRepository.getPopularMovies();
    }

    LiveData<PagedList<Movie>> getPagedList() {
        return repoMoviesResult.data;
    }

    LiveData<NetworkState> getNetWorkState() {
        return repoMoviesResult.networkState;
    }

    // retries any failed requests.
    void retry() {
        repoMoviesResult.sourceLiveData.getValue().retryCallback.invoke();
    }
}