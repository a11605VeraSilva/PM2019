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

import com.example.tp_android.Models.Administrativo;
import com.example.tp_android.R;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private Context mContext;
    private List<Administrativo> mData;
    private OnItemClickListener mListener;

    public AdminAdapter(Context mContext, List<Administrativo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_adminlist, viewGroup, false);
        return new AdminViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder adminViewHolder, int i) {
        adminViewHolder.adminemail.setText(mData.get(i).getEmail());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class AdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView adminemail;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            adminemail = itemView.findViewById(R.id.rvadmin_listrow);

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
            MenuItem doEmail = menu.add(Menu.NONE, 1, 1, "Enviar email");
            MenuItem doDelete = menu.add(Menu.NONE, 2, 2, "Eliminar");

            doEmail.setOnMenuItemClickListener(this);
            doDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch(item.getItemId()){
                        case 1:
                            mListener.onEmail(position);
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

        void onEmail(int posiiton);

        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void removeItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }
}
