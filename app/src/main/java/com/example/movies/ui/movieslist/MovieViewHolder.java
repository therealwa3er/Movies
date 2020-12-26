package com.example.movies.ui.movieslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.data.model.Movie;
import com.example.movies.databinding.ItemMovieBinding;
import com.example.movies.ui.moviedetails.DetailsActivity;
import com.example.movies.utils.GlideRequests;

import static com.example.movies.utils.Constants.IMAGE_URL;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private final ItemMovieBinding binding;
    private GlideRequests glide;

    public MovieViewHolder(@NonNull ItemMovieBinding binding, GlideRequests glide) {
        super(binding.getRoot());

        this.binding = binding;
        this.glide = glide;
    }

    void bindTo(final Movie movie) {

        // movie poster
        glide
                .load(IMAGE_URL + movie.getImageUrl())
                .placeholder(android.R.color.holo_red_dark)
                .into(binding.imageMoviePoster);

        // movie click event
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.getId());
                view.getContext().startActivity(intent);
            }
        });

        binding.executePendingBindings();
    }

    static MovieViewHolder create(ViewGroup parent, GlideRequests glide) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemMovieBinding binding =
                ItemMovieBinding.inflate(layoutInflater, parent, false);
        return new MovieViewHolder(binding, glide);
    }
}