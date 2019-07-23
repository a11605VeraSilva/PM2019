package com.example.tp_android.Mural;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Tarefa;
import com.example.tp_android.Turmas.TarefaAdapter;

import java.util.List;

public class MuralAdapter extends RecyclerView.Adapter<MuralAdapter.MuralViewHolder> {

    private Context mContext;
    private List<Tarefa> mData;
    private List<Turma> mTurma;

    public MuralAdapter(Context mContext, List<Tarefa> mData, List<Turma> mTurma) {
        this.mContext = mContext;
        this.mData = mData;
        this.mTurma= mTurma;
    }

    @NonNull
    @Override
    public MuralViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_muralaluno, viewGroup, false);
        return new MuralViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MuralViewHolder muralViewHolder, int i) {
        muralViewHolder.tarefatitulo.setText(mData.get(i).getTitulotarefa());
        muralViewHolder.tarefadesc.setText(mTurma.get(i).getDisciplinaTurma());
        muralViewHolder.tarefadata.setText(mData.get(i).getDatafinaltarefa());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MuralViewHolder extends RecyclerView.ViewHolder {

        TextView tarefatitulo, tarefadesc, tarefadata;

        public MuralViewHolder(@NonNull final View itemView) {
            super(itemView);
            tarefatitulo = itemView.findViewById(R.id.Mural_Tarefa);
            tarefadesc = itemView.findViewById(R.id.Mural_Tarefaturma);
            tarefadata = itemView.findViewById(R.id.Mural_TarefaFim);
        }
    }
}
