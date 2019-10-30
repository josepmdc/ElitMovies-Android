package com.example.josepm.elitmovies.api.tmdb;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetMovieSearchResultsCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTvShowSearchResultCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.TMDbApi;
import com.example.josepm.elitmovies.api.tmdb.models.MovieSearchResponse;
import com.example.josepm.elitmovies.api.tmdb.models.TvShowSearchResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRepository {

    private static SearchRepository searchRepository;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String LANGUAGE = Locale.getDefault().getLanguage();
    private static final String TMDB_API_KEY = "1b5adf76a72a13bad99b8fc0c68cb085";
    private TMDbApi api;

    public SearchRepository(TMDbApi api) {
        this.api = api;
    }

    public static SearchRepository getInstance() {

        if (searchRepository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            searchRepository = new SearchRepository(retrofit.create(TMDbApi.class));
        }

        return searchRepository;

    }

    public void getMovieSearchResults(String query, final OnGetMovieSearchResultsCallback callback) {
        api.getMovieSearch(TMDB_API_KEY, LANGUAGE, query)
                .enqueue(new Callback<MovieSearchResponse>() {
                    @Override
                    public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                        if (response.isSuccessful()) {
                            MovieSearchResponse movieSearchResponse = response.body();
                            if (movieSearchResponse != null) {
                                callback.onSuccess(movieSearchResponse.getMovieSearchResults());
                            } else {
                                Log.e("ERROR", "getMovieSearchResults: No hay resultados");
                            }
                        } else {
                            Log.e("ERROR", " getMovieSearchResults: Fallo en la petición");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                        callback.onError(call, t);
                    }
                });
    }

    public void getTvShowSearchResults(String query, final OnGetTvShowSearchResultCallback callback) {
        api.getTvShowSearch(TMDB_API_KEY, LANGUAGE, query)
                .enqueue(new Callback<TvShowSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TvShowSearchResponse> call, @NonNull Response<TvShowSearchResponse> response) {
                        if (response.isSuccessful()) {
                            TvShowSearchResponse tvShowSearchResponse = response.body();
                            if (tvShowSearchResponse != null) {
                                callback.onSuccess(tvShowSearchResponse.gettvShowSearchResults());
                            } else {
                                Log.e("ERROR", "getTvShowSearchResults: No hay resultados");
                            }
                        } else {
                            Log.e("ERROR", " getTvShowSearchResults: Fallo en la petición");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TvShowSearchResponse> call, @NonNull Throwable t) {
                        callback.onError(call, t);
                    }
                });
    }


}
