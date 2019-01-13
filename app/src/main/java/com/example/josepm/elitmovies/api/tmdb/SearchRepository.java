package com.example.josepm.elitmovies.api.tmdb;

import android.util.Log;

import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetSearchResultsCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.TMDbApi;
import com.example.josepm.elitmovies.api.tmdb.models.SearchResponse;

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

    public void getSearchResults(String query, final OnGetSearchResultsCallback callback) {
        api.getSearch(TMDB_API_KEY, LANGUAGE, query)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if (response.isSuccessful()) {
                            SearchResponse searchResponse = response.body();
                            if (searchResponse != null) {
                                callback.onSuccess(searchResponse.getSearchResults());
                            } else {
                                Log.e("ERROR getSearchResults", "No hay resultados");
                            }
                        } else {
                            Log.e("ERROR getSearchResults", "Fallo en la petición");
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        callback.onError(call, t);
                    }
                });
    }


}
