package com.example.tp_android.Perfil.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Aluno> mUser;

    //call users(Alunos)
    private String idTurma;
    private String currentUser;
    private DatabaseReference alunosref;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_users, container, false);

        //inicializar a firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.ContactarRV_utilizadores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser = new ArrayList<>();
        readUser();

        return view;
    }

    private void readUser() {

        firebaseUser = firebaseAuth.getCurrentUser();
        currentUser = firebaseUser.getUid();
        DatabaseReference refTurma = firebaseDatabase.getReference("Turma").child(currentUser);
        refTurma.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Turma turma = ds.getValue(Turma.class);
                    idTurma = turma.getIdTurma();
                    Log.d("Turmaaaaaa", idTurma);
                    alunosref = firebaseDatabase.getReference("Aluno").child(idTurma);
                    alunosref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //mUser.clear();
                            for(DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                                Aluno aluno = dSnapshot.getValue(Aluno.class);
                                Log.d("Emails", aluno.getEmail());
                                mUser.add(aluno);
                            }
                            userAdapter = new UserAdapter(getContext(), mUser);
                            recyclerView.setAdapter(userAdapter);
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
