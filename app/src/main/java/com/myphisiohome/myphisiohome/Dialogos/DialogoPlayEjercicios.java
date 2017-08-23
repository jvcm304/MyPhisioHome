package com.myphisiohome.myphisiohome.Dialogos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.R;

import java.util.ArrayList;

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
    private int contador=0;
    private Float tiempo;
    private SoundPool soundPool;
    int pito1;
    private int finalizar=1;
    ArrayList<Ejercicio> ejercicios2=new ArrayList<Ejercicio>();
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
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        pito1=soundPool.load(getActivity(),R.raw.pg01448,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_play, container, false);
        idPlan=getArguments().getInt("idPlan");
        series=getArguments().getInt("series");
        tiempo=getArguments().getFloat("tiempo");
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
        ArrayList<Ejercicio> ejercicios = new ArrayList<Ejercicio>();
        Ejercicio ejercicio;
        //cuentaAtras(Math.round(segundos),1000,cursor);//Math.round(segundos),1000);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            ejercicio=new Ejercicio(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPS)),
                    cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)),
                    cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)),
                    cursor.getInt(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPO)),
                    Float.valueOf(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.REPETICIONES))));
            ejercicios.add(ejercicio);
            cursor.moveToNext();
        }

        cuentaAtras(ejercicios,series,0);
    }

    public void seguimiento(int aux){
        if(aux==0){
            finalizar=0;
        }

        args=new Bundle();
        args.putInt("idPU",idPU);
        args.putInt("aux",aux);
        DialogFragment dialogFragment=new DialogoSegEjercicios();
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(),"DialogoSeguimientoPlan");
        dismiss();
    }

    public void esperar(int milisegundos) {
        if(finalizar==1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // acciones que se ejecutan tras los milisegundos
                    esperar = 0;
                    Log.e("Prueba-->", "espera");
                }
            }, milisegundos);
        }
    }


    public void cuentaAtras(ArrayList<Ejercicio> ejercicios,int series,int aux){
        if(finalizar==1) {
            if (aux == 0) {
                for (int i = 0; i < series; i++) {
                    for (int j = 0; j < ejercicios.size(); j++) {
                        ejercicios2.add(ejercicios.get(j));
                    }
                }
            }

            if (contador < ejercicios2.size()) {
                if (contador == ejercicios2.size() - 1) {
                    CountDownTimer(0, 0, ejercicios2.get(contador), null);
                } else {
                    CountDownTimer(0, 0, ejercicios2.get(contador), ejercicios2.get(contador + 1));
                }
            }
            if (contador == ejercicios2.size()) {
                if(finalizar==1){
                    seguimiento(1);
                }

            }
        }




    }


    public void CountDownTimer(int milisegundos, int intervalo, final Ejercicio ejercicio, Ejercicio ejercicio2){
        if(finalizar==1) {
            nombre.setText(ejercicio.getNombre());
            tips.setText(ejercicio.getTips());
            downloadImageTask = new DownloadImageTask(imagen, null, 1);
            downloadImageTask.execute(urlImagenes + ejercicio.getImagen());

            if (ejercicio.getTipo() == 1) {
                intervalo = 1000;
                tipo.setText("Segundos restantes:");
            } else {
                tipo.setText("Repeteciones restantes:");
                intervalo = 1000;
            }
            milisegundos = Math.round(ejercicio.getRepeticiones() * 1000);

            new CountDownTimer(milisegundos, intervalo) {

                public void onTick(long millisUntilFinished) {
                    if ((millisUntilFinished / 1000) == 2) {
                        if(finalizar==1) {
                            soundPool.play(pito1, 1, 1, 1, 0, 1);
                        }
                    }
                    repeticiones.setText("" + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    tipo.setText("");
                    repeticiones.setText("Ejercicio finalizado!");
                    contador++;
                    CountDownTimerEspera(0, tiempo);


                }
            }.start();
        }
    }
    public void CountDownTimerEspera(int milisegundos,float segundos ){
        if(finalizar==1){
            nombre.setText("Descanso");
            tips.setText("Tomese un descanso");
            imagen.setImageDrawable(getResources().getDrawable(R.drawable.descanso));


            tipo.setText("Segundos restantes:");

            milisegundos=Math.round(segundos*1000);

            new CountDownTimer(milisegundos, 1000) {

                public void onTick(long millisUntilFinished) {
                    if((millisUntilFinished / 1000)==2){
                        if(finalizar==1) {
                            soundPool.play(pito1, 1, 1, 1, 0, 1);
                        }
                    }
                    repeticiones.setText(""+millisUntilFinished / 1000);
                }

                public void onFinish() {
                    tipo.setText("");
                    repeticiones.setText("Ejercicio finalizado!");
                    cuentaAtras(null,0,1);


                }
            }.start();
        }
        }


}