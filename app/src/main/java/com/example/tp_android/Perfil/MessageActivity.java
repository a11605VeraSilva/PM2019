package com.example.tp_android.Perfil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Models.Aluno;
import com.example.tp_android.Models.Message;
import com.example.tp_android.Models.Turma;
import com.example.tp_android.Perfil.Fragment.MessageAdapter;
import com.example.tp_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private TextView useremail;

    private FirebaseUser fuser;
    private DatabaseReference reference;
    private DatabaseReference refemessage;
    private String CurrentUser;
    private String idturma;


    private Intent intent;

   private String useree;
    private ImageButton btn_send;
    private EditText text_send;

    private MessageAdapter messageAdapter;
    private List<Message> mchat;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        refemessage = FirebaseDatabase.getInstance().getReference("Mensagem");

        Toolbar toolbar = findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.message_RVmessages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        useremail = findViewById(R.id.message_username);
        btn_send = findViewById(R.id.message_btnsend);
        text_send = findViewById(R.id.message_messagesend);
        intent = getIntent();
        useree = intent.getStringExtra("email");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getEmail(), useree, msg);
                    Log.d("VERRRRRRR", fuser.getEmail()+" "+useree);
                } else{
                    Toast.makeText(MessageActivity.this, "Não pode enviar mensagem vazia", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        useremail.setText(useree);
        readMessager(fuser.getEmail(), useree);
    }

    private void sendMessage (String sender, String receiver, String message){

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("visto", false);
        String emailfinal = useree.replace(".", ",");
        refemessage.child(emailfinal).push().setValue(hashMap);
    }

    private void readMessager(final String idprincipal, final String userid){
        mchat = new ArrayList<>();
        refemessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Message message = ds.getValue(Message.class);
                        if(message.getReceiver().equals(idprincipal) && message.getSender().equals(userid) ||
                                message.getReceiver().equals(userid) && message.getSender().equals(idprincipal)){

                            mchat.add(message);

                        }
                        messageAdapter = new MessageAdapter(MessageActivity.this, mchat);
                        recyclerView.setAdapter(messageAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
