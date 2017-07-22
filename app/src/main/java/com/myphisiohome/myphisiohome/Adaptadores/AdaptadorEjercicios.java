package com.myphisiohome.myphisiohome.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.R;

/**
 * Created by Vicente on 31/5/17.
 */

public class AdaptadorEjercicios extends RecyclerView.Adapter<AdaptadorEjercicios.ViewHolder> {

    private Context context;
    private MyPhisioBBDDHelper pacienteBBDDHelper;
    private Cursor cursor;
    public static ImageView imagen2;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    DownloadImageTask downloadImageTask=null;
    private static OnItemClickListener onItemClickListener;

    public static interface OnItemClickListener {
        void onItemClick(ViewHolder view, int position);

        void onItemClick(View view, int position, Cursor ejercicio);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView categoria, repeticiones;
        public ImageView imagen;
        public int idEjercicio2;
        MyPhisioBBDDHelper pacienteBBDDHelper;
        Cursor cursor,cursor2;

        Context c;


        public ViewHolder(View v, Context c) {
            super(v);
            this.c=c;
            pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
            nombre = (TextView) v.findViewById(R.id.nombre_ejercicio);
            categoria = (TextView) v.findViewById(R.id.categoria_ejercicio);
            imagen2 = (ImageView) v.findViewById(R.id.image_ejercicio);
            repeticiones=(TextView) v.findViewById(R.id.repeticiones);
            cursor=pacienteBBDDHelper.getAllEjercicios();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = ViewHolder.super.getAdapterPosition();
                    cursor.moveToPosition(position);
                    if(cursor.moveToPosition(position)){

                        cursor2=pacienteBBDDHelper.getEjercicioById(cursor.getInt(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.ID_EJERCICIO)));
                        onItemClickListener.onItemClick(view,position,cursor2);

                    }
                }
            });

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
        return cursor.getCount();
    }



    @Override
    public AdaptadorEjercicios.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_ejercicios, viewGroup, false);
        return new AdaptadorEjercicios.ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(AdaptadorEjercicios.ViewHolder viewHolder, int i) {

        if(cursor.getCount()>i){

            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)));
            viewHolder.categoria.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)));

            downloadImageTask=new DownloadImageTask(imagen2,null,1);
            downloadImageTask.execute(urlImagenes+cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)));
            cursor.moveToNext();
        }

    }

}