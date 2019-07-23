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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tp_android.Email.SendMail;
import com.example.tp_android.Models.Administrativo;
import com.example.tp_android.Models.Aluno;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Aula;
import com.example.tp_android.Turmas.AulaAdapter;
import com.example.tp_android.Turmas.TabLTurma;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.UUID;

public class FragmentAula extends Fragment implements AulaAdapter.OnItemClickListener{

    View view;
    //AULAS
    private RecyclerView RvAulas;
    private String idturma;
    private String key;
    private List<Aula> listAula;
    private AulaAdapter aulaAdapter;

    //Alterar data
    private Dialog popAlterar;
    private TextView datanova;
    private Button buttondata;
    private DatePickerDialog.OnDateSetListener mDataSetListener;

    //Editar todas as informações
    private Dialog popEditar;
    private TextView popupAula_Hora, popupAula_Duracao, popupAula_Sala;
    private TimePickerDialog.OnTimeSetListener mTimeSetListenerAlterar;
    private Button popup_editarAula;
    private CheckBox popaula_hecktext;
    private EditText popaula_checkdesc;
    private String nomedadi;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener mDBListener;
    private DatabaseReference aulasref;


    List<Aluno> listaluno;

    private String DataAnterior;
    private String DataNova;

    public FragmentAula() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aula_fragment, container, false);

        //Inicializar firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        idturma = getActivity().getIntent().getExtras().getString("idTurma");
        nomedadi = getActivity().getIntent().getExtras().getString("disciplinaTurma");
        aulasref = firebaseDatabase.getReference("Aula").child(idturma);


        //--------------------------------------Inicializar Aulas
        RvAulas = view.findViewById(R.id.aulafrag_rv);
        iniRvAula();
        return view;
    }


    private void iniRvAula() {
        listAula = new ArrayList<>();
        aulaAdapter = new AulaAdapter(getActivity(), listAula);
        RvAulas.setAdapter(aulaAdapter);
        RvAulas.setLayoutManager(new LinearLayoutManager(getContext()));
        aulaAdapter.setOnItemClickListener(this);

        mDBListener = aulasref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAula.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    Aula aula = snap.getValue(Aula.class);
                    key = snap.getKey();
                    listAula.add(aula);
                }
                aulaAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Para alterar ou eliminar click longo", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEditClick(final int position) {
        popAlterar = new Dialog(getContext());
        popAlterar.setContentView(R.layout.popup_alterardata);
        popAlterar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAlterar.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAlterar.getWindow().getAttributes().gravity = Gravity.CENTER;
        datanova = popAlterar.findViewById(R.id.Alterardata_data);
        datanova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, mDataSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d("Data", "a data é: "+ year + "/"+month+"/"+dayOfMonth);
                String datafinal = dayOfMonth + "/" + month + "/" + year;
                datanova.setText(datafinal);
            }
        };

        buttondata = popAlterar.findViewById(R.id.Alterardata_button);
        //Log.d("TESTAR KEY: ", listAula.get(position).getIdaula());
        final String idAula = listAula.get(position).getIdaula();
        datanova.setText(listAula.get(position).getTimeStamp().toString());
        DataAnterior = listAula.get(position).getTimeStamp().toString();
        popAlterar.show();
        buttondata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aula aulasss = new Aula();
                aulasss.setTimeStamp(datanova.getText().toString().trim());
                aulasss.setUserid(listAula.get(position).getUserid());
                aulasss.setSalaaula(listAula.get(position).getSalaaula());
                aulasss.setDuracaoaula(listAula.get(position).getDuracaoaula());
                aulasss.setHoraaula(listAula.get(position).getHoraaula());
                aulasss.setIdaula(listAula.get(position).getIdaula());
                aulasss.setTipoAula(listAula.get(position).getTipoAula());
                aulasref.child(idAula).setValue(aulasss);
                showMessage("Alterada");
                DataNova = datanova.getText().toString().trim();
                sendEmail();
            }
        });
    }

    //Para enviar email aos alunos
    private void sendEmail() {
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
                    String subject = "Alteração Data de Aula";
                    String message = "A aula de " +nomedadi+ " do dia "+ DataAnterior + " foi alterada para o dia: "+ DataNova;
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

        DatabaseReference proRef = firebaseDatabase.getReference("Administrativo").child(idturma);
        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Administrativo admin = snapshot.getValue(Administrativo.class);
                    String antes = admin.getEmail();
                    String antesreplace = antes.replace(".",",");
                    String emailadmin = FirebaseDatabase.getInstance().getReference("Administrativo").child(idturma).child(antesreplace).getKey();
                    String transformaadmin = emailadmin.replace(",",".");
                    Log.d("EMAIL", transformaadmin);


                    String email = transformaadmin;
                    String subject = "Alteração Data de Aula";
                    String message = "A aula de "+ nomedadi + " do dia "+ DataAnterior + " foi alterada para o dia: "+ DataNova;
                    //Creating SendMail object
                    SendMail sm = new SendMail(getContext(), email, subject, message);
                    //Executing sendmail to send email
                    sm.execute();
                    popAlterar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onSeeClick(final int position) {
        popEditar = new Dialog(getContext());
        popEditar.setContentView(R.layout.popupeditar_aula);
        popEditar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popEditar.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popEditar.getWindow().getAttributes().gravity = Gravity.CENTER;

        //DECLARAR TUDO
        popupAula_Hora = popEditar.findViewById(R.id.Editar_Hora);
        popupAula_Duracao = popEditar.findViewById(R.id.Editar_Duracao);
        popupAula_Sala = popEditar.findViewById(R.id.Editor_Sala);
        popaula_checkdesc = popEditar.findViewById(R.id.Editar_checkExplicação);

        //Tratar das Horas
        popupAula_Hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timedial = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, mTimeSetListenerAlterar, hour, minute, true);
                timedial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timedial.show();
            }
        });
        mTimeSetListenerAlterar = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaeminutos = hourOfDay+":"+minute;
                popupAula_Hora.setText(horaeminutos);
            }
        };


       //Chamar cada informação do campo
        final String callid = listAula.get(position).getIdaula();
        popupAula_Hora.setText(listAula.get(position).getHoraaula());
        popaula_checkdesc.setText(listAula.get(position).getTipoAula());
        popupAula_Duracao.setText(listAula.get(position).getDuracaoaula());
        popupAula_Sala.setText(listAula.get(position).getSalaaula());

        popEditar.show();
        popup_editarAula = popEditar.findViewById(R.id.Editor_Button);
        popup_editarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adicionar uma turma
                Aula aula = new Aula();
                aula.setIdaula(listAula.get(position).getIdaula());
                aula.setTimeStamp(listAula.get(position).getTimeStamp());
                aula.setHoraaula(popupAula_Hora.getText().toString());
                aula.setDuracaoaula(popupAula_Duracao.getText().toString());
                aula.setSalaaula(popupAula_Sala.getText().toString());
                aula.setUserid(listAula.get(position).getUserid());
                if(aula.getTipoAula() != null){
                    aula.setTipoAula(popaula_checkdesc.getText().toString());
                }
                aulasref.child(callid).setValue(aula);
                popEditar.dismiss();
                enviaraluno();
            }
        });
    }

    private void enviaraluno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Pretende notificar todos os alunos desta alterações na aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviaremail();
                showMessage("Enviado");
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage("Aula Alterada");
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Notificar aluno");
        d.show();
    }

    private void enviaremail() {
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
                    String subject = "Alteração numa Aula";
                    String message = "Atenção, verifica a tua aplicação de gestão escolar porque uma disciplina alterou a hora ou sala da aula";
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
    public void onDeleteClick(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Tem a certeza que pretende eliminar esta aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String eliminarAual = listAula.get(position).getIdaula();
                aulasref.child(eliminarAual).removeValue();
                Toast.makeText(getContext(), "Eliminada", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Eliminar Aula");
        d.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseReference aulaarr = firebaseDatabase.getReference("Turma").child(idturma);
        if(mDBListener != null){
            aulaarr.removeEventListener(mDBListener);
            RvAulas.setLayoutManager(null);
        }

    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
