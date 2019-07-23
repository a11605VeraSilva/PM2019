package com.example.tp_android.Perfil.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Perfil.MessageActivity;
import com.example.tp_android.R;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<Aluno> mAluno;

    public UserAdapter (Context mContext, List<Aluno> mAluno){
        this.mAluno = mAluno;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_contactuser, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Aluno aluno = mAluno.get(i);
        viewHolder.alunoname.setText(aluno.getEmail());
        viewHolder.alunoimage.setImageResource(R.drawable.alunos);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("email", aluno.getEmail());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAluno.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView alunoname;
        public ImageView alunoimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alunoname = itemView.findViewById(R.id.contact_email);
            alunoimage = itemView.findViewById(R.id.contact_imagemuser);
        }
    }
}
