package com.example.tp_android.Models;

public class Note {

    private String titulo, tempo, descricao, userid;

    public Note() {
    }

    public Note(String titulo, String tempo, String descricao, String userid) {
        this.titulo = titulo;
        this.tempo = tempo;
        this.descricao = descricao;
        this.userid = userid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
