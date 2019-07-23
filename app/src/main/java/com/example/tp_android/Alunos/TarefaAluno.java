package com.example.tp_android.Alunos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Tarefa;
import com.example.tp_android.Turmas.TurmasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class TarefaAluno extends AppCompatActivity {

    private RecyclerView recyclerViewTarefa;
    private TarefaAluno_Adapter tfAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference Turmaref;
    private DatabaseReference Alunoref;
    private List<Tarefa> tarefaList;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private String KEY;
    private String idTurma;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_aluno);
        recyclerViewTarefa = findViewById(R.id.rvaluno_vertarefas);
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

                                    tarefaList = new ArrayList<>();
                                    tfAdapter = new TarefaAluno_Adapter(TarefaAluno.this, tarefaList);
                                    recyclerViewTarefa.setAdapter(tfAdapter);
                                    recyclerViewTarefa.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                    DatabaseReference datatarefa = FirebaseDatabase.getInstance().getReference("Tarefa").child(idTurma);
                                    datatarefa.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            tarefaList.clear();
                                            for (DataSnapshot snap: dataSnapshot.getChildren()){
                                                Tarefa tarefa= snap.getValue(Tarefa.class);
                                                Log.d("TAREFAAA", tarefa.getDescricaotarefa());
                                                tarefaList.add(tarefa);
                                            }
                                            tfAdapter.notifyDataSetChanged();
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
