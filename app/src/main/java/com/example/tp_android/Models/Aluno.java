package com.example.tp_android.Models;

public class Aluno {

    private String alunoid, email, turmaid, professorid, tipo;
    private Integer codigo;

    public Aluno() {
    }

    public Aluno(String alunoid, String email, String tipo) {
        this.alunoid = alunoid;
        this.email = email;
        this.tipo = tipo;
    }

    public Aluno(String alunoid, String email, String turmaid, String professorid, Integer codigo, String tipo) {
        this.alunoid = alunoid; //
        this.email = email; //
        this.turmaid = turmaid;
        this.professorid = professorid;
        this.codigo = codigo; //
        this.tipo = tipo;//
    }

    public String getAlunoid() {
        return alunoid;
    }

    public void setAlunoid(String alunoid) {
        this.alunoid = alunoid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTurmaid() {
        return turmaid;
    }

    public void setTurmaid(String turmaid) {
        this.turmaid = turmaid;
    }

    public String getProfessorid() {
        return professorid;
    }

    public void setProfessorid(String professorid) {
        this.professorid = professorid;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
