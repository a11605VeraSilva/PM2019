package com.example.tp_android.Turmas;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tp_android.Models.Note;
import com.example.tp_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewNote extends AppCompatActivity {

    private Button btnCreate;
    private EditText etTitle, etContent;
    private Toolbar mToolbar;

    private FirebaseAuth fAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference fNotesDatabase;
    private String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);



        btnCreate = (Button) findViewById(R.id.nota_buttonadd);
        etTitle = (EditText) findViewById(R.id.nota_novanota);
        etContent = (EditText) findViewById(R.id.nota_textonota);
        mToolbar = (Toolbar) findViewById(R.id.nota_toolbar);

        setSupportActionBar(mToolbar);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        currentuser = firebaseUser.getUid();

        fNotesDatabase = FirebaseDatabase.getInstance().getReference("Nota").child(currentuser).push();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                    createNote(title, content);
                    etTitle.setText("");
                    etContent.setText("");
                }else{
                    showMessage("Erro ao inserir");
                }
            }
        });

    }

    public void createNote(String title, String content){

        if (currentuser != null){

            Note note = new Note();
            note.setUserid(currentuser);
            note.setTitulo(etTitle.getText().toString());
            note.setDescricao(etContent.getText().toString());
            note.setTempo(String.valueOf(DateFormat.DAY_OF_YEAR_FIELD));
            fNotesDatabase.setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showMessage("Adicionado com sucesso");
                }
            });
        }else{
            showMessage("Erro");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(NewNote.this, message, Toast.LENGTH_LONG).show();
    }
}
