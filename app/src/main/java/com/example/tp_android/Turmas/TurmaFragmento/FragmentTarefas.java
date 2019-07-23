package com.example.tp_android.Turmas.TurmaFragmento;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Alunos.AlunoAdapter;
import com.example.tp_android.Email.SendMail;
import com.example.tp_android.Models.Aluno;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Tarefa;
import com.example.tp_android.Turmas.TarefaAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentTarefas extends Fragment implements TarefaAdapter.OnItemClickListener{

    View view;

    //Tarefas
    private RecyclerView RvTarefas;
    private String idturma;
    private String nodi;
    private String key;
    private List<Tarefa> listTarefa;
    private TarefaAdapter tarefaAdapter;

    //Para Editar uma tarefa
    private Dialog popupTarefa;
    private TextView tarefadata, tarefatitulo, tarefadescricao;
    private Button btnSalvar;
    private DatePickerDialog.OnDateSetListener mDataSetListenerAlterar;
    private TimePickerDialog.OnTimeSetListener mTimeSetListenerAlterar;
    private Button popup_editartarefa;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference tarefaref;
    private ValueEventListener mDBListener;

    public FragmentTarefas() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tarefas_fragment, container, false);

        //inicializar a firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        idturma = getActivity().getIntent().getExtras().getString("idTurma");
        nodi = getActivity().getIntent().getExtras().getString("disciplinaTurma");
        tarefaref = firebaseDatabase.getReference("Tarefa").child(idturma);

        RvTarefas = view.findViewById(R.id.tarefas_fragrv);
        iniRvTarefa();

        return view;
    }

    private void iniRvTarefa() {
        listTarefa = new ArrayList<>();
        tarefaAdapter = new TarefaAdapter(getActivity(), listTarefa);
        RvTarefas.setAdapter(tarefaAdapter);
        RvTarefas.setLayoutManager(new LinearLayoutManager(getContext()));
        tarefaAdapter.setOnItemClickListener(this);



        tarefaref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTarefa.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    Tarefa tarefa= snap.getValue(Tarefa.class);
                    key = snap.getKey();
                    listTarefa.add(tarefa);
                }
                tarefaAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Para editar pressionar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSeen(final int position) {
        popupTarefa = new Dialog(getContext());
        popupTarefa.setContentView(R.layout.popupeditar_tarefa);
        popupTarefa.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupTarefa.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popupTarefa.getWindow().getAttributes().gravity = Gravity.CENTER;

        //Declarar todos os campos
        tarefadata = popupTarefa.findViewById(R.id.Editar_TarefaDataFinal);
        tarefatitulo = popupTarefa.findViewById(R.id.Editar_TarefaTitulo);
        tarefadescricao = popupTarefa.findViewById(R.id.Editar_TarefaDescricao);
        btnSalvar = popupTarefa.findViewById(R.id.editar_tarefaButton);

        //Chamar todos os dados
        tarefadata.setText(listTarefa.get(position).getDatafinaltarefa());
        tarefatitulo.setText(listTarefa.get(position).getTitulotarefa());
        tarefadescricao.setText(listTarefa.get(position).getDescricaotarefa());

        //String para apanhar o id
        final String calltask = listTarefa.get(position).getIdtarefa();

        //Alterar data
        tarefadata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, mDataSetListenerAlterar, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListenerAlterar= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d("Data", "a data é: "+ year + "/"+month+"/"+dayOfMonth);
                String datafinal = dayOfMonth + "/" + month + "/" + year;
                tarefadata.setText(datafinal);
            }
        };

        //Gravar novos dados
        popupTarefa.show();
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tarefa task = new Tarefa();
                task.setIdtarefa(listTarefa.get(position).getIdtarefa());
                task.setUserid(listTarefa.get(position).getUserid());
                task.setDatafinaltarefa(tarefadata.getText().toString());
                task.setDescricaotarefa(tarefadescricao.getText().toString());
                task.setTitulotarefa(tarefatitulo.getText().toString());
                tarefaref.child(calltask).setValue(task);
                popupTarefa.dismiss();
                enviaralteracao();
            }
        });

    }

    private void enviaralteracao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Pretende notificar todos os alunos desta alterações na aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviarmail();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Tarefa Alterada", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Notificar aluno");
        d.show();
    }

    private void enviarmail() {
        DatabaseReference alunoRef = firebaseDatabase.getReference("Aluno").child(idturma);
        alunoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Aluno aluno = snapshot.getValue(Aluno.class);
                    String antes = aluno.getEmail();
                    String antesreplace = antes.replace(".",",");
                    String emailaluno = FirebaseDatabase.getInstance().getReference("Aluno").child(idturma).child(antesreplace).getKey();
                    String transforma = emailaluno.replace(",",".");
                    Log.d("EMAIL", transforma);

                    String email = transforma;
                    String subject = "Alteração numa Tarefa";
                    String message = "Atenção, verifica a tua aplicação de gestão escolar porque uma tarefa da aula de "+nodi+ " foi alterada. Está atento às datas!";
                    //Creating SendMail object
                    SendMail sm = new SendMail(getContext(), email, subject, message);
                    //Executing sendmail to send email
                    sm.execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Tem a certeza que pretende eliminar esta tarefa?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idTarefa = listTarefa.get(position).getIdtarefa();
                tarefaref.child(idTarefa).removeValue();
                Toast.makeText(getContext(), "Eliminada", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = builder.create();
        d.setTitle("Eliminar Tarefa");
        d.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseReference aulaarr = firebaseDatabase.getReference("Turma").child(idturma);
        if(mDBListener != null){
            aulaarr.removeEventListener(mDBListener);
            RvTarefas.setLayoutManager(null);
        }

    }
}
