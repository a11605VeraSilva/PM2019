package com.example.tp_android.Horarios;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;
import com.example.tp_android.Turmas.Aula;
import com.example.tp_android.Turmas.AulaAdapter;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {

    private Context mContext;
    private List<Aula> mData;

    public HorarioAdapter(Context mContext, List<Aula> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_horariolist, viewGroup, false);
        return new HorarioViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder horarioViewHolder, int i) {
        horarioViewHolder.textDisciplina.setText(mData.get(i).getTimeStamp().toString());
        horarioViewHolder.textHora.setText("Hora: "+mData.get(i).getHoraaula());
        horarioViewHolder.textsala.setText("Sala: "+mData.get(i).getSalaaula());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class HorarioViewHolder extends RecyclerView.ViewHolder{

        ImageView imageicon;
        TextView textDisciplina, textHora, textsala;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            imageicon = itemView.findViewById(R.id.horario_image);
            textDisciplina = itemView.findViewById(R.id.horario_dataatividade);
            textHora = itemView.findViewById(R.id.horario_disciplinaatividade);
            textsala = itemView.findViewById(R.id.horario_salaatividade);
        }
    }
}
