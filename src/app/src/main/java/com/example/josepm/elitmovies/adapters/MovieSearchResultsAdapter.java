package com.example.josepm.elitmovies.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.josepm.elitmovies.R;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnMovieSearchResultClickCallback;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.MovieSearchResult;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchResultsAdapter extends RecyclerView.Adapter<MovieSearchResultsAdapter.SearchResultsViewHolder> {

    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private List<MovieSearchResult> movieSearchResults;
    private List<Genre> allGenres;
    private OnMovieSearchResultClickCallback callback;

    public MovieSearchResultsAdapter(List<MovieSearchResult> movieSearchResults, List<Genre> allGenres, OnMovieSearchResultClickCallback callback) {
        this.movieSearchResults = movieSearchResults;
        this.allGenres = allGenres;
        this.callback = callback;
    }

    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_result_item, viewGroup, false);
        return new SearchResultsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder searchResultsViewHolder, int i) {
        final MovieSearchResult movieSearchResult = movieSearchResults.get(i);
        searchResultsViewHolder.title.setText(movieSearchResult.getTitle());
        searchResultsViewHolder.releaseDate.setText(movieSearchResult.getReleaseDate().split("-")[0]);
        searchResultsViewHolder.rating.setText(movieSearchResult.getVoteAverage());
        searchResultsViewHolder.genres.setText(getGenres(movieSearchResult.getGenreIds()));
        Glide.with(searchResultsViewHolder.itemView)
                .load(IMAGE_BASE_URL + movieSearchResult.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(searchResultsViewHolder.poster);
        searchResultsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(movieSearchResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieSearchResults.size();
    }

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

    class SearchResultsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView releaseDate;
        TextView rating;
        TextView genres;
        ImageView poster;
        View itemView;

        public SearchResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.search_resut_title);
            releaseDate = itemView.findViewById(R.id.search_result_release_date);
            rating = itemView.findViewById(R.id.search_result_rating);
            genres = itemView.findViewById(R.id.search_result_genre);
            poster = itemView.findViewById(R.id.search_result_poster);
        }

    }

}
