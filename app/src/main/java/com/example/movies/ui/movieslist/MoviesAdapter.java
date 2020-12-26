package com.example.movies.ui.movieslist;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.data.model.Movie;
import com.example.movies.utils.GlideRequests;

public class MoviesAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";
    public static final String IMAGE_URL = IMAGE_BASE_URL + IMAGE_SIZE;

    private GlideRequests glide;

    public MoviesAdapter(GlideRequests glide) {
        super(MOVIE_COMPARATOR);

        this.glide = glide;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MovieViewHolder.create(parent, glide);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = getItem(position);
        ((MovieViewHolder) holder).bindTo(movie);
    }

    private static DiffUtil.ItemCallback<Movie> MOVIE_COMPARATOR = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };
}