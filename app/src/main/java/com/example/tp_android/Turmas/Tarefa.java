package com.example.tp_android.Turmas;

public class Tarefa {

    String idtarefa, titulotarefa, descricaotarefa, datafinaltarefa, userid;

    public Tarefa() {
    }

    public Tarefa(String idtarefa, String titulotarefa, String descricaotarefa, String datafinaltarefa, String userid) {
        this.idtarefa = idtarefa;
        this.titulotarefa = titulotarefa;
        this.descricaotarefa = descricaotarefa;
        this.datafinaltarefa = datafinaltarefa;
        this.userid = userid;
    }

    public String getIdtarefa() {
        return idtarefa;
    }

    public void setIdtarefa(String idtarefa) {
        this.idtarefa = idtarefa;
    }

    public String getTitulotarefa() {
        return titulotarefa;
    }

    public void setTitulotarefa(String titulotarefa) {
        this.titulotarefa = titulotarefa;
    }

    public String getDescricaotarefa() {
        return descricaotarefa;
    }

    public void setDescricaotarefa(String descricaotarefa) {
        this.descricaotarefa = descricaotarefa;
    }

    public String getDatafinaltarefa() {
        return datafinaltarefa;
    }

    public void setDatafinaltarefa(String datafinaltarefa) {
        this.datafinaltarefa = datafinaltarefa;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
