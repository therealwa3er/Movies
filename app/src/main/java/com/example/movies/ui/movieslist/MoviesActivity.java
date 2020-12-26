package com.example.movies.ui.movieslist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.data.api.NetworkState;
import com.example.movies.data.model.Movie;
import com.example.movies.utils.GlideApp;
import com.example.movies.utils.GlideRequests;
import com.example.movies.utils.Injection;
import com.example.movies.utils.ViewModelFactory;

public class MoviesActivity extends AppCompatActivity {

    MoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = obtainViewModel();
        setupListAdapter();
    }

    private MoviesViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance(Injection.provideMovieRepository());
        return ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_movie_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        GlideRequests glideRequests = GlideApp.with(this);
        final MoviesAdapter moviesAdapter = new MoviesAdapter(glideRequests, viewModel);
        recyclerView.setAdapter(moviesAdapter);

        // observe paged list
        viewModel.getPagedList().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                moviesAdapter.submitList(movies);
            }
        });

        // observe network state
        viewModel.getNetWorkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                moviesAdapter.setNetworkState(networkState);
            }
        });
    }
}