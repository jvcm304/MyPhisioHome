package com.myphisiohome.myphisiohome;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

        String titulo=prefs.getString("PREF_PACIENTE_NAME","")+" "+prefs.getString("PREF_PACIENTE_APELLIDOS","");
        collapser.setTitle(titulo); // Cambiar título
        email.setText(prefs.getString("PREF_PACIENTE_EMAIL",""));
        fecNacimiento.setText(prefs.getString("PREF_PACIENTE_NACIMIENTO",""));
        sexo.setText(prefs.getString("PREF_PACIENTE_SEXO",""));
        estatura.setText(String.valueOf(prefs.getInt("PREF_PACIENTE_ESTATURA",0)));
        peso.setText(String.valueOf((int) prefs.getFloat("PREF_PACIENTE_PESO",Float.parseFloat("0.0"))));
        String image=prefs.getString("PREF_PACIENTE_IMAGE","");
        new DownloadImageTask().execute(urlImagenes+image);


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
        Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG)
                .show();
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