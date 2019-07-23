package com.example.tp_android.Alunos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.tp_android.Alunos.Contactar.ContactarProfessor;
import com.example.tp_android.MainActivity;
import com.example.tp_android.Perfil.MessageActivity;
import com.example.tp_android.R;
import com.google.firebase.auth.FirebaseAuth;

public class MenuAluno extends AppCompatActivity {

    private CardView cardlogout;
    private CardView cardTarefas;
    private CardView cardSumários;
    private CardView cardMensagens;
    private CardView cardAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_aluno);


        //Terminar Sessão alunos
        cardlogout = findViewById(R.id.Aluno_terminarsessao);
        cardlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logoutActivity = new Intent(MenuAluno.this, MainActivity.class);
                startActivity(logoutActivity);
                finish(); //Não conseguir voltar a entrar
            }
        });

        //Ver Tarefas
        cardTarefas = findViewById(R.id.Aluno_vertarefas);
        cardTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inttarefa = new Intent(MenuAluno.this, TarefaAluno.class);
                startActivity(inttarefa);
            }
        });


        //Ver Sumários
        cardSumários = findViewById(R.id.Aluno_Sumarios);
        cardSumários.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suma = new Intent(MenuAluno.this, SumarioAluno.class);
                startActivity(suma);
            }
        });

        //Ver Aulas
        cardAulas = findViewById(R.id.Aluno_Acessos);
        cardAulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intaula = new Intent(MenuAluno.this, AulaAluno.class);
                startActivity(intaula);
            }
        });

        //Ver Mensagens
        cardMensagens = findViewById(R.id.Aluno_vermensagens);
        cardMensagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intsms = new Intent(MenuAluno.this, ContactarProfessor.class);
                startActivity(intsms);
            }
        });
    }
}
