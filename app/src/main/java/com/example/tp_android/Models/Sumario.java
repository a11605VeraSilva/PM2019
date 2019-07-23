package com.example.tp_android.Models;

public class Sumario {

    String idsumario, descricaosumario, datasumario, idutilizador;

    public Sumario() {
    }

    public Sumario(String idsumario, String descricaosumario, String datasumario, String idutilizador) {
        this.idsumario = idsumario;
        this.descricaosumario = descricaosumario;
        this.datasumario = datasumario;
        this.idutilizador = idutilizador;
    }

    public String getIdsumario() {
        return idsumario;
    }

    public void setIdsumario(String idsumario) {
        this.idsumario = idsumario;
    }

    public String getDescricaosumario() {
        return descricaosumario;
    }

    public void setDescricaosumario(String descricaosumario) {
        this.descricaosumario = descricaosumario;
    }

    public String getDatasumario() {
        return datasumario;
    }

    public void setDatasumario(String datasumario) {
        this.datasumario = datasumario;
    }

    public String getIdutilizador() {
        return idutilizador;
    }

    public void setIdutilizador(String idutilizador) {
        this.idutilizador = idutilizador;
    }
}
