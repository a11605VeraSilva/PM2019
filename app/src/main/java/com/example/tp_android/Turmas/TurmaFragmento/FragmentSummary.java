package com.example.tp_android.Turmas.TurmaFragmento;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Email.SendMail;
import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Sumario;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.SumarioAdapter;
import com.example.tp_android.Turmas.Tarefa;
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

public class FragmentSummary extends Fragment implements SumarioAdapter.OnItemClickListener{

    View view;

    //Sumarios
    private RecyclerView RvSumario;
    private String idturma;
    private String key;
    private String nodi;
    private List<Sumario> listSumario;
    private SumarioAdapter sumarioAdapter;

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference sumarioref;

    //Para editar sumarios
    private Dialog popeditsum;
    private TextView datasumario, descricaosumario;
    private Button btnsalvar;
    private DatePickerDialog.OnDateSetListener mDataSetListenerAlterar;
    private ValueEventListener mDBListener;



    public FragmentSummary() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sum_fragment, container, false);

        //inicializar a firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        idturma = getActivity().getIntent().getExtras().getString("idTurma");
        nodi =  getActivity().getIntent().getExtras().getString("disciplinaTurma");
        sumarioref = firebaseDatabase.getReference("Sumario").child(idturma);

        RvSumario = view.findViewById(R.id.summary_rv);
        iniRvSum();

        return view;
    }

    private void iniRvSum() {
        listSumario = new ArrayList<>();
        sumarioAdapter = new SumarioAdapter(getActivity(), listSumario);
        RvSumario.setAdapter(sumarioAdapter);
        RvSumario.setLayoutManager(new LinearLayoutManager(getContext()));
        sumarioAdapter.setOnItemClickListener(this);

        sumarioref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSumario.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    Sumario sumario = snap.getValue(Sumario.class);
                    key = snap.getKey();
                    listSumario.add(sumario);
                }
                sumarioAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Para ver detalhes ou eliminar click longo", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEditClcik(final int positon) {
        popeditsum = new Dialog(getContext());
        popeditsum.setContentView(R.layout.popupeditar_sumario);
        popeditsum.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popeditsum.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popeditsum.getWindow().getAttributes().gravity = Gravity.CENTER;

        //Declarar todos os campos
        datasumario = popeditsum.findViewById(R.id.editar_sumariodata);
        descricaosumario = popeditsum.findViewById(R.id.editar_sumariodescricao);
        btnsalvar = popeditsum.findViewById(R.id.editar_sumariobutton);

        //Chamar todos os dados
        datasumario.setText(listSumario.get(positon).getDatasumario());
        descricaosumario.setText(listSumario.get(positon).getDescricaosumario());

        //String para apanhar o id
        final String callsum = listSumario.get(positon).getIdsumario();

        //Alterar data
        datasumario.setOnClickListener(new View.OnClickListener() {
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
                datasumario.setText(datafinal);
            }
        };

        //Gravar novos dados
        popeditsum.show();
        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sumario sum = new Sumario();
                sum.setIdutilizador(listSumario.get(positon).getIdutilizador());
                sum.setIdsumario(listSumario.get(positon).getIdsumario());
                sum.setDatasumario(datasumario.getText().toString());
                sum.setDescricaosumario(descricaosumario.getText().toString());
                sumarioref.child(callsum).setValue(sum);
                popeditsum.dismiss();
                sumarioalterar();
            }
        });
    }

    private void sumarioalterar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Pretende notificar todos os alunos desta alterações na aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviaremail();
                Toast.makeText(getContext(), "Enviado", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Sumário Alterado", Toast.LENGTH_SHORT).show();;
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
                    String subject = "Alteração num Sumário";
                    String message = "Atenção, verifica a tua aplicação de gestão escolar porque o sumário de "+nodi+ " foi alterado pelo teu Professor";
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
        builder.setMessage("Tem a certeza que pretende eliminar este sumário?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idSum = listSumario.get(position).getIdsumario();
                sumarioref.child(idSum).removeValue();
                Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = builder.create();
        d.setTitle("Eliminar Sumário");
        d.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDBListener != null){
            sumarioref.removeEventListener(mDBListener);
            RvSumario.setLayoutManager(null);
        }

    }
}
