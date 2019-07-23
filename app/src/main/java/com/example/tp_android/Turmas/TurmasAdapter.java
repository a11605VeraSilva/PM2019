package com.example.tp_android.Turmas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tp_android.Models.Turma;
import com.example.tp_android.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TurmasAdapter extends RecyclerView.Adapter<TurmasAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<Turma> mTurma;
    List<Turma> testeTurma;
    private OnItemClickListener mListener;


    public TurmasAdapter(Context mContext, List<Turma> mTurma){
        this.mContext = mContext;
        this.mTurma = mTurma;
        testeTurma = new ArrayList<>(mTurma);
    }

    @NonNull
    @Override
    public TurmasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_turma_item, viewGroup, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TurmasAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvNome.setText(mTurma.get(i).getDisciplinaTurma());
        myViewHolder.tvCurso.setText("Curso: "+mTurma.get(i).getCursoTurma());
        myViewHolder.tvAno.setText("Ano Turma: "+mTurma.get(i).getAnoTurma());
        myViewHolder.tvAcronimo.setText("(" + mTurma.get(i).getAcronimoTurma()+")");
        myViewHolder.tvAnoAtual.setText(mTurma.get(i).getAnocorrente());
    }

    @Override
    public int getItemCount() {
        return mTurma.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charSTring = constraint.toString();
            if(charSTring.isEmpty()){
                testeTurma = mTurma; //se não funcionar verificar esta linha
            } else{
                List<Turma> filteredList = new ArrayList<>();
                for(Turma row: mTurma){
                    if(row.getDisciplinaTurma().toLowerCase().contains(charSTring.toLowerCase()) || row.getAcronimoTurma().contains(charSTring) || row.getAnocorrente().contains(charSTring) || row.getCursoTurma().toLowerCase().contains(charSTring)){
                        filteredList.add(row);
                    }
                }
                testeTurma = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = testeTurma;
            filterResults.count = testeTurma.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //testeTurma = (ArrayList<Turma>) results.values;
            mTurma.clear();
            notifyDataSetChanged();
            mTurma.addAll((ArrayList<Turma>)results.values);
            notifyDataSetChanged();

        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView tvNome, tvCurso, tvAno, tvAnoAtual, tvAcronimo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.lista_NomeTurma);
            tvCurso = itemView.findViewById(R.id.lista_Curso);
            tvAno = itemView.findViewById(R.id.lista_Ano);
            tvAnoAtual = itemView.findViewById(R.id.lista_DataAtual);
            tvAcronimo = itemView.findViewById(R.id.lista_AcronimoTurma);

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
            MenuItem doOpen = menu.add(Menu.NONE, 1, 1, "Abrir");
            MenuItem doEdit = menu.add(Menu.NONE, 2, 2, "Editar");
            MenuItem doConte = menu.add(Menu.NONE, 3, 3, "Conteúdos");
            MenuItem doDelete = menu.add(Menu.NONE, 4, 4, "Eliminar");

            doOpen.setOnMenuItemClickListener(this);
            doEdit.setOnMenuItemClickListener(this);
            doConte.setOnMenuItemClickListener(this);
            doDelete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                   switch (item.getItemId()){
                       case 1:
                           mListener.onOpenClick(position);
                           return true;
                       case 2:
                           mListener.onEditClick(position);
                           return true;
                       case 3:
                           mListener.onConteudoClick(position);
                           return true;
                       case 4:
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

        void onOpenClick(int position);

        void onEditClick(int position);

        void onConteudoClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    private String timestampToString (long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("yyyy", calendar).toString();
        return date;
    }

    public void removeItem(int position){
        mTurma.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTurma.size());
    }
}
