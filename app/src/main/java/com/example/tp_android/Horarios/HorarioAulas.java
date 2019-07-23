package com.example.tp_android.Horarios;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Aula;
import com.example.tp_android.Turmas.MenuTurmas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.utils.Utils;

public class HorarioAulas extends AppCompatActivity {
    private TextView textData;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private List<Aula> aulaList;
    private HorarioAdapter horarioAdapter;
    private FloatingActionButton goToday;
    private boolean immediate;
    private RecyclerView RvEventos;
    private String currentUser;
    private String idTurma;

   //Declarar variavel calendario
   private HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_aulas);
        RvEventos = findViewById(R.id.horario_rvdatas);

        inicializar();

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.getSelectedItemStyle().setColorMiddleText(Color.GRAY).setColorBottomText(Color.GRAY).setColorTopText(Color.GRAY);

        /*Inicialize button */
        goToday = findViewById(R.id.horario_today);
        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalCalendar.goToday(immediate);
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                final int day = date.get(Calendar.DAY_OF_MONTH);
                final int month = date.get(Calendar.MONTH);
                final int year = date.get(Calendar.YEAR);
                //textView.setText("Data: " + day+"-"+month+"-"+year);

                final String dataCalendario = day+"/"+(month+1)+"/"+year;
                // Log.i("Data Selecionada", dataCalendario);

                aulaList = new ArrayList<>();
                firebaseUser = firebaseAuth.getCurrentUser();
                currentUser = firebaseUser.getUid();


                DatabaseReference refTurma = firebaseDatabase.getReference("Turma").child(currentUser);
                refTurma.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap: dataSnapshot.getChildren()){
                            Turma turma = snap.getValue(Turma.class);
                            idTurma = turma.getIdTurma();
                            databaseReference = firebaseDatabase.getReference("Aula").child(idTurma);
                            Query query = databaseReference.orderByChild("horaaula");
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // aulaList.clear();
                                    for (DataSnapshot snap: dataSnapshot.getChildren()){
                                        Aula aula = snap.getValue(Aula.class);
                                        String dataAula = aula.getTimeStamp().toString();

                                        String data_aula = dataAula;
                                        String data_calendario = dataCalendario;
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Date convertedDate_aula = new Date();
                                        Date convertedDate_calendario = new Date();

                                        try {
                                            convertedDate_aula = dateFormat.parse(data_aula);
                                            convertedDate_calendario = dateFormat.parse(data_calendario);
                                            if (convertedDate_calendario.equals(convertedDate_aula)) {
                                                //Log.i("DATAS MD:", "igual");
                                                aulaList.add(aula);
                                            } /*else {
                                                Log.i("DATAAAAAAAA"," diferente");
                                            }*/
                                        } catch (ParseException e) {
                                            e.printStackTrace();

                                        }
                                    }
                                    horarioAdapter = new HorarioAdapter(HorarioAulas.this, aulaList);
                                    RvEventos.setAdapter(horarioAdapter);
                                    RvEventos.setLayoutManager(new LinearLayoutManager(HorarioAulas.this));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void inicializar() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
