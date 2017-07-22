package com.myphisiohome.myphisiohome.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.Actividades.AddEditPacienteActivity;
import com.myphisiohome.myphisiohome.R;

import java.io.File;

/**
 * Created by Vicente on 31/5/17.
 */

public class FragmentPaciente extends android.support.v4.app.Fragment{

    private TextView email;
    private TextView fecNacimiento;
    private TextView sexo;
    private TextView peso;
    private TextView estatura;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private ImageView imagen;

    public FragmentPaciente() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paciente_detail, container, false);



        setToolbar(view);// Añadir action bar


        email=(TextView) view.findViewById(R.id.email);
        fecNacimiento=(TextView) view.findViewById(R.id.fecNacimiento);
        sexo=(TextView) view.findViewById(R.id.sexo);
        peso=(TextView) view.findViewById(R.id.peso);
        estatura=(TextView) view.findViewById(R.id.estatura);
        imagen=(ImageView) view.findViewById(R.id.image_paralax);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapser);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);

        String titulo=prefs.getString("PREF_PACIENTE_NAME","");
        collapser.setTitle(titulo); // Cambiar título
        email.setText(prefs.getString("PREF_PACIENTE_EMAIL",""));
        fecNacimiento.setText(prefs.getString("PREF_PACIENTE_NACIMIENTO",""));
        sexo.setText(prefs.getString("PREF_PACIENTE_SEXO",""));
        estatura.setText(String.valueOf(prefs.getInt("PREF_PACIENTE_ESTATURA",0)));
        peso.setText(prefs.getString("PREF_PACIENTE_PESO",""));
        String image=prefs.getString("PREF_PACIENTE_IMAGE","");
        File file = new File(getContext().getFilesDir().getPath()+"/app_fotoPerfil/foto.png");
        Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.myphisiohome.myphisiohome/app_fotoPerfil/foto.png");//getContext().getPackageName()+"/app_fotoPerfil/foto.png");
        imagen.setImageBitmap(bitmap);
        //new DownloadImageTask().execute(urlImagenes+image);


        // Setear escucha al FAB
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSnackBar("Configuracion", v);
                    }
                }
        );
        return view;
    }
    private void setToolbar(View view) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity =(AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (activity.getSupportActionBar() != null) { // Habilitar up button
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        activity.getSupportActionBar();

    }


    private void showSnackBar(String msg,View view) {

        startActivity(new Intent(getActivity(),AddEditPacienteActivity.class));
    }


}