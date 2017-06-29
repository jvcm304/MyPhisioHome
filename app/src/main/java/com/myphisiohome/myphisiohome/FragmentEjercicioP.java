package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.AsyncTask.DeleteEjercicioServidor;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vicente on 31/5/17.
 */

public class FragmentEjercicioP extends android.support.v4.app.Fragment{

    private TextView categoria;
    private TextView descripcion;
    private TextView tips;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private ImageView imagen;
    private FloatingActionButton fab;
    private int idEjercicio;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    DeleteEjercicioServidor deleteEjercicioServidor;
    int aux;

    public FragmentEjercicioP() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ejercicio_detail_paciente, container, false);



        setToolbar(view);// Añadir action bar


        categoria=(TextView) view.findViewById(R.id.categoria_ejercicioP_detail);
        descripcion=(TextView) view.findViewById(R.id.descripcion_ejercicioP_detail);
        tips=(TextView) view.findViewById(R.id.tips_ejercicioP_detail);
        imagen=(ImageView) view.findViewById(R.id.imagen_ejercicioP_detail);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapser_ejercicoP);
        collapser.setTitle(getArguments().getString("nombre"));
        categoria.setText(getArguments().getString("categoria"));
        String image=getArguments().getString("imagen");
        tips.setText(getArguments().getString("tips"));
        descripcion.setText(getArguments().getString("descripcion"));
        idEjercicio=getArguments().getInt("idEjercicio");
        aux=getArguments().getInt("aux");
        if (SessionPrefs.get(getActivity()).isLoggedIn(getActivity())) {
           fab.hide();

        }
        // Redirección al Login
        if (SessionPrefs.get(getActivity()).isLoggedInAdministrador(getActivity())) {
            if(aux==2){
                fab.hide();
            }else if(aux==3){
                fab.show();
                //borrar ejercicioPlan
            }else if(aux==1){
                fab.show();
                //borrar ejercicio
                fab.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showSnackBar("Borrar", v);
                            }
                        }
                );
            }

        }


        new DownloadImageTask().execute(urlImagenes+image);


        // Setear escucha al FAB

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


        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        myPhisioBBDDHelper.deleteEjercicio(idEjercicio);
        deleteEjercicioServidor= new DeleteEjercicioServidor(idEjercicio,getActivity());
        deleteEjercicioServidor.execute();
        startActivity(new Intent(getActivity(),AdministradorActivity.class));
        /*Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG)
                .show();*/
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {

        //final ProgressDialog progressDialog = new ProgressDialog(main.this);

        protected void onPreExecute()
        {
            imagen.setImageDrawable(getResources().getDrawable(R.drawable.material_background3));
        }

        protected Bitmap doInBackground(String... urls)
        {
            Log.d("DEBUG", "drawable");

            return downloadImage(urls[0]);

        }

        protected void onPostExecute(Bitmap imagenBitmap)
        {
            Drawable draImage=new BitmapDrawable(imagenBitmap);
            imagen.setImageDrawable(draImage);

        }

        /**
         * Devuelve una imagen desde una URL
         *
         * @return Una imagen
         */
        private Bitmap downloadImage(String imageUrlS)
        {
            try
            {
                URL imageUrl = new URL(imageUrlS);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();

                return BitmapFactory.decodeStream(conn.getInputStream());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }


}