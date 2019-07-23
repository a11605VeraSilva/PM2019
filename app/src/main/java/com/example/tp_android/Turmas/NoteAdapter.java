package com.example.tp_android.Turmas;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Email.SendMail;
import com.example.tp_android.Models.Note;
import com.example.tp_android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

        private static final String TAG = "PostListAdapter";
        private static final int NUM_GRID_COLUMNS = 3;

        private List<Note> mNotes;
        private Context mContext;

    //popup descricao nota
    private Dialog popEditarNota;
    private TextView descricao;
    private Button gravanota;

        public NoteAdapter(Context context, List<Note> notes) {
            mNotes = notes;
            mContext = context;
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_note_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textContent.setText(mNotes.get(position).getDescricao());
        holder.textTitle.setText(mNotes.get(position).getTitulo());
        holder.textHora.setText(mNotes.get(position).getTempo());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textContent, textTitle, textHora;

            public ViewHolder(final View itemView) {
                super(itemView);
                textContent = itemView.findViewById(R.id.cardnote_descricao);
                textTitle = itemView.findViewById(R.id.cardnote_titulo);
                textHora = itemView.findViewById(R.id.cardnote_hora);



                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popEditarNota = new Dialog(mContext);
                        popEditarNota.setContentView(R.layout.viewsinglenote);
                        popEditarNota.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        popEditarNota.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                        popEditarNota.getWindow().getAttributes().gravity = Gravity.CENTER;

                        descricao = popEditarNota.findViewById(R.id.viewnote_descricao);
                        descricao.setText(mNotes.get(getAdapterPosition()).getDescricao());
                        popEditarNota.show();

                    }
                });
            }
        }
}
