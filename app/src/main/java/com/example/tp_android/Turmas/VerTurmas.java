package com.example.tp_android.Turmas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerTurmas extends AppCompatActivity implements TurmasAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TurmasAdapter turmasAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference turmarref;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private List<Turma> turmaList;
    private ValueEventListener mDBListener;
    Dialog popedit;
    TextView popup_NomeTurma, popup_acronimoTurma, popup_CursoTurma, popup_AnoTurma, popup_SemestreTurma, pop_anocorrente;
    Button popup_edit;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_turmas);
        recyclerView = findViewById(R.id.rvv_turmas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        currentUser = firebaseUser.getUid();
        turmarref = firebaseDatabase.getReference("Turma").child(currentUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();


        turmaList = new ArrayList<>();
        turmasAdapter = new TurmasAdapter(VerTurmas.this, turmaList);
        recyclerView.setAdapter(turmasAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        turmasAdapter.setOnItemClickListener(VerTurmas.this);

        mDBListener = turmarref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    turmaList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Turma turmaa = snapshot.getValue(Turma.class);
                        turmaList.add(turmaa);
                    }
                    turmasAdapter.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                turmasAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent entrarTurma = new Intent(this, TabLTurma.class);
        entrarTurma.putExtra("disciplinaTurma", turmaList.get(position).getDisciplinaTurma());
        entrarTurma.putExtra("cursoTurma", turmaList.get(position).getCursoTurma());
        entrarTurma.putExtra("idTurma", turmaList.get(position).getIdTurma());
        startActivity(entrarTurma);
    }

    @Override
    public void onOpenClick(int position) {
        Intent entrarTurma = new Intent(this, TabLTurma.class);
        entrarTurma.putExtra("disciplinaTurma", turmaList.get(position).getDisciplinaTurma());
        entrarTurma.putExtra("cursoTurma", turmaList.get(position).getCursoTurma());
        entrarTurma.putExtra("idTurma", turmaList.get(position).getIdTurma());
        startActivity(entrarTurma);
    }

    @Override
    public void onEditClick(final int position) {
        popedit = new Dialog(this);
        popedit.setContentView(R.layout.popup_editturma);
        popedit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popedit.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popedit.getWindow().getAttributes().gravity = Gravity.CENTER;
        popup_NomeTurma = popedit.findViewById(R.id.EditTurma_Nome);
        popup_acronimoTurma = popedit.findViewById(R.id.EditTurma_Acronimo);
        popup_CursoTurma = popedit.findViewById(R.id.EditTurma_Curso);
        popup_AnoTurma = popedit.findViewById(R.id.EditTurma_Ano);
        popup_SemestreTurma = popedit.findViewById(R.id.EditTurma_Semestre);
        popup_edit = popedit.findViewById(R.id.EditTurma_Button);
        pop_anocorrente = popedit.findViewById(R.id.editar_anocorrente);

        popup_NomeTurma.setText(turmaList.get(position).getDisciplinaTurma());
        popup_acronimoTurma.setText(turmaList.get(position).getAcronimoTurma());
        popup_CursoTurma.setText(turmaList.get(position).getCursoTurma());
        popup_AnoTurma.setText(turmaList.get(position).getAnoTurma());
        popup_SemestreTurma.setText(turmaList.get(position).getSemestreTurma());
        pop_anocorrente.setText(turmaList.get(position).getAnocorrente());
        final String verTeste = turmaList.get(position).getIdTurma();
        Log.d("O ID É", verTeste);
        popedit.show();
        //Para gravar
        popup_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Turma turrma = new Turma();
                turrma.setIdTurma(turmaList.get(position).getIdTurma());
                turrma.setAnocorrente(pop_anocorrente.getText().toString().trim());
                turrma.setDisciplinaTurma(popup_NomeTurma.getText().toString().trim());
                turrma.setAcronimoTurma(popup_acronimoTurma.getText().toString().trim());
                turrma.setCursoTurma(popup_CursoTurma.getText().toString().trim());
                turrma.setAnoTurma(popup_AnoTurma.getText().toString().trim());
                turrma.setSemestreTurma(popup_SemestreTurma.getText().toString().trim());
                turmarref.child(verTeste).setValue(turrma);
                popedit.dismiss();
            }
        });
    }

    @Override
    public void onConteudoClick(int position) {
        Toast.makeText(VerTurmas.this,"Conteudo", Toast.LENGTH_SHORT).show();
        Intent entrarTurma = new Intent(this, ConteudosTurma.class);
        entrarTurma.putExtra("disciplinaTurma", turmaList.get(position).getDisciplinaTurma());
        entrarTurma.putExtra("cursoTurma", turmaList.get(position).getCursoTurma());
        entrarTurma.putExtra("idTurma", turmaList.get(position).getIdTurma());
        entrarTurma.putExtra("acronimoTurma", turmaList.get(position).getAcronimoTurma());
        entrarTurma.putExtra("anoTurma", turmaList.get(position).getAnoTurma());
        entrarTurma.putExtra("anocorrente", turmaList.get(position).getAnocorrente());
        entrarTurma.putExtra("semestreTurma", turmaList.get(position).getSemestreTurma());
        startActivity(entrarTurma);
    }

    @Override
    public void onDeleteClick(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tem a certeza que pretende eliminar esta disciplina?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String eliminar = turmaList.get(position).getIdTurma();
                turmarref.child(eliminar).removeValue();
                turmasAdapter.removeItem(position);
                Toast.makeText(VerTurmas.this, "Eliminada", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VerTurmas.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = builder.create();
        d.setTitle("Eliminar Disciplina");
        d.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDBListener != null){
            turmarref.removeEventListener(mDBListener);
            recyclerView.setLayoutManager(null);
        }

    }
}
