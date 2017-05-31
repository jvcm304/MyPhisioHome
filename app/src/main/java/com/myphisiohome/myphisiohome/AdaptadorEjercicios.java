package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vicente on 31/5/17.
 */

public class AdaptadorEjercicios extends RecyclerView.Adapter<AdaptadorEjercicios.ViewHolder> {

    Context context;
    MyPhisioBBDDHelper pacienteBBDDHelper;
    Cursor cursor;
    int idPlan;
    public CircleImageView imagen2;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private static AdapterView.OnItemClickListener escucha;

    public static interface OnItemClickListener {
        public void onItemClick(AdaptadorPlanes.ViewHolder view, int position);

        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView categoria;
        public CircleImageView imagen;
        public int idPlan;

        Context c;


        public ViewHolder(View v, Context c) {
            super(v);
            this.c=c;
            nombre = (TextView) v.findViewById(R.id.nombre_ejercicio);
            categoria = (TextView) v.findViewById(R.id.categoria_ejercicio);
            imagen = (CircleImageView) v.findViewById(R.id.image_ejercicio);

        }

    }

    public AdaptadorEjercicios(Context c) {
        this.context=c;
        this.pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
        cursor=pacienteBBDDHelper.getAllEjercicios();
        cursor.moveToFirst();
    }

    @Override
    public int getItemCount() {
        pacienteBBDDHelper= new MyPhisioBBDDHelper(context);
        return pacienteBBDDHelper.getAllEjercicios().getCount();
    }



    @Override
    public AdaptadorEjercicios.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_ejercicios, viewGroup, false);
        return new AdaptadorEjercicios.ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(AdaptadorEjercicios.ViewHolder viewHolder, int i) {
        if(pacienteBBDDHelper.getAllEjercicios().moveToNext() && pacienteBBDDHelper.getAllEjercicios().getCount()>0){

            /*Glide.with(viewHolder.itemView.getContext())
                    .load(new DownloadImageTask().execute(urlImagenes+cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN))))
                    .centerCrop()
                    .into(viewHolder.imagen);*/
            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)));
            viewHolder.categoria.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)));
            //viewHolder.imagenS.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)));

        }

    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {

        //final ProgressDialog progressDialog = new ProgressDialog(main.this);

        protected void onPreExecute()
        {

            //imagen2.setImageDrawable(context.getResources().getDrawable(R.drawable.cargar2));
        }

        protected Bitmap doInBackground(String... urls)
        {
            Log.d("DEBUG", "drawable");

            return downloadImage(urls[0]);

        }

        protected void onPostExecute(Bitmap imagenB)
        {

            imagen2.setImageBitmap(imagenB);

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