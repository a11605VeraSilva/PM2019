package com.example.tp_android.Turmas;

import java.util.Date;

public class Aula {

    private String idaula, horaaula, duracaoaula, salaaula, userid, tipoAula;
    private Object timeStamp;

    public Aula() {
    }

    public Aula(String idaula, String horaaula, String duracaoaula, String salaaula, String userid, String tipoAula) {
        this.idaula = idaula;
        this.horaaula = horaaula;
        this.duracaoaula = duracaoaula;
        this.salaaula = salaaula;
        this.userid = userid;
        this.tipoAula = tipoAula;
    }

    public String getIdaula() {
        return idaula;
    }

    public void setIdaula(String idaula) {
        this.idaula = idaula;
    }

    public String getHoraaula() {
        return horaaula;
    }

    public void setHoraaula(String horaaula) {
        this.horaaula = horaaula;
    }

    public String getDuracaoaula() {
        return duracaoaula;
    }

    public void setDuracaoaula(String duracaoaula) {
        this.duracaoaula = duracaoaula;
    }

    public String getSalaaula() {
        return salaaula;
    }

    public void setSalaaula(String salaaula) {
        this.salaaula = salaaula;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
