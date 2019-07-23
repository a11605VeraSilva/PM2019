package com.example.tp_android.Alunos.Contactar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tp_android.Models.Message;
import com.example.tp_android.Perfil.MessageActivity;
import com.example.tp_android.R;

import java.util.List;

public class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.ViewHolder>{

    private Context mContext;
    private List<Message> messages;

    public ProfAdapter(Context mContext, List<Message> messages) {
        this.mContext = mContext;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ProfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_contactuser, viewGroup, false);
        return new ProfAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfAdapter.ViewHolder viewHolder, int i) {
        final Message sms = messages.get(i);
        viewHolder.professorname.setText(sms.getSender());
        viewHolder.professorimage.setImageResource(R.drawable.school);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("email", sms.getSender());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView professorname;
        public ImageView professorimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            professorname = itemView.findViewById(R.id.contact_email);
            professorimage = itemView.findViewById(R.id.contact_imagemuser);
        }
    }
}
