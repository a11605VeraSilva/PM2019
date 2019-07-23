package com.example.tp_android.Models;

public class Administrativo {

    private String adminid, email, idturma, idprof, tipo;

    public Administrativo() {
    }

    public Administrativo(String adminid, String email, String idturma, String idprof, String tipo) {
        this.adminid = adminid;
        this.email = email;
        this.idturma = idturma;
        this.idprof = idprof;
        this.tipo = tipo;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdturma() {
        return idturma;
    }

    public void setIdturma(String idturma) {
        this.idturma = idturma;
    }

    public String getIdprof() {
        return idprof;
    }

    public void setIdprof(String idprof) {
        this.idprof = idprof;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
