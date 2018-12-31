package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.TvShow;

import java.util.List;

public interface OnGetTvShowCallback {

    void onSuccess(int page, List<TvShow> tvShows);

    void onError();

}
