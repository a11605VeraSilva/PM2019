package com.example.tp_android.Alunos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp_android.R;
import com.example.tp_android.Turmas.Aula;

import java.util.List;

public class AulaAluno_Adapter extends RecyclerView.Adapter<AulaAluno_Adapter.AlunoAulaHolder> {

    private Context mContext;
    private List<Aula> mData;

    public AulaAluno_Adapter(Context mContext, List<Aula> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AulaAluno_Adapter.AlunoAulaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.rowaluno_aulas, viewGroup, false);
        return new AlunoAulaHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AulaAluno_Adapter.AlunoAulaHolder alunoAulaHolder, int i) {

        alunoAulaHolder.dataula.setText(mData.get(i).getTimeStamp().toString());
        alunoAulaHolder.horaaula.setText(mData.get(i).getHoraaula());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AlunoAulaHolder extends RecyclerView.ViewHolder{

        TextView dataula, horaaula;

        public AlunoAulaHolder(@NonNull View itemView) {
            super(itemView);
            dataula = itemView.findViewById(R.id.rowaluno_aulasdata);
            horaaula = itemView.findViewById(R.id.rowaluno_aulaHora);
        }
    }
}
