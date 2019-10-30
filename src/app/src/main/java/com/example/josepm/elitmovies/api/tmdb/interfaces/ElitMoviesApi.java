package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.CommentsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ElitMoviesApi {

    @GET("api/Comentarios")
    Call<CommentsResponse> getComments(
            @Query("IdPelicula") int idPelicula,
            @Query("page") int page
    );

}
