package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.Comment;
import com.example.josepm.elitmovies.api.tmdb.models.CommentsResponse;

import java.util.List;

import retrofit2.Call;

public interface OnGetCommentsCallback {

    void onSuccess(int page, List<Comment> comments);

    void onError(Call<CommentsResponse> call, Throwable t);

}
