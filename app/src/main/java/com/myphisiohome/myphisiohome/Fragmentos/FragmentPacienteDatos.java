package com.myphisiohome.myphisiohome.Fragmentos;

import android.content.Intent;
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

import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.R;

/**
 * Created by Vicente on 31/5/17.
 */

public class FragmentPacienteDatos extends android.support.v4.app.Fragment{

    private TextView email;
    private TextView fecNacimiento;
    private TextView sexo;
    private TextView peso;
    private TextView estatura;
    int idPaciente;
    Bundle arg=new Bundle();

    public FragmentPacienteDatos() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paciente_detail_datos, container, false);



        setToolbar(view);// Añadir action bar


        email=(TextView) view.findViewById(R.id.email);
        fecNacimiento=(TextView) view.findViewById(R.id.fecNacimiento);
        sexo=(TextView) view.findViewById(R.id.sexo);
        peso=(TextView) view.findViewById(R.id.peso);
        estatura=(TextView) view.findViewById(R.id.estatura);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapser);
        arg=getArguments();
        //String titulo=arg.getString("nombre");
        //collapser.setTitle(titulo); // Cambiar título
        email.setText(arg.getString("email"));
        fecNacimiento.setText(arg.getString("fecNacimiento"));
        sexo.setText(arg.getString("sexo"));
        estatura.setText(String.valueOf(arg.getInt("estatura")));
        peso.setText(arg.getString("peso"));
        //String image=arg.getString("imagen");
        //idPaciente=arg.getInt("idPaciente");

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


}