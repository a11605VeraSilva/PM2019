package com.example.tp_android.Alunos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp_android.Models.Sumario;
import com.example.tp_android.R;

import java.util.List;

public class SumarioAluno_Adapter extends RecyclerView.Adapter<SumarioAluno_Adapter.AlunoSumarioHolder> {

    private Context mContext;
    private List<Sumario> mData;

    public SumarioAluno_Adapter(Context mContext, List<Sumario> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AlunoSumarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.rowaluno_versumario, viewGroup, false);
        return new AlunoSumarioHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoSumarioHolder alunoSumarioHolder, int i) {

        alunoSumarioHolder.sumariotexto.setText(mData.get(i).getDescricaosumario());
        alunoSumarioHolder.sumariodata.setText(mData.get(i).getDatasumario());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AlunoSumarioHolder extends RecyclerView.ViewHolder{

        TextView sumariotexto, sumariodata;

        public AlunoSumarioHolder(@NonNull View itemView) {
            super(itemView);
            sumariotexto = itemView.findViewById(R.id.rowaluno_sumario);
            sumariodata = itemView.findViewById(R.id.rowaluno_sumariodata);
        }
    }

}
