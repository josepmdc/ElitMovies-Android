package com.example.josepm.elitmovies.api.tmdb;

import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetGenresCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTvShowCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.TMDbApi;
import com.example.josepm.elitmovies.api.tmdb.models.GenresResponse;
import com.example.josepm.elitmovies.api.tmdb.models.TvShowResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowsRepository {

    // region Variable Declaration
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String LANGUAGE = Locale.getDefault().getLanguage();
    private static final String TMDB_API_KEY = "1b5adf76a72a13bad99b8fc0c68cb085";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String AIRING_TODAY = "airing_today";
    public static final String ON_THE_AIR = "on_the_air";

    private static TvShowsRepository repository;

    private TMDbApi api;
    // endregion

    // Constructor
    private TvShowsRepository(TMDbApi api) {
        this.api = api;
    }

    // It creates the TMDBApi class with retrofit instance
    public static TvShowsRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new TvShowsRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    // region Get Data

    // Calls the api to get movies
    public void getTvShows(int page, String sortBy, final OnGetTvShowCallback callback) {
        Callback<TvShowResponse> call = new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    TvShowResponse tvShowResponse = response.body();
                    if (tvShowResponse != null && tvShowResponse.getTvShows() != null) {
                        callback.onSuccess(tvShowResponse.getPage(), tvShowResponse.getTvShows());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getPopularShows(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case AIRING_TODAY:
                api.getShowsAiringToday(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
                api.getPopularShows(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case ON_THE_AIR:
                api.getShowsOnAir(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
            default:
                api.getPopularShows(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

    // Calls the api to get all the genres
    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(TMDB_API_KEY, LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                        if (response.isSuccessful()) {
                            GenresResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    // endregion

}
