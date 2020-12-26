package com.example.movies.ui.moviedetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.movies.R;
import com.example.movies.data.model.Movie;
import com.example.movies.databinding.ActivityDetailsBinding;
import com.example.movies.utils.Constants;
import com.example.movies.utils.GlideApp;
import com.example.movies.utils.Injection;
import com.example.movies.utils.ViewModelFactory;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private static final int DEFAULT_ID = -1;

    private ActivityDetailsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        long movieId = intent.getLongExtra(EXTRA_MOVIE_ID, DEFAULT_ID);
        if (movieId == DEFAULT_ID) {
            closeOnError();
        }

        MovieDetailsViewModel viewModel = obtainViewModel();

        if (savedInstanceState == null) {
            // trigger loading movie details, only once the activity created
            viewModel.setMovieId(movieId);
        }
        viewModel.getMovieLiveData().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                updateUi(movie);
            }
        });

    }

    private void updateUi(Movie movie) {
        // movie backdrop
        GlideApp.with(this)
                .load(Constants.BACKDROP_URL + movie.getBackdrop())
                .into(mBinding.imageMovieBackdrop);

        // movie poster
        GlideApp.with(this)
                .load(Constants.IMAGE_URL + movie.getImageUrl())
                .into(mBinding.imagePoster);

        // movie title
        mBinding.textTitle.setText(movie.getTitle());

        // movie release date
        mBinding.textReleaseDate.setText(movie.getReleaseDate());

        // movie overview
        mBinding.textOverview.setText(movie.getOverview());
    }

    private MovieDetailsViewModel obtainViewModel() {
        ViewModelFactory factory = ViewModelFactory.getInstance(Injection.provideMovieRepository());
        return ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);
    }

    private void closeOnError() {
        throw new RuntimeException("Access denied.");
    }

}