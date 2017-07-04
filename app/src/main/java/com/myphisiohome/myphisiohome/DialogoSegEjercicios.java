package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AsyncTask.AddSeguimientoServidor;
import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
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

public class DialogoSegEjercicios extends DialogFragment {
    private TextView fecha ;
    private TextInputEditText comentarios;
    private Button boton;
    private RatingBar ratingBar;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private int aux;
    private int idPU;
    private int idSeguimiento;
    private AddSeguimientoServidor addSeguimientoServidor=null;

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
        View view = inflater.inflate(R.layout.dialog_seguimiento, container, false);
        idPU=getArguments().getInt("idPU");
        aux=getArguments().getInt("aux");
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        boton =(Button)view.findViewById(R.id.button);
        comentarios=(TextInputEditText) view.findViewById(R.id.edit_seg_comentarios);
        fecha = (TextView) view.findViewById(R.id.seg_fecha);
        ratingBar = (RatingBar) view.findViewById(R.id.seg_ratingBar);
        ratingBar.setMax(5);
        Calendar calendar= new GregorianCalendar();
        String calen=Integer.toString(calendar.get(Calendar.HOUR_OF_DAY))+
                Integer.toString(calendar.get(Calendar.MINUTE))+Integer.toString(calendar.get(Calendar.SECOND));
        idSeguimiento=Integer.valueOf(calen);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String fechaActual=dateFormat.format(date);
        fecha.setText(fechaActual);
        if(aux==0){
            ratingBar.setNumStars(aux);
            ratingBar.setEnabled(false);
        }

        //quitar dialogo
        //dismiss();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarSeguimiento();
            }
        });


        return view;
    }

    private void enviarSeguimiento() {

        if(comentarios.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"Los comentarios no pueden estar vacios",Toast.LENGTH_LONG).show();
        }else{
            String comentarios2="";
            if(aux==0){
                comentarios2+="Plan finalizado antes de acabar!! Comentarios: ";
            }
            comentarios2+=comentarios.getText().toString();
            Seguimiento seguimiento= new Seguimiento(idSeguimiento,idPU,comentarios2,
                    Math.round(ratingBar.getRating()),fecha.getText().toString());
            myPhisioBBDDHelper.saveSeguimiento(seguimiento);
            addSeguimientoServidor=new AddSeguimientoServidor(seguimiento,getActivity());
            addSeguimientoServidor.execute();
            dismiss();
        }

    }


}