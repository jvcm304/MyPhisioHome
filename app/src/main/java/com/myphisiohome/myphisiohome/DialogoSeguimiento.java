package com.myphisiohome.myphisiohome;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AsyncTask.AddSeguimientoServidor;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Seguimiento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoSeguimiento extends DialogFragment {
    private TextView fecha ;
    private TextView nombre ;
    private TextView comentarios;
    private Button boton;
    private RatingBar ratingBar;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private String nombrePlan;
    private String fechaS;
    private String comentariosS;
    private int satisfaccion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Obtener instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

        if (actionBar != null) {
            // Habilitar el Up Button
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar icono del Up Button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_seguimiento_detail, container, false);
        satisfaccion=getArguments().getInt("satisfaccion");
        comentariosS=getArguments().getString("comentarios");
        fechaS=getArguments().getString("fecha");
        nombrePlan=getArguments().getString("nombrePlan");
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        boton =(Button)view.findViewById(R.id.button);
        comentarios=(TextView) view.findViewById(R.id.seg_comentarios);
        fecha = (TextView) view.findViewById(R.id.seg_fecha);
        ratingBar = (RatingBar) view.findViewById(R.id.seg_ratingBar);
        nombre= (TextView) view.findViewById(R.id.seg_nombrePlan);
        comentarios.setText(comentariosS);
        ratingBar.setEnabled(false);
        ratingBar.setRating(satisfaccion);
        fecha.setText(fechaS);
        nombre.setText(nombrePlan);


        //quitar dialogo
        //dismiss();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return view;
    }


}