package com.example.josepm.elitmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.Movie;
import com.example.josepm.elitmovies.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    //region Variable declaration
    private List<Movie> movies;
    private List<Genre> allGenres;
    private String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    // endregion

    //Constructor
    public MoviesAdapter(List<Movie> movies, List<Genre> allGenres) {
        this.movies = movies;
        this.allGenres = allGenres;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    // region MovieViewHolder class
    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView poster;
        TextView releaseDate;
        TextView rating;

        // Constructor
        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_movie_title);
            poster = itemView.findViewById(R.id.item_movie_poster);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            rating = itemView.findViewById(R.id.item_movie_rating);
        }

        // It binds the data to the components in the View
        public void bind(Movie movie) {
            title.setText(StringUtils.abbreviate(movie.getTitle(), 13));
            rating.setText(String.valueOf(movie.getRating()));
            releaseDate.setText(String.valueOf(movie.getReleaseDate().split("-")[0]));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }

        // Gets the genres for the movie
        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
    }
    // endregion
}