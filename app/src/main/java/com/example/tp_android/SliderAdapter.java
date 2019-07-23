package com.example.tp_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tp_android.Horarios.HorarioAulas;
import com.example.tp_android.Mural.MuralTurmas;
import com.example.tp_android.Turmas.MenuTurmas;
import com.google.firebase.auth.FirebaseAuth;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.alunos,
            R.drawable.turmas,
            R.drawable.horario,
            R.drawable.atividades
    };

    public String[] slide_headings = {
            "Perfil",
            "Disciplinas",
            "Horários",
            "Terminar Sessão"
    };

    /*public String[] slide_descs = {
            "bla bla bla",
            "bla bla bla",
            "bla bla bla"
    };*/

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_text);

        slideImage.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent imural = new Intent(context, MuralTurmas.class);
                    context.startActivity(imural);
                } else if(position == 1){
                    Intent iturmas = new Intent(context, MenuTurmas.class);
                    context.startActivity(iturmas);
                }else if(position == 2){
                    Intent iHorario = new Intent(context, HorarioAulas.class);
                    context.startActivity(iHorario);
                }else if(position == 3){
                    FirebaseAuth.getInstance().signOut();
                    Intent loginActivity = new Intent(context, MainActivity.class);
                    context.startActivity(loginActivity);
                    ((Activity) context).finish(); //Para quando clicarmos no botão voltar atrás ele não entre novamente na aplicação
                }
            }
        });


        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
