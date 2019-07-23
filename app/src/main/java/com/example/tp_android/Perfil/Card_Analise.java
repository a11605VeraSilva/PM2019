package com.example.tp_android.Perfil;

import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tp_android.Models.Aluno;
import com.example.tp_android.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Card_Analise extends AppCompatActivity {

    private static String TAG="Card_Analise";

    private float[] yData = {25,10,10,30,40};
    private String[] xData = {"Vera","Ana","Paula","Carla","Maria"};


    //lista alunos
    private List<Aluno> list;

    // Grafico
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card__analise);

        Log.d(TAG, "onCreate: ");

        pieChart = (PieChart) findViewById(R.id.idPieChart);


        DatabaseReference countalunos = FirebaseDatabase.getInstance().getReference("Aluno");
        list = new ArrayList<>();
        countalunos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    int teste = (int) snap.getChildrenCount();
                    //int c = Integer.(teste);


                   // Log.d("VALORRRRRRRRRRRRRRRRR", c+"");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Análise");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);


        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "Valor selecionado");
                Log.d(TAG, "Valor selecionado" + e.toString());
                Log.d(TAG, "Valor selecionado" + h.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for(int i=0; i< yData.length; i++){
            yEntries.add(new PieEntry(yData[i], i));
        }

        for(int i=1; i< xData.length; i++){
            xEntries.add(xData[i]);
        }

        //Criar  o data set
        PieDataSet pieDataSet = new PieDataSet(yEntries, "Valores");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //Adicionares cores ao dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);

        pieDataSet.setColors(colors);

        //Adicionar legenda
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);


        //Criar objetos
        PieData pd = new PieData(pieDataSet);
        pieChart.setData(pd);
        pieChart.invalidate();

    }
}
