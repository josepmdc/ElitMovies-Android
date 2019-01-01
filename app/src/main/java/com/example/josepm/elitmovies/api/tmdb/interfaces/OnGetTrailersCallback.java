package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.Trailer;

import java.util.List;

public interface OnGetTrailersCallback {

    void onSuccess(List<Trailer> trailers);

    void onError();

}
