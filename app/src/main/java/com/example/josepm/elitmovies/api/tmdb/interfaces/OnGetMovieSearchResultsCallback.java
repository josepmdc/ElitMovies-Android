package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.MovieSearchResponse;
import com.example.josepm.elitmovies.api.tmdb.models.MovieSearchResult;

import java.util.List;

import retrofit2.Call;

public interface OnGetMovieSearchResultsCallback {

    void onSuccess(List<MovieSearchResult> movieSearchResults);

    void onError(Call<MovieSearchResponse> call, Throwable t);

}
