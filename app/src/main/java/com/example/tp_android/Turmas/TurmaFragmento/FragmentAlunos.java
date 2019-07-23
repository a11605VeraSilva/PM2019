package com.example.tp_android.Turmas.TurmaFragmento;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tp_android.Alunos.AlunoAdapter;
import com.example.tp_android.Email.SendMail;
import com.example.tp_android.Models.Aluno;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Aula;
import com.example.tp_android.Turmas.AulaAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentAlunos extends Fragment implements AlunoAdapter.OnItemClickListener{

    View view;

    //ALUNOS
    private RecyclerView RvAlunos;
    private String idturma;
    private String key;
    private List<Aluno> listAluno;
    private AlunoAdapter alunoAdapter;
    private EditText escreveremail;
    private Button btnenviaremail;

    //EMAIL
    private Dialog popEmail;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference alunosref;
    private ValueEventListener mDBListener;

    public FragmentAlunos() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alunos_fragment, container, false);

        //inicializar a firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        idturma = getActivity().getIntent().getExtras().getString("idTurma");
        alunosref = firebaseDatabase.getReference("Aluno").child(idturma);

        //--------------------------------------Inicializar Alunos
        RvAlunos = view.findViewById(R.id.alunosfrag_rv);
        iniRvAluno();
        return view;
    }

    private void iniRvAluno() {
        listAluno = new ArrayList<>();
        alunoAdapter = new AlunoAdapter(getActivity(), listAluno);
        RvAlunos.setAdapter(alunoAdapter);
        RvAlunos.setLayoutManager(new LinearLayoutManager(getContext()));
        alunoAdapter.setOnItemClickListener(this);


        alunosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAluno.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    Aluno aluno = snap.getValue(Aluno.class);
                    key = snap.getKey();
                    listAluno.add(aluno);
                }
                alunoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Para contactar deve pressionar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEmail(final int positon) {
        popEmail = new Dialog(getContext());
        popEmail.setContentView(R.layout.popup_escreveremail);
        popEmail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popEmail.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popEmail.getWindow().getAttributes().gravity = Gravity.CENTER;

        escreveremail = popEmail.findViewById(R.id.Email_textoemail);
        btnenviaremail = popEmail.findViewById(R.id.EmailEnviar_Button);

        popEmail.show();
        btnenviaremail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailaluno = listAluno.get(positon).getEmail();
                String TransformaEmailAlluno = emailaluno.replace(",", ".");
                String email = TransformaEmailAlluno;
                String subject = "Contacto através da aplicação Gestão Escolar";
                String message = escreveremail.getText().toString().trim();
                //Creating SendMail object
                SendMail sm = new SendMail(getContext(), email, subject, message);
                //Executing sendmail to send email
                sm.execute();
                popEmail.dismiss();
            }
        });
    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Tem a certeza que pretende eliminar este aluno?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String deleteuser = listAluno.get(position).getEmail();
                alunosref.child(deleteuser.replace(".", ",")).removeValue();
                Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = builder.create();
        d.setTitle("Eliminar Utilizador");
        d.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDBListener != null){
            alunosref.removeEventListener(mDBListener);
            RvAlunos.setLayoutManager(null);
        }

    }
}
