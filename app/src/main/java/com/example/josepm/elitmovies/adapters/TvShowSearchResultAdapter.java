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
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnTvShowsClickCallback;
import com.example.josepm.elitmovies.api.tmdb.models.Genre;
import com.example.josepm.elitmovies.api.tmdb.models.TvShow;

import java.util.ArrayList;
import java.util.List;

public class TvShowSearchResultAdapter extends RecyclerView.Adapter<TvShowSearchResultAdapter.SearchResultsViewHolder> {

    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private List<TvShow> tvShowsSearchResults;
    private List<Genre> allGenres;
    private OnTvShowsClickCallback callback;

    public TvShowSearchResultAdapter(List<TvShow> tvShowsSearchResults, List<Genre> allGenres, OnTvShowsClickCallback callback) {
        this.tvShowsSearchResults = tvShowsSearchResults;
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
        final TvShow tvShowSearchResult = tvShowsSearchResults.get(i);
        searchResultsViewHolder.title.setText(tvShowSearchResult.getName());
        searchResultsViewHolder.releaseDate.setText(tvShowSearchResult.getFirstAirDate().split("-")[0]);
        //searchResultsViewHolder.rating.setText(Math.round(tvShowSearchResult.getRating()));
        searchResultsViewHolder.genres.setText(getGenres(tvShowSearchResult.getGenreIds()));
        Glide.with(searchResultsViewHolder.itemView)
                .load(IMAGE_BASE_URL + tvShowSearchResult.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(searchResultsViewHolder.poster);
        searchResultsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(tvShowSearchResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShowsSearchResults.size();
    }

    private String getGenres(List<Integer> genreIds) {
        List<String> tvShowGenres = new ArrayList<>();
        for (Integer genreId : genreIds) {
            for (Genre genre : allGenres) {
                if (genre.getId() == genreId) {
                    tvShowGenres.add(genre.getName());
                    break;
                }
            }
        }
        return TextUtils.join(", ", tvShowGenres);
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
