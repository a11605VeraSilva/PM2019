package com.example.tp_android.Alunos.Contactar;

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

import com.example.tp_android.Models.Message;
import com.example.tp_android.Perfil.Fragment.MessageAdapter;
import com.example.tp_android.Perfil.MessageActivity;
import com.example.tp_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class professorfrag extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private List<Message> mMessage;
    private FirebaseUser firebaseUser;
    private String CurrentUser;
    private ProfAdapter profadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_professorfrag, container, false);

        //inicializar a firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.ContactarRV_professores);
        readUser();

        return view;
    }

    private void readUser() {
        firebaseUser = firebaseAuth.getCurrentUser();
        CurrentUser = firebaseUser.getEmail();

        final String Translate = CurrentUser.replace(".", ",");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Mensagem").child(Translate);

        final String BuscarMensagem = userRef.getKey().replace(",", ".");

        mMessage = new ArrayList<>();
        profadapter = new ProfAdapter(getContext(), mMessage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessage.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message sms = ds.getValue(Message.class);
                    mMessage.add(sms);
                    Log.d("ENTRAAAAA", "CurrentUser: " + CurrentUser + " Translate " + BuscarMensagem);
                }
                recyclerView.setAdapter(profadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
