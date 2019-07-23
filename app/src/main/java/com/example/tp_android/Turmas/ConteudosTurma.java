package com.example.tp_android.Turmas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ConteudosTurma extends AppCompatActivity {

    private TextView conteudoAdicionar;
    private LinearLayout ll_conteudos;
    private EditText  txtConteudo;
    private DatabaseReference conteref;
    private String idturma;
    private String cursoturma;
    private String disciplinaTurma;
    private String acronimoturma;
    private String anoTurma;
    private String anoCorrente;
    private String semestreTurma;
    //private ArrayList<String> topicos;

    private DatabaseReference arr_conteudos;

    //Declarar botões
    private Button btnguardar;
    private Button btnPDF;
    private Button btnExcel;

    //PARA o pdf
    private static  final int STORAGE_CODE = 1000;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudos_turma);
        idturma = getIntent().getExtras().getString("idTurma");
        cursoturma = getIntent().getExtras().getString("cursoTurma");
        disciplinaTurma = getIntent().getExtras().getString("disciplinaTurma");
        acronimoturma = getIntent().getExtras().getString("acronimoTurma");
        anoTurma = getIntent().getExtras().getString("anoTurma");
        anoCorrente = getIntent().getExtras().getString("anocorrente");
        semestreTurma = getIntent().getExtras().getString("semestreTurma");

        conteudoAdicionar = findViewById(R.id.conteudo_adicionar);
        ll_conteudos = findViewById(R.id.conteudos_adicionar);
        txtConteudo = findViewById(R.id.txtConteudo);
        btnguardar = findViewById(R.id.conteudo_buttonsave);
        btnPDF = findViewById(R.id.conteudo_buttonpdf);
        btnExcel = findViewById(R.id.conteudo_buttonexcel);


        conteref = FirebaseDatabase.getInstance().getReference("Conteudos").child(idturma).push();

        //arr_conteudos = FirebaseDatabase.getInstance().getReference("Conteudos").child(idturma);
        //Log.i("Enttraaaaaaaaa", arr_conteudos+"");
        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Conteudos").child(idturma);
        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //Log.i("Enttraaaaaaaaa", snapshot+"");
                    //Log.i("Enttraaaaaaaaa", snapshot.getValue()+"");

                    String arr = snapshot.getValue().toString();
                    StringBuilder sb = new StringBuilder(arr);
                    sb.deleteCharAt(0);
                    sb.deleteCharAt(arr.length()-2);
                    String result = sb.toString();

                    String[] parts = result.split(",");
                    int pos = 0;
                    for (String part : parts) {

                        if(pos==0) {
                            txtConteudo.setText(part);
                        } else {
                            EditText tv = new EditText(ConteudosTurma.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            tv.setLayoutParams(params);
                            tv.setHint("Conteúdos");
                            tv.setText(part);
                            params.setMargins(0, 10, 0, 30);
                            tv.setBackgroundTintList(ContextCompat.getColorStateList(ConteudosTurma.this.getApplicationContext(), R.color.colorPrimary));
                            ll_conteudos.addView(tv);
                        }
                        pos++;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        conteudoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tv = new EditText(ConteudosTurma.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv.setLayoutParams(params);
                tv.setHint("Conteúdos");
                params.setMargins(0, 10, 0, 30);
                tv.setBackgroundTintList(ContextCompat.getColorStateList(ConteudosTurma.this.getApplicationContext(), R.color.colorPrimary));
                ll_conteudos.addView(tv);
            }
        });


        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Conteudos").child(idturma);
                proRef.removeValue();

                ArrayList<String> topicos = new ArrayList<>();
                topicos.clear();
                topicos.add(txtConteudo.getText().toString());
                for (int i = 1; i < ll_conteudos.getChildCount(); i++){
                    if (ll_conteudos.getChildAt(i) instanceof EditText) {
                        String t = ((EditText) ll_conteudos.getChildAt(i)).getText().toString();
                        topicos.add(t);
                        //topicos.add(txtConteudo.getText().toString());
                    }
                }
                conteref.setValue(topicos);

                startActivity(new Intent(ConteudosTurma.this, VerTurmas.class));
            }
        });

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    }else{
                        //Permissão já estava garantida
                        savepdf();
                    }
                }else{
                    //sistema inferior ao marshmallow
                    savepdf();
                }
            }
        });

        btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1000);
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1000);
                saveExcel();
            }
        });
    }

    private void saveExcel() {
        File sd = Environment.getExternalStorageDirectory();
        String FileCSv = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String csvFile = FileCSv+".xls";

        File directory = new File(sd.getAbsolutePath());

        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //Excel sheetA first sheetA
            WritableSheet sheetA = workbook.createSheet("Gestão Escolar - A", 0);

            // Linha 0
            sheetA.addCell(new Label(0, 0, "Disciplina"));
            sheetA.addCell(new Label(1, 0, "Curso"));
            sheetA.addCell(new Label(2, 0, "Ano"));
            sheetA.addCell(new Label(3, 0, "Acrónimo"));
            sheetA.addCell(new Label(4, 0, "Ano Turma"));
            sheetA.addCell(new Label(5, 0, "Semestre"));

            //Linha 1
            sheetA.addCell(new Label(0, 1, disciplinaTurma));
            sheetA.addCell(new Label(1, 1, cursoturma));
            sheetA.addCell(new Label(2, 1, anoCorrente));
            sheetA.addCell(new Label(3, 1, acronimoturma));
            sheetA.addCell(new Label(4, 1, anoTurma));
            sheetA.addCell(new Label(5, 1, semestreTurma));

            //Linha 3 para conteudos
            sheetA.addCell(new Label(0, 3, "Conteúdos"));
            sheetA.addCell(new Label(1, 3, txtConteudo.getText().toString()));
            for (int i = 0; i < ll_conteudos.getChildCount(); i++){
                if (ll_conteudos.getChildAt(i) instanceof EditText) {
                    String t = ((EditText) ll_conteudos.getChildAt(i)).getText().toString();
                    sheetA.addCell(new Label(i+1, 3, t));
                }
            }

            // close workbook
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ConteudosTurma.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    ConteudosTurma.this, permission)) {

                ActivityCompat.requestPermissions(ConteudosTurma.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(ConteudosTurma.this,
                        new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, permission + " is already granted.",
                    Toast.LENGTH_SHORT).show();
        }
    }



    private void savepdf() {
        final Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //pasta do pdf
        String mFilePath = Environment.getExternalStorageDirectory()+"/"+mFileName+".pdf";

        try{
            //Class pdf
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //abre documento para escrever
            mDoc.open();

            //Adicionar a gestão como autora
            mDoc.addAuthor("Gestão Escolar");

            mDoc.addTitle("Conteúdos");

            Font mOrderDetailsTitleFont = new Font(Font.FontFamily.HELVETICA, 36.0f, Font.BOLDITALIC, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk ( "Gestão Escolar" , mOrderDetailsTitleFont);
            Paragraph paragrafo1 = new Paragraph(mOrderDetailsTitleChunk);
            paragrafo1.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(new Paragraph(paragrafo1));
            mDoc.add(new Paragraph(" "));
            mDoc.add(new Paragraph(" "));

            Font segundafotne = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk segundafotneChunk = new Chunk ( "Ano: "+anoCorrente+"\n"+ "Curso: "+cursoturma+"\n"+"Disciplina: "+disciplinaTurma+"\n" +
                    "Acrónimo Disciplina: "+acronimoturma+"\n"+"Ano Turma: "+anoTurma+"\n"+"Semestre Turma: "+semestreTurma, segundafotne);
            Paragraph paragrafo2 = new Paragraph(segundafotneChunk);
            paragrafo2.setAlignment(Element.ALIGN_CENTER);
            mDoc.add(new Paragraph(paragrafo2));
            mDoc.add(new Paragraph(" "));

            //LINHA SEPARADORA
            LineSeparator line = new LineSeparator();
            line.setLineColor(new BaseColor(R.color.colorPrimary));
            mDoc.add(new Chunk(line));
            mDoc.add(new Paragraph(" "));

            //Listar Conteúdos
            Font conteudolist = new Font(Font.FontFamily.HELVETICA, 23.0f, Font.BOLD, BaseColor.BLACK);
            Chunk conteudol = new Chunk ( "Conteúdos lecionados nesta Disciplina:" , conteudolist);
            Paragraph conte = new Paragraph(conteudol);
            mDoc.add(new Paragraph(conte));
            mDoc.add(new Paragraph(txtConteudo.getText().toString()));

            for (int i = 1; i < ll_conteudos.getChildCount(); i++){
                if (ll_conteudos.getChildAt(i) instanceof EditText) {
                    String t = ((EditText) ll_conteudos.getChildAt(i)).getText().toString();
                    mDoc.add(new Paragraph(t));
                }
            }


            //Fechar o documento
            mDoc.close();
            Toast.makeText(ConteudosTurma.this,"PDF Criado", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(ConteudosTurma.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if(grantResults.length >0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){

                    //Permissão Garantida
                    savepdf();

                }else{

                    //Permissão recusada
                    Toast.makeText(ConteudosTurma.this, "Permissão recusada", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}
