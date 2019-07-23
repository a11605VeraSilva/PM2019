package com.example.tp_android.Turmas;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tp_android.Models.Note;
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

public class BlocoNotas extends AppCompatActivity {


    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;
    private List<Note> noteList;
    private NoteAdapter noteadapter;
    private GridLayoutManager gridLayoutManager;
    private String currentuser;
    private FirebaseUser firebaseUser;



    private DatabaseReference fNotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloco_notas);

        fAuth = FirebaseAuth.getInstance();
        mNotesList = (RecyclerView) findViewById(R.id.notas_lista);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        currentuser = firebaseUser.getUid();

        if(fAuth.getCurrentUser() != null){
            fNotesDatabase = FirebaseDatabase.getInstance().getReference("Nota").child(currentuser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.nota_menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.new_nota_add:
                Intent newintent = new Intent(BlocoNotas.this, NewNote.class);
                startActivity(newintent);
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteList=new ArrayList<>();
        fNotesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noteList.clear();
                for (DataSnapshot journalDS : dataSnapshot.getChildren()) {
                    Note note = journalDS.getValue(Note.class);
                    noteList.add(note);
                }
                int numberOfColumns = 2;
                mNotesList.setLayoutManager(new GridLayoutManager(BlocoNotas.this, numberOfColumns));
                noteadapter = new NoteAdapter(BlocoNotas.this, noteList);
                mNotesList.setAdapter(noteadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
