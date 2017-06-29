package com.myphisiohome.myphisiohome;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoPlayEjercicios extends DialogFragment {
    private ImageView imagen;
    private TextView nombre ;
    private TextView repeticiones;
    private TextView tips;
    private TextView tipo;
    private Button boton;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private Cursor cursor;
    private int idPlan;
    private int series;
    private int esperar;
    private DownloadImageTask downloadImageTask=null;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private int idPaciente;
    private int idPU;
    private Bundle args;
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
        View view = inflater.inflate(R.layout.dialog_play, container, false);
        idPlan=getArguments().getInt("idPlan");
        series=getArguments().getInt("series");
        SharedPreferences prefs = this.getActivity().getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);
        idPaciente=prefs.getInt("PREF_PACIENTE_ID",0);
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        idPU=myPhisioBBDDHelper.getIdPU(idPaciente,idPlan);
        imagen = (ImageView) view.findViewById(R.id.play_imagen);
        boton =(Button)view.findViewById(R.id.button);
        tipo=(TextView) view.findViewById(R.id.play_tipo);
        nombre = (TextView) view.findViewById(R.id.play_nombre);
        repeticiones = (TextView) view.findViewById(R.id.play_repeticones);
        tips = (TextView) view.findViewById(R.id.play_tips);
        myPhisioBBDDHelper= new MyPhisioBBDDHelper(getActivity());
        cursor=myPhisioBBDDHelper.getEjerciciosByPlan(idPlan);
        //nombre.setText(R.string.app_name);
        //Log.e("idPlan-->",Integer.toString(idPlan));
        playPlan(cursor,series);


        //quitar dialogo
        //dismiss();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seguimiento(0);
            }
        });


        return view;
    }

    private void playPlan(Cursor cursor, int series) {
        int aux=0;
        for(int i=0;i<series;i++){
            cursor.moveToFirst();
            if(cursor.getCount()>0) {
                do {
                    nombre.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)));
                    //categoria.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)));
                    downloadImageTask=new DownloadImageTask(imagen,null,1);
                    downloadImageTask.execute(urlImagenes+cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)));
                    if (cursor.getInt(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPO)) == 1) {
                        Float segundos= Float.valueOf(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.REPETICIONES)));
                        segundos=segundos*1000;
                        cuentaAtras(Math.round(segundos),1000);//Math.round(segundos),1000);
                        tipo.setText("Segundos restantes:");
                        Log.e("Prueba-->","entra");
                        esperar=1;
                        esperar(Math.round(segundos));
                        Log.e("Prueba-->","vuelve");
                    } else {
                        tipo.setText("Repeticiones restantes:");
                        Float segundos= Float.valueOf(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.REPETICIONES)));
                        segundos=segundos*1000;
                        String segun=Float.toString(segundos);
                        cuentaAtras(Math.round(segundos),3000);
                    }
                    tips.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPS)));
                } while (cursor.moveToNext() && esperar==0);
            }

        }

    }

    public void seguimiento(int aux){

        args=new Bundle();
        args.putInt("idPU",idPU);
        args.putInt("aux",aux);
        DialogFragment dialogFragment=new DialogoSegEjercicios();
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(),"DialogoSeguimientoPlan");
        dismiss();
    }

    public void esperar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                esperar=0;
                Log.e("Prueba-->","espera");
            }
        }, milisegundos);
    }
    public void cuentaAtras(int milisegundos,int intervalo){

        new CountDownTimer(milisegundos, intervalo) {

            public void onTick(long millisUntilFinished) {
                repeticiones.setText(""+millisUntilFinished / 1000);
            }

            public void onFinish() {
                tipo.setText("");
                repeticiones.setText("Ejercicio finalizado!");

                seguimiento(1);



            }
        }.start();

    }

}