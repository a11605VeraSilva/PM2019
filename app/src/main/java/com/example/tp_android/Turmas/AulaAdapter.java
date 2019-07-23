package com.example.tp_android.Turmas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp_android.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AulaAdapter extends RecyclerView.Adapter<AulaAdapter.AulaViewHolder> {

    private Context mContext;
    private List<Aula> mData;
    private OnItemClickListener mListener;

    public AulaAdapter(Context mContext, List<Aula> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AulaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_listaaula, viewGroup, false);
        return new AulaViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AulaViewHolder aulaViewHolder, int i) {
        aulaViewHolder.auladata.setText(mData.get(i).getTimeStamp().toString());
        aulaViewHolder.aulahora.setText("Hora: "+mData.get(i).getHoraaula());
        aulaViewHolder.auladuracao.setText("Duração: "+mData.get(i).getDuracaoaula());
        aulaViewHolder.aulasala.setText("Sala: "+mData.get(i).getSalaaula());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class AulaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        TextView auladata, aulahora, auladuracao, aulasala;


        public AulaViewHolder(@NonNull View itemView) {
            super(itemView);
            auladata = itemView.findViewById(R.id.aula_data);
            aulahora = itemView.findViewById(R.id.aula_hora);
            auladuracao = itemView.findViewById(R.id.aula_duracao);
            aulasala = itemView.findViewById(R.id.aula_sala);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Selecione uma ação");
            MenuItem doEdit = menu.add(Menu.NONE, 1, 1, "Alterar data");
            MenuItem doSee = menu.add(Menu.NONE, 2, 2, "Ver detalhes");
            MenuItem doDelete = menu.add(Menu.NONE, 3, 3, "Eliminar Aula");

            doEdit.setOnMenuItemClickListener(this);
            doSee.setOnMenuItemClickListener(this);
            doDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onEditClick(position);
                            return true;
                        case 2:
                            mListener.onSeeClick(position);
                            return true;
                        case 3:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);

        void onSeeClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
