package com.example.tp_android.Mural;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Turma;
import com.example.tp_android.Perfil.Card_Analise;
import com.example.tp_android.Perfil.Card_Contactar;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.AulaAdapter;
import com.example.tp_android.Turmas.Tarefa;
import com.example.tp_android.Turmas.TarefaAdapter;
import com.example.tp_android.Turmas.TurmasAdapter;
import com.example.tp_android.Turmas.VerTurmas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MuralTurmas extends AppCompatActivity {

   private DatabaseReference databaseReference;
   private FirebaseDatabase firebaseDatabase;
   private FirebaseAuth firebaseAuth;
   private String currentUser;
   private FirebaseUser firebaseUser;
   private DatabaseReference userref;

   private TextView textNome;
   private TextView textEmail;

   //Card para outras páginas
    private CardView cardcontactar;
    private CardView cardnotificacoes;
    private CardView cardanalise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mural_turmas);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = firebaseAuth.getCurrentUser().getUid();
        userref = firebaseDatabase.getReference().child("User").child(currentUser);

        textNome = (TextView) findViewById(R.id.perfil_username);
        textEmail = (TextView) findViewById(R.id.perfil_useremail);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {

                    String user_nome = dataSnapshot.child("username").getValue().toString();
                    String user_email = dataSnapshot.child("userEmail").getValue().toString();

                    textNome.setText(user_nome);
                    textEmail.setText(user_email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cardcontactar = (CardView) findViewById(R.id.card_contacta);
        cardcontactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intfrag = new Intent(MuralTurmas.this, Card_Contactar.class);
                startActivity(intfrag);
            }
        });

    }
}
