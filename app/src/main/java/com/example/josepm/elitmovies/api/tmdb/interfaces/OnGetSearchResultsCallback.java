package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.SearchResponse;
import com.example.josepm.elitmovies.api.tmdb.models.SearchResult;

import java.util.List;

import retrofit2.Call;

public interface OnGetSearchResultsCallback {

    void onSuccess(List<SearchResult> searchResults);

    void onError(Call<SearchResponse> call, Throwable t);

}
