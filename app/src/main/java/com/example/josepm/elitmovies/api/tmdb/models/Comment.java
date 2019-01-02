package com.example.josepm.elitmovies.api.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    @Expose
    private int commentId;

    @SerializedName("titulo")
    @Expose
    private String titulo;

    @SerializedName("contenido")
    @Expose
    private String contenido;

    @SerializedName("id_SubComentarioDe")
    @Expose
    private int idSubcomentario;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("Pelicula_Id")
    @Expose
    private int peliculaId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdSubcomentario() {
        return idSubcomentario;
    }

    public void setIdSubcomentario(int idSubcomentario) {
        this.idSubcomentario = idSubcomentario;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(int peliculaId) {
        this.peliculaId = peliculaId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
