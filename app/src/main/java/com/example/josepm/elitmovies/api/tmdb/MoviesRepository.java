package com.example.josepm.elitmovies.api.tmdb;

import android.util.Log;

import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetGenresCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetMovieCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetMoviesCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetTrailersCallback;
import com.example.josepm.elitmovies.api.tmdb.interfaces.TMDbApi;
import com.example.josepm.elitmovies.api.tmdb.models.GenresResponse;
import com.example.josepm.elitmovies.api.tmdb.models.Movie;
import com.example.josepm.elitmovies.api.tmdb.models.MoviesResponse;
import com.example.josepm.elitmovies.api.tmdb.models.TrailerResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MoviesRepository  {

    // region Variable Declaration
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String LANGUAGE = Locale.getDefault().getLanguage();
    private static final String TMDB_API_KEY = "1b5adf76a72a13bad99b8fc0c68cb085";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static MoviesRepository repository;

    private TMDbApi api;
    // endregion

    // Constructor
    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    // region getInstance (It creates the TMDBApi class with retrofit instance)
    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }
    // endregion

    // region getMovies (Calls the api to get all the movies)
    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MoviesResponse> call = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularMovies(TMDB_API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }
    // endregion

    // region getMovies (Calls the api to get the info of a movie)
    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, TMDB_API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    // endregion

    // region getGenres (Calls the api to get all the genres)
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

    // region getTrailers (Calls api to get trailers for specific movie)
    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, TMDB_API_KEY, LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    // endregion


}
