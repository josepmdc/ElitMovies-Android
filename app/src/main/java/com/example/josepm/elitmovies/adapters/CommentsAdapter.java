package com.example.josepm.elitmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.josepm.elitmovies.R;
import com.example.josepm.elitmovies.api.tmdb.models.Comment;

import java.util.List;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentsAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expandable_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder commentViewHolder, int i) {
        Comment comment = commentList.get(i);
        commentViewHolder.title.setText(comment.getTitulo());
        commentViewHolder.body.setText(comment.getContenido());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void clearComments() {
        commentList.clear();
        notifyDataSetChanged();
    }

    // region MovieViewHolder class
    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView body;
        TextView user;

        public CommentViewHolder (View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.comment_title);
            body = itemView.findViewById(R.id.comment_body);
            user = itemView.findViewById(R.id.comment_username);
        }

    }
}
