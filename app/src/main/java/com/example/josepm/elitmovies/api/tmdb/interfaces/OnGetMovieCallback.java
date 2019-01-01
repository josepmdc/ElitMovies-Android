package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.Movie;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}