package com.example.josepm.elitmovies.api.tmdb;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.josepm.elitmovies.api.tmdb.interfaces.ElitMoviesApi;
import com.example.josepm.elitmovies.api.tmdb.interfaces.OnGetCommentsCallback;
import com.example.josepm.elitmovies.api.tmdb.models.CommentsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsRepository {

    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static CommentsRepository repository;

    private ElitMoviesApi api;

    public CommentsRepository (ElitMoviesApi api) {
        this.api = api;
    }

    // region getInstance (It creates the TMDBApi class with retrofit instance)
    public static CommentsRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new CommentsRepository(retrofit.create(ElitMoviesApi.class));
        }

        return repository;
    }
    // endregion

    // region getComments (Calls the api to get the comments of a movie/TV show)
    public void getComments(int movieId, final OnGetCommentsCallback callback) {
        api.getComments(movieId, 1)
                .enqueue(new Callback<CommentsResponse>() {
                    @Override
                    public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {
                        if (response.isSuccessful()) {
                            CommentsResponse commentsResponse = response.body();
                            if (commentsResponse != null) {
                                callback.onSuccess(commentsResponse.getCurrentPage(), commentsResponse.getComments());
                            } else {
                                Log.e("ERROR getComments", "No hay comentarios");
                            }
                        } else {
                            Log.e("ERROR getComments", "Peticion fallida");
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentsResponse> call, Throwable t) {
                        callback.onError(call, t);
                    }
                });
    }
    // endregion

}
