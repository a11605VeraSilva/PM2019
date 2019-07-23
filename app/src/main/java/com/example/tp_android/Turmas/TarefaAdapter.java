package com.example.tp_android.Turmas;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.mail.FetchProfile;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private Context mContext;
    private List<Tarefa> mData;
    private OnItemClickListener mListener;

    public TarefaAdapter(Context mContext, List<Tarefa> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_listatarefa, viewGroup, false);
        return new TarefaViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder tarefaViewHolder, int i) {

        tarefaViewHolder.tarefatitulo.setText(mData.get(i).getTitulotarefa());
        tarefaViewHolder.tarefadesc.setText(mData.get(i).getDescricaotarefa());
        tarefaViewHolder.tarefadata.setText("Data Final: " + mData.get(i).getDatafinaltarefa());

    }

    public void deleteItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class TarefaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,  View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView tarefatitulo, tarefadesc, tarefadata;

        public TarefaViewHolder(@NonNull final View itemView) {
            super(itemView);
            tarefatitulo = itemView.findViewById(R.id.Tarefatitulo);
            tarefadesc = itemView.findViewById(R.id.tarefaatividade);
            tarefadata = itemView.findViewById(R.id.tarefadatafim);

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
            MenuItem doSeen = menu.add(Menu.NONE, 1, 1, "Ver Detalhes");
            MenuItem doDelete = menu.add(Menu.NONE, 2, 2, "Eliminar");

            doSeen.setOnMenuItemClickListener(this);
            doDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch(item.getItemId()){
                        case 1:
                            mListener.onSeen(position);
                            return true;
                        case 2:
                            mListener.onDelete(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onSeen(int position);

        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
