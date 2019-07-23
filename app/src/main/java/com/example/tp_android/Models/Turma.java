package com.example.tp_android.Models;

import com.google.firebase.database.ServerValue;

public class Turma {

    private String idTurma, disciplinaTurma, acronimoTurma, cursoTurma, anoTurma, semestreTurma, anocorrente;

    public Turma() {
    }

    public Turma(String idTurma, String disciplinaTurma, String acronimoTurma, String cursoTurma, String anoTurma, String semestreTurma, String anocorrente) {
        this.idTurma = idTurma;
        this.disciplinaTurma = disciplinaTurma;
        this.acronimoTurma = acronimoTurma;
        this.cursoTurma = cursoTurma;
        this.anoTurma = anoTurma;
        this.semestreTurma = semestreTurma;
        this.anocorrente = anocorrente;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    public String getDisciplinaTurma() {
        return disciplinaTurma;
    }

    public void setDisciplinaTurma(String disciplinaTurma) {
        this.disciplinaTurma = disciplinaTurma;
    }

    public String getAcronimoTurma() {
        return acronimoTurma;
    }

    public void setAcronimoTurma(String acronimoTurma) {
        this.acronimoTurma = acronimoTurma;
    }

    public String getCursoTurma() {
        return cursoTurma;
    }

    public void setCursoTurma(String cursoTurma) {
        this.cursoTurma = cursoTurma;
    }

    public String getAnoTurma() {
        return anoTurma;
    }

    public void setAnoTurma(String anoTurma) {
        this.anoTurma = anoTurma;
    }

    public String getSemestreTurma() {
        return semestreTurma;
    }

    public void setSemestreTurma(String semestreTurma) {
        this.semestreTurma = semestreTurma;
    }

    public String getAnocorrente() {
        return anocorrente;
    }

    public void setAnocorrente(String anocorrente) {
        this.anocorrente = anocorrente;
    }
}
