package com.example.tp_android.Alunos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp_android.R;
import com.example.tp_android.Turmas.Tarefa;

import java.util.List;

public class TarefaAluno_Adapter extends RecyclerView.Adapter<TarefaAluno_Adapter.AlunoTarefaViewHolder> {

    private Context mContext;
    private List<Tarefa> mData;

    public TarefaAluno_Adapter(Context mContext, List<Tarefa> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public TarefaAluno_Adapter.AlunoTarefaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.rowaluno_vertarefa, viewGroup, false);
        return new AlunoTarefaViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaAluno_Adapter.AlunoTarefaViewHolder alunoTarefaViewHolder, int i) {
        alunoTarefaViewHolder.tarefanome.setText(mData.get(i).getTitulotarefa());
        alunoTarefaViewHolder.tarefadata.setText(mData.get(i).getDatafinaltarefa());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AlunoTarefaViewHolder extends RecyclerView.ViewHolder {

        TextView tarefanome, tarefadata;

        public AlunoTarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tarefanome = itemView.findViewById(R.id.rowaluno_tarefas);
            tarefadata = itemView.findViewById(R.id.rowaluno_tarefasdata);

        }
    }
}
