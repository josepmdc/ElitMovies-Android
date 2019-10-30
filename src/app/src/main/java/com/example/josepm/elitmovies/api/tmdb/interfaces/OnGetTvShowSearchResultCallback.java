package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.TvShow;
import com.example.josepm.elitmovies.api.tmdb.models.TvShowSearchResponse;

import java.util.List;

import retrofit2.Call;

public interface OnGetTvShowSearchResultCallback {

    void onSuccess(List<TvShow> tvShowSearchResults);

    void onError(Call<TvShowSearchResponse> call, Throwable t);

}
