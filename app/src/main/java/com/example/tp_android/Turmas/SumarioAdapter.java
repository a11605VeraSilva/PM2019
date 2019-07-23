package com.example.tp_android.Turmas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tp_android.Models.Sumario;
import com.example.tp_android.R;

import java.util.List;

public class SumarioAdapter extends RecyclerView.Adapter<SumarioAdapter.SumarioViewHolder> {

    private Context mContext;
    private List<Sumario> mData;
    private OnItemClickListener mListener;

    public SumarioAdapter(Context mContext, List<Sumario> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SumarioAdapter.SumarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_sumario, viewGroup, false);
        return new SumarioAdapter.SumarioViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull SumarioAdapter.SumarioViewHolder sumarioViewHolder, int i) {
        sumarioViewHolder.sumariodesc.setText(mData.get(i).getDescricaosumario());
        sumarioViewHolder.sumariodata.setText("Data: " + mData.get(i).getDatasumario());

    }

    public void deleteItem(int position) {
        mData.remove(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class SumarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        TextView sumariodesc, sumariodata;

        public SumarioViewHolder(@NonNull final View itemView) {
            super(itemView);
            sumariodesc = itemView.findViewById(R.id.sumario_desc);
            sumariodata = itemView.findViewById(R.id.sumario_data);

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
            MenuItem doEdit = menu.add(Menu.NONE, 1, 1, "Ver detalhes");
            MenuItem doDelete = menu.add(Menu.NONE, 2, 2, "Eliminar sumario");

            doEdit.setOnMenuItemClickListener(this);
            doDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onEditClcik(position);
                            return true;
                        case 2:
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

        void onEditClcik(int positon);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
