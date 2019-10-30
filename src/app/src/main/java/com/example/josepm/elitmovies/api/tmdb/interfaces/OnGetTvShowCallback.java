package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.TvShow;

public interface OnGetTvShowCallback {

    void onSuccess(TvShow tvShow);

    void onError();

}
