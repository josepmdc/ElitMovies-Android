package com.example.josepm.elitmovies.adapters;

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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder> {

    //region Variable declaration
    private List<TvShow> tvShows;
    private List<Genre> allGenres;
    private String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private OnTvShowsClickCallback callback;
    // endregion

    //Constructor
    public TvShowsAdapter(List<TvShow> tvShows, List<Genre> allGenres, OnTvShowsClickCallback callback) {
        this.callback = callback;
        this.tvShows = tvShows;
        this.allGenres = allGenres;
    }

    @Override
    public TvShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_show_item, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TvShowsAdapter.TvShowViewHolder holder, int position) {
        holder.bind(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public void appendTvShows(List<TvShow> showsToAppend) {
        tvShows.addAll(showsToAppend);
        notifyDataSetChanged();
    }

    public void clearTvShows() {
        tvShows.clear();
        notifyDataSetChanged();
    }

    // region TvShowViewHolder class
    class TvShowViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView poster;
        TextView releaseDate;
        TextView rating;
        TvShow tvShow;

        // Constructor
        public TvShowViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_tv_show_name);
            poster = itemView.findViewById(R.id.item_tv_show_poster);
            releaseDate = itemView.findViewById(R.id.item_tv_show_release_date);
            rating = itemView.findViewById(R.id.item_tv_show_rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(tvShow);
                }
            });
        }

        // It binds the data to the components in the View
        public void bind(TvShow tvShow) {
            this.tvShow = tvShow;
            name.setText(StringUtils.abbreviate(tvShow.getName(), 13));
            rating.setText(String.valueOf(tvShow.getRating()));
            releaseDate.setText(String.valueOf(tvShow.getFirstAirDate().split("-")[0]));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + tvShow.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }

        // Gets the genres for the tv show
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
    }
    // endregion
}
