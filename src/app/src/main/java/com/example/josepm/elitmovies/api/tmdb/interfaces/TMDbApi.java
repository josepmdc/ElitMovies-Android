package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.GenresResponse;
import com.example.josepm.elitmovies.api.tmdb.models.Movie;
import com.example.josepm.elitmovies.api.tmdb.models.MoviesResponse;
import com.example.josepm.elitmovies.api.tmdb.models.MovieSearchResponse;
import com.example.josepm.elitmovies.api.tmdb.models.TrailerResponse;
import com.example.josepm.elitmovies.api.tmdb.models.TvShow;
import com.example.josepm.elitmovies.api.tmdb.models.TvShowResponse;
import com.example.josepm.elitmovies.api.tmdb.models.TvShowSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {

    // region Movies
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    // endregion

    // region TV Shows

    @GET("tv/popular")
    Call<TvShowResponse> getPopularShows(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/top_rated")
    Call<TvShowResponse> getTopRatedShows(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/airing_today")
    Call<TvShowResponse> getShowsAiringToday(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/on_the_air")
    Call<TvShowResponse> getShowsOnAir(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/{tv_show_id}")
    Call<TvShow> getTvShow(
            @Path("tv_show_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/{tv_show_id}/videos")
    Call<TrailerResponse> getTrailersTvShow(
            @Path("tv_show_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    // endregion

    // region Genres

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // endregion

    // region Search

    @GET("search/movie")
    Call<MovieSearchResponse> getMovieSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );

    @GET("search/tv")
    Call<TvShowSearchResponse> getTvShowSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );

    // endregion

}
