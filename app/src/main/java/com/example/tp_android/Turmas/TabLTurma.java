package com.example.tp_android.Turmas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tp_android.Email.SendMail;
import com.example.tp_android.Models.Administrativo;
import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Sumario;
import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.TurmaFragmento.FragmentAdmin;
import com.example.tp_android.Turmas.TurmaFragmento.FragmentAlunos;
import com.example.tp_android.Turmas.TurmaFragmento.FragmentAula;
import com.example.tp_android.Turmas.TurmaFragmento.FragmentSummary;
import com.example.tp_android.Turmas.TurmaFragmento.FragmentTarefas;
import com.example.tp_android.Turmas.TurmaFragmento.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class TabLTurma extends AppCompatActivity{


    private TabLayout tabLayout;
    private ViewPager viewPager;
    //inicializar views que vêm da lista de turmas
    private TextView nomeTurma;
    private TextView cursoTurma;
    //Botões
    //Declarar todos os botões
    private Button btnAdicionarAula;
    private Button btnAdicionarTarefas;
    private Button btnAdicionarAlunos;
    private Button btnSumarios;
    private Button btnCopiar;
    private Button btnpdf;

    //popup_adicionarAula
    private Dialog popAddAula;
    private TextView popupAula_data, popupAula_Hora, popupAula_Duracao, popupAula_Sala;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button popup_adicionarAula;
    private String idturma;
    private String nomedadi;
    private CheckBox popaula_hecktext;
    private CheckBox popaula_avisaraluno;
    private EditText popaula_checkdesc;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //popup_AdicionarAluno
    private Dialog popAddAluno;
    private TextView popaluno_email;
    private Button popaluno_button;
    private RadioButton radioAluno;
    private RadioButton radioAdministrativo;

    //popup adicionar tarefa
    private Dialog popTarefa;
    private TextView poptarefa_titulo, poptarefa_descricao, poptarefa_datafinal;
    private Button poptarefa_add;
    private DatePickerDialog.OnDateSetListener mDataSetTarefa;

    //popup adicionar sumario
    private Dialog popSumario;
    private TextView popsumario_data, popsumario_descricao;
    private Button popsumario_add;
    private DatePickerDialog.OnDateSetListener mDataSetSumario;

    //Copiar conteúdo de uma disciplina para criar outra
    private Dialog popCopiar;
    private TextView copiar_disciplina, copiar_acronimo, copiar_curso, copiar_ano, copiar_semestre;
    private Button copiar_buttoncriar;
    private String currentUser;
    private String idNovaTurma;

    //TESTAR
    String emailaluno;
    String subjectaluno;
    String messagealuno;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_lturma);

        //Inicializar firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //-------------------------------Chamar dados da turma
        nomeTurma = (TextView) findViewById(R.id.aulafrag_nomedisc);
        cursoTurma = (TextView) findViewById(R.id.aulafrag_nomecurso);
        String nometurmaaividade = getIntent().getExtras().getString("disciplinaTurma");
        nomeTurma.setText("Disciplina: " + nometurmaaividade);
        String cursoturmaact = getIntent().getExtras().getString("cursoTurma");
        cursoTurma.setText("Curso: "+cursoturmaact);
        idturma = getIntent().getExtras().getString("idTurma");
        nomedadi = getIntent().getExtras().getString("disciplinaTurma");


        //------------------------Organizar o Layout e funcionamento das tabs
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Fragmentos
        adapter.AddFragment(new FragmentAula(), "Aulas");
        adapter.AddFragment(new FragmentTarefas(), "Tarefas");
        adapter.AddFragment(new FragmentSummary(), "Sumário");
        adapter.AddFragment(new FragmentAlunos(), "Alunos");
        adapter.AddFragment(new FragmentAdmin(), "Admin");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //------------------------------------Popup Aula
        inipopupAula();

        //--------------------------------- Adicionar Aula
        btnAdicionarAula = (Button) findViewById(R.id.button_addAula);
        btnAdicionarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para ter sempre os campos limpos
                popupAula_data.setText("Data Aula");
                popupAula_Hora.setText("Hora Aula");
                popupAula_Duracao.setText("");
                popupAula_Sala.setText("");
                popAddAula.show();
            }
        });

        //----------------------------------Popup Aluno
        inipopupAluno();
        //----------------------------------Adicionar Aluno
        btnAdicionarAlunos = (Button) findViewById(R.id.button_addAlunos);
        btnAdicionarAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddAluno.show();
            }
        });

        //----------------------------------Popup Tarefa
        inipopupTarefa();

        // ---------------------------------Adicionar Tarefa
        btnAdicionarTarefas = (Button) findViewById(R.id.button_addTarefas);
        btnAdicionarTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poptarefa_titulo.setText("");
                poptarefa_descricao.setText("");
                poptarefa_datafinal.setText("Data Final da Tarefa");
                popTarefa.show();
            }
        });

        //-----------------------------------Popup Sumario
        inipopupSumario();

        //-----------------------------------AdicionarSumário
        btnSumarios = (Button) findViewById(R.id.button_addSumarios);
        btnSumarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popsumario_descricao.setText("");
                popsumario_data.setText("Data Sumário");
                popSumario.show();
            }
        });


        //------------------------------------Pop Copiar
        inipopCopiar();
        btnCopiar = (Button) findViewById(R.id.button_copiardiscilpina);
        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popCopiar.show();
            }
        });

    }


    private void inipopCopiar() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUser = firebaseUser.getUid();

        popCopiar = new Dialog(this);
        popCopiar.setContentView(R.layout.popup_addturma);
        popCopiar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popCopiar.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popCopiar.getWindow().getAttributes().gravity = Gravity.CENTER;

        copiar_disciplina = popCopiar.findViewById(R.id.AddTurma_Nome);
        copiar_acronimo = popCopiar.findViewById(R.id.AddTurma_Acronimo);
        copiar_ano = popCopiar.findViewById(R.id.AddTurma_Ano);
        copiar_curso = popCopiar.findViewById(R.id.AddTurma_Curso);
        copiar_semestre = popCopiar.findViewById(R.id.AddTurma_Semestre);
        copiar_buttoncriar = popCopiar.findViewById(R.id.AddTurma_Button);
        copiar_buttoncriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adicionar tarefa
                Turma turma = new Turma();
                turma.setIdTurma(UUID.randomUUID().toString());
                String anocorrente = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                turma.setAnocorrente(anocorrente);
                turma.setDisciplinaTurma(copiar_disciplina.getText().toString());
                turma.setAcronimoTurma(copiar_acronimo.getText().toString());
                turma.setAnoTurma(copiar_ano.getText().toString());
                turma.setCursoTurma(copiar_curso.getText().toString());
                turma.setSemestreTurma(copiar_semestre.getText().toString());
                final String verTeste = turma.getIdTurma();
                mostramaisfuncoes();
                DatabaseReference turmareference = firebaseDatabase.getReference("Turma").child(currentUser).child(verTeste);
                turmareference.setValue(turma).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Adicionado com sucesso");
                        idNovaTurma = verTeste;
                        copiar_semestre.setText("");
                        copiar_ano.setText("");
                        copiar_curso.setText("");
                        copiar_acronimo.setText("");
                        copiar_disciplina.setText("");
                        copyTarefaContext();
                        copySumarioContext();
                    }
                });
            }
        });
    }

    private void mostramaisfuncoes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pretende copiar também os alunos e os administradores?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copyAluno();
                copyAdmin();
                showMessage("Copiado");
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage("Copia Concluída");
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Copiar Alunos e Administradores");
        d.show();
    }

    private void copyAdmin() {
        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference("Administrativo").child(idturma);
        final DatabaseReference toPath = FirebaseDatabase.getInstance().getReference("Administrativo").child(idNovaTurma);

        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            System.out.println("Success");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Copy failed");
            }
        });
    }

    private void copyAluno() {
        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference("Aluno").child(idturma);
        final DatabaseReference toPath = FirebaseDatabase.getInstance().getReference("Aluno").child(idNovaTurma);

        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            System.out.println("Success");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Copy failed");
            }
        });

    }

    private void copySumarioContext() {
        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference("Sumario").child(idturma);
        final DatabaseReference toPath = FirebaseDatabase.getInstance().getReference("Sumario").child(idNovaTurma);

        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            System.out.println("Success");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Copy failed");
            }
        });
    }

    private void copyTarefaContext() {
        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference("Tarefa").child(idturma);
        final DatabaseReference toPath = FirebaseDatabase.getInstance().getReference("Tarefa").child(idNovaTurma);

        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            System.out.println("Success");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Copy failed");
            }
        });
    }

    public void onCheckEvento(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.Aula_ckheckevento:
                if (checked){
                    popaula_checkdesc.setVisibility(View.VISIBLE);
                    popup_adicionarAula.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Adicionar uma turma
                            Aula aula = new Aula();
                            aula.setIdaula(UUID.randomUUID().toString());
                            aula.setTimeStamp(popupAula_data.getText().toString());
                            aula.setHoraaula(popupAula_Hora.getText().toString());
                            aula.setDuracaoaula(popupAula_Duracao.getText().toString());
                            aula.setSalaaula(popupAula_Sala.getText().toString());
                            aula.setTipoAula(popaula_checkdesc.getText().toString());
                            aula.setUserid(firebaseAuth.getUid());
                            DatabaseReference aulareference = firebaseDatabase.getReference("Aula").child(idturma).child(aula.getIdaula());
                            aulareference.setValue(aula).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showMessage("Aula Adicionada");
                                    popupAula_data.setText("Data Aula");
                                    popupAula_Hora.setText("Hora Aula");
                                    popupAula_Duracao.setText("");
                                    popupAula_Sala.setText("");
                                    popaula_checkdesc.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage("Erro ao adicionar: "+e.getMessage());
                                }
                            });
                        }
                    });
                }else{
                    popaula_checkdesc.setVisibility(View.GONE);

                }
                break;

        }
    }

    private void inipopupAula(){
        popAddAula = new Dialog(this);
        popAddAula.setContentView(R.layout.popup_addaula);
        popAddAula.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddAula.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddAula.getWindow().getAttributes().gravity = Gravity.CENTER;


        popupAula_data = popAddAula.findViewById(R.id.Aula_Data);
        popupAula_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TabLTurma.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDataSetListener, year, month, day);
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
                popupAula_data.setText(datafinal);
            }
        };

        popupAula_Hora = popAddAula.findViewById(R.id.Aula_Hora);
        popupAula_Hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timedial = new TimePickerDialog(TabLTurma.this, android.R.style.Theme_Holo_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
                timedial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timedial.show();
            }
        });
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaeminutos = hourOfDay+":"+minute;
                popupAula_Hora.setText(horaeminutos);
            }
        };

        popaula_hecktext = popAddAula.findViewById(R.id.Aula_ckheckevento);
        popaula_checkdesc = popAddAula.findViewById(R.id.Aula_checkExplicação);
        popupAula_Duracao = popAddAula.findViewById(R.id.Aula_Duracao);
        popupAula_Sala = popAddAula.findViewById(R.id.Aula_Sala);
        popup_adicionarAula = popAddAula.findViewById(R.id.Aula_Button);
        popup_adicionarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Adicionar uma turma
                Aula aula = new Aula();
                aula.setIdaula(UUID.randomUUID().toString());
                aula.setTimeStamp(popupAula_data.getText().toString());
                aula.setHoraaula(popupAula_Hora.getText().toString());
                aula.setDuracaoaula(popupAula_Duracao.getText().toString());
                aula.setSalaaula(popupAula_Sala.getText().toString());
                aula.setUserid(firebaseAuth.getUid());
                DatabaseReference aulareference = firebaseDatabase.getReference("Aula").child(idturma).child(aula.getIdaula());
                aulareference.setValue(aula).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Aula Adicionada");
                        popAddAula.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Erro ao adicionar: "+e.getMessage());
                    }
                });
                showQuestion();
            }
        });
    }

    private void showQuestion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pretende notificar todos os alunos da nova aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendEmail();
                showMessage("Enviado");
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage("Aula adicionada");
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Notificar aluno");
        d.show();
    }

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
                    String subject = "Marcação de Aula";
                    String message = "No dia "+popupAula_data.getText().toString()+" haverá aula na sala "+popupAula_Sala.getText().toString()+" às "+popupAula_Hora.getText().toString()+". ";
                    //Creating SendMail object
                    SendMail sm = new SendMail(TabLTurma.this, email, subject, message);
                    //Executing sendmail to send email
                    sm.execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void inipopupAluno(){
        popAddAluno = new Dialog(TabLTurma.this);
        popAddAluno.setContentView(R.layout.popup_addaluno);
        popAddAluno.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddAluno.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddAluno.getWindow().getAttributes().gravity = Gravity.CENTER;
        popaluno_email = popAddAluno.findViewById(R.id.Convidar_alunoemail);
        popaluno_button = popAddAluno.findViewById(R.id.Convidar_Button);
        radioAluno = popAddAluno.findViewById(R.id.Convidar_Aluno);
        radioAdministrativo = popAddAluno.findViewById(R.id.Convidar_administrativo);

        popaluno_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioAdministrativo.isChecked()){
                    final Administrativo admin = new Administrativo();
                    Random rd = new Random();

                    admin.setAdminid(UUID.randomUUID().toString());
                    admin.setEmail(popaluno_email.getText().toString());
                    String emailadmin = popaluno_email.getText().toString();
                    String adminteste = emailadmin.replace(".", ",");
                    Log.d("EMAIL", adminteste);
                    String tipo = "Administrativo";
                    admin.setTipo(tipo);
                    DatabaseReference adminref = firebaseDatabase.getReference("Administrativo").child(idturma).child(adminteste);
                    adminref.setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            String emailadmin = popaluno_email.getText().toString();
                            if(emailadmin.isEmpty()){
                                showMessage("Qualquer problema aconteceu ao criar a conta");
                            }else{
                                showMessage("Foi adicionado à turma o Administrativo");
                            }

                            popaluno_email.setText(" ");
                            radioAdministrativo.setChecked(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Erro ao convidar administrativo "+e.getMessage());
                        }
                    });



                }if(radioAluno.isChecked()){
                    final Aluno aluno = new Aluno();
                    Random rd = new Random();

                    aluno.setAlunoid(UUID.randomUUID().toString());
                    aluno.setEmail(popaluno_email.getText().toString());
                    aluno.setCodigo(rd.nextInt(9999999));
                    aluno.setTurmaid(idturma);
                    String testeemail = aluno.getEmail().replace(".", ",");
                    Log.d("EMAIL", testeemail);

                    String tipo2 = "Aluno";
                    aluno.setTipo(tipo2);
                    final DatabaseReference alunoref = firebaseDatabase.getReference("Aluno").child(idturma).child(testeemail);
                    alunoref.setValue(aluno).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //showMessage("Enviado código de acesso ao aluno");
                            String email = popaluno_email.getText().toString().trim();
                            String passwordLogin = aluno.getCodigo().toString();
                            if(email.isEmpty() || passwordLogin.isEmpty()){
                                showMessage("Qualquer problema aconteceu ao criar a conta");
                            }else{
                                CreateAluno(email, passwordLogin);
                            }


                            //Executing sendmail to send email

                            emailaluno = popaluno_email.getText().toString().trim();
                            subjectaluno = "Código Acesso Gestão Escolar";
                            messagealuno = "Bem vindo à aplicação Gestão escolar, foi convidado pela professora:"+" "+ firebaseUser.getEmail() +".<br><br>Faça o download da aplicação"+ " <a href='https://www.netbooks.pt/pw/gestaoescolar.apk'>aqui</a> " + "e insira o seguinte código: "+aluno.getCodigo();
                            //Creating SendMail object
                            SendMail sm = new SendMail(TabLTurma.this, emailaluno, subjectaluno, messagealuno);
                            sm.execute();
                            popAddAluno.dismiss();
                            popaluno_email.setText(" ");
                            radioAluno.setChecked(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Erro ao convidar aluno "+e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void CreateAluno(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            showMessage("Criado");
                        }else{
                            showMessage("Esta conta já tem autenticação" + task.getException().getMessage());
                        }
                    }
                });
    }

    private void inipopupSumario(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        popSumario = new Dialog(this);
        popSumario.setContentView(R.layout.popup_sumario);
        popSumario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popSumario.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popSumario.getWindow().getAttributes().gravity = Gravity.CENTER;

        popsumario_descricao = popSumario.findViewById(R.id.sumario_descricao);
        popsumario_data = popSumario.findViewById(R.id.sumario_dataAula);
        popsumario_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TabLTurma.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDataSetSumario, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetSumario = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d("Data", "a data é: "+ year + "/"+month+"/"+dayOfMonth);
                String datasum = dayOfMonth + "/" + month + "/" + year;
                popsumario_data.setText(datasum);
            }
        };
        popsumario_add = popSumario.findViewById(R.id.sumario_button);


        popsumario_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adicionar sumario
                Sumario sumario = new Sumario();
                sumario.setIdsumario(UUID.randomUUID().toString());
                sumario.setDescricaosumario(popsumario_descricao.getText().toString());
                sumario.setDatasumario(popsumario_data.getText().toString());
                sumario.setIdutilizador(firebaseAuth.getUid());
                DatabaseReference sumref = firebaseDatabase.getReference("Sumario").child(idturma).child(sumario.getIdsumario());
                sumref.setValue(sumario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Sumario Adicionado");
                        sumarioask();
                        popSumario.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Erro ao adicionar: "+e.getMessage());
                    }
                });
            }
        });
    }

    private void sumarioask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pretende notificar todos os alunos sobre esta sumário?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviasumario();
                showMessage("Enviado");
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage("Sumário adicionado");
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Notificar aluno");
        d.show();
    }

    private void enviasumario() {
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
                    String subject = "Atenção Novo Sumário";
                    String message = "Sumário: "+popsumario_descricao.getText().toString()+"<br><br>Este sumário pertence à aula: "+popsumario_data.getText().toString()+". ";
                    //Creating SendMail object
                    SendMail sm = new SendMail(TabLTurma.this, email, subject, message);
                    //Executing sendmail to send email
                    sm.execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(TabLTurma.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
    }

    private void inipopupTarefa() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        popTarefa = new Dialog(this);
        popTarefa.setContentView(R.layout.popup_tarefa);
        popTarefa.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popTarefa.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popTarefa.getWindow().getAttributes().gravity = Gravity.CENTER;

        poptarefa_titulo = popTarefa.findViewById(R.id.Tarefa_titulo);
        poptarefa_descricao = popTarefa.findViewById(R.id.sumario_descricao);
        poptarefa_datafinal = popTarefa.findViewById(R.id.Tarefa_DataFinal);
        poptarefa_datafinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TabLTurma.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDataSetTarefa, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetTarefa = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d("Data", "a data é: "+ year + "/"+month+"/"+dayOfMonth);
                String datatarefa = dayOfMonth + "/" + month + "/" + year;
                poptarefa_datafinal.setText(datatarefa);
            }
        };
        poptarefa_add = popTarefa.findViewById(R.id.sumario_button);


        poptarefa_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adicionar tarefa
                Tarefa tarefa = new Tarefa();
                tarefa.setIdtarefa(UUID.randomUUID().toString());
                tarefa.setTitulotarefa(poptarefa_titulo.getText().toString());
                tarefa.setDescricaotarefa(poptarefa_descricao.getText().toString());
                tarefa.setDatafinaltarefa(poptarefa_datafinal.getText().toString());
                tarefa.setUserid(firebaseAuth.getUid());
                mostrarpergunta();
                DatabaseReference tarefaref = firebaseDatabase.getReference("Tarefa").child(idturma).child(tarefa.getIdtarefa());
                tarefaref.setValue(tarefa).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Tarefa Adicionada");
                        popTarefa.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Erro ao adicionar: "+e.getMessage());
                    }
                });
            }
        });
    }

    private void mostrarpergunta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pretende notificar todos os alunos sobre esta nova tarefa?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviaemail();
                showMessage("Enviado");
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage("Tarefa Adicionada");
            }
        });
        AlertDialog d = builder.create();
        d.setTitle("Notificar aluno");
        d.show();
    }

    private void enviaemail() {
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
                    String subject = "Atenção Nova Tarefa";
                    String message = "Tarefa: "+poptarefa_descricao.getText().toString()+"<br><br>Esta tarefa termina em: "+poptarefa_datafinal.getText().toString()+". ";
                    //Creating SendMail object
                    SendMail sm = new SendMail(TabLTurma.this, email, subject, message);
                    //Executing sendmail to send email
                    sm.execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
