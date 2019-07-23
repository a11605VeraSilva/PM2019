package com.example.tp_android.Alunos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Sumario;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Tarefa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SumarioAluno extends AppCompatActivity {

    private RecyclerView recyclerViewSumario;
    private SumarioAluno_Adapter sumarioAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference Turmaref;
    private DatabaseReference Alunoref;
    private List<Sumario> sumarioList;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private String KEY;
    private String idTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumario_aluno);
        recyclerViewSumario = findViewById(R.id.rvaluno_versumarios);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Turmaref = firebaseDatabase.getReference("Aluno");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = mAuth.getCurrentUser();
        CurrentUser = fuser.getEmail();

        Turmaref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    KEY = data.getKey();
                    final String transformacurrent = CurrentUser.replace(".", ",");
                    Alunoref = firebaseDatabase.getReference("Aluno").child(KEY);
                    Alunoref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Aluno student = ds.getValue(Aluno.class);
                                if(CurrentUser.equals(student.getEmail())){
                                    idTurma = student.getTurmaid();

                                    sumarioList = new ArrayList<>();
                                    sumarioAdapter = new SumarioAluno_Adapter(SumarioAluno.this, sumarioList);
                                    recyclerViewSumario.setAdapter(sumarioAdapter);
                                    recyclerViewSumario.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                    DatabaseReference datasumario = FirebaseDatabase.getInstance().getReference("Sumario").child(idTurma);
                                    datasumario.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            sumarioList.clear();
                                            for (DataSnapshot snap: dataSnapshot.getChildren()){
                                                Sumario sumario= snap.getValue(Sumario.class);
                                                sumarioList.add(sumario);
                                            }
                                            sumarioAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
