package com.example.tp_android.Turmas;

import android.accessibilityservice.GestureDescription;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.time.Year;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MenuTurmas extends AppCompatActivity {

    CardView cardAddTurma, cardVerTurmas, cardNotas;
    Dialog popAddTurma;
    TextView popup_NomeTurma, popup_acronimoTurma, popup_CursoTurma, popup_AnoTurma, popup_SemestreTurma;
    Button popup_adicionarturma;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_turmas);

        cardAddTurma = findViewById(R.id.Menu_addturma);
        cardVerTurmas = findViewById(R.id.Menu_verturma);
        cardNotas = findViewById(R.id.Menu_verNotas);

        iniPopup();
        inicializarFirebase();

        cardAddTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              popAddTurma.show();

            }
        });

        cardVerTurmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuTurmas.this, VerTurmas.class);
                startActivity(intent);
            }
        });

        cardNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intNotas = new Intent(MenuTurmas.this, BlocoNotas.class);
                startActivity(intNotas);
            }
        });
    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void iniPopup() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = firebaseUser.getUid();


        popAddTurma = new Dialog(this);
        popAddTurma.setContentView(R.layout.popup_addturma);
        popAddTurma.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddTurma.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddTurma.getWindow().getAttributes().gravity = Gravity.CENTER;

        popup_NomeTurma = popAddTurma.findViewById(R.id.AddTurma_Nome);
        popup_acronimoTurma = popAddTurma.findViewById(R.id.AddTurma_Acronimo);
        popup_CursoTurma = popAddTurma.findViewById(R.id.AddTurma_Curso);
        popup_AnoTurma = popAddTurma.findViewById(R.id.AddTurma_Ano);
        popup_SemestreTurma = popAddTurma.findViewById(R.id.AddTurma_Semestre);
        popup_adicionarturma = popAddTurma.findViewById(R.id.AddTurma_Button);


        popup_adicionarturma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adicionar uma turma
                Turma turma = new Turma();
                turma.setIdTurma(UUID.randomUUID().toString());
                String verTeste = turma.getIdTurma();
                String anocorrente = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                turma.setAnocorrente(anocorrente);
                turma.setDisciplinaTurma(popup_NomeTurma.getText().toString());
                turma.setAcronimoTurma(popup_acronimoTurma.getText().toString());
                turma.setAnoTurma(popup_AnoTurma.getText().toString());
                turma.setCursoTurma(popup_CursoTurma.getText().toString());
                turma.setSemestreTurma(popup_SemestreTurma.getText().toString());
                DatabaseReference turmareference = firebaseDatabase.getReference("Turma").child(currentUser).child(verTeste);
                Log.d("ERRO", turmareference.toString());
                turmareference.setValue(turma).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Adicionado com sucesso");
                        popup_SemestreTurma.setText("");
                        popup_AnoTurma.setText("");
                        popup_CursoTurma.setText("");
                        popup_acronimoTurma.setText("");
                        popup_NomeTurma.setText("");
                        Intent intTurmas = new Intent(popAddTurma.getContext(), VerTurmas.class);
                        startActivity(intTurmas);
                    }
                });
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(MenuTurmas.this, message, Toast.LENGTH_LONG).show();
    }
}
