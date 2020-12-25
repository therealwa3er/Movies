package com.example.movies.ui.movieslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.movies.data.MovieRepository;
import com.example.movies.data.model.Movie;

public class MoviesViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    private LiveData<PagedList<Movie>> pagedList;

    public MoviesViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        pagedList = movieRepository.getPopularMovies();
    }

    public LiveData<PagedList<Movie>> getPagedList() {
        return pagedList;
    }
}