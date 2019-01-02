package com.example.josepm.elitmovies.api.tmdb.interfaces;

import com.example.josepm.elitmovies.api.tmdb.models.Comment;

import java.util.List;

public interface OnGetCommentsCallback {

    void onSuccess(int page, List<Comment> comments);

    void onError();

}
