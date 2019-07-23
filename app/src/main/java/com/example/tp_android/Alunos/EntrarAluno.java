package com.example.tp_android.Alunos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tp_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EntrarAluno extends AppCompatActivity {

    //Declaração variaveis aluno
    private EditText alunoEmail, alunocodigo;
    private Button alunoButton;
    private ProgressBar alunoProgress;
    private FirebaseAuth mAuth;
    private Intent MenuAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar_aluno);

        mAuth =FirebaseAuth.getInstance();

        alunoEmail = findViewById(R.id.aluno_username);
        alunocodigo = findViewById(R.id.aluno_codigo);
        alunoButton = findViewById(R.id.buttonEntrarAluno);
        alunoProgress = findViewById(R.id.login_progressbarAluno);

        MenuAluno = new Intent(this, com.example.tp_android.Alunos.MenuAluno.class);
        alunoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = alunoEmail.getText().toString();
                final String codigo = alunocodigo.getText().toString();

                if(email.isEmpty() || codigo.isEmpty()){
                    showMessage("Verifique as credenciais");
                }else{
                    signIn(email, codigo);
                }
            }
        });
    }

    private void signIn(String email, String codigo) {
        mAuth.signInWithEmailAndPassword(email, codigo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    alunoProgress.setVisibility(View.INVISIBLE);
                    alunoButton.setVisibility(View.VISIBLE);
                    updateUI();
                }else{
                    showMessage(task.getException().getMessage());
                    alunoButton.setVisibility(View.VISIBLE);
                    alunoProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        startActivity(MenuAluno);
        finish();
    }

    private void showMessage(String verifique_as_credencias) {

        Toast.makeText(getApplicationContext(), verifique_as_credencias, Toast.LENGTH_LONG).show();
    }

}
