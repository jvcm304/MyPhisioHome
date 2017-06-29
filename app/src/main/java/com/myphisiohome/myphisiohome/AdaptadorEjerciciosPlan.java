package com.myphisiohome.myphisiohome;

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

/**
 * Created by Vicente on 31/5/17.
 */

public class AdaptadorEjerciciosPlan extends RecyclerView.Adapter<AdaptadorEjerciciosPlan.ViewHolder> {

    private Context context;
    private MyPhisioBBDDHelper pacienteBBDDHelper;
    private Cursor cursor;
    private int idPlan;
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
        public int idPlan2;
        MyPhisioBBDDHelper pacienteBBDDHelper;
        Cursor cursor,cursor2;

        Context c;


        public ViewHolder(View v, Context c,int idPlan) {
            super(v);
            this.c=c;
            this.idPlan2=idPlan;
            pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
            nombre = (TextView) v.findViewById(R.id.nombre_ejercicio);
            categoria = (TextView) v.findViewById(R.id.categoria_ejercicio);
            imagen2 = (ImageView) v.findViewById(R.id.image_ejercicio);
            repeticiones=(TextView) v.findViewById(R.id.repeticiones);
            cursor=pacienteBBDDHelper.getEjerciciosByPlan(idPlan2);
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

    public AdaptadorEjerciciosPlan(Context c, int idPlan) {
        this.context=c;
        this.idPlan=idPlan;
        this.pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
        cursor=pacienteBBDDHelper.getEjerciciosByPlan(idPlan);
        cursor.moveToFirst();
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }



    @Override
    public AdaptadorEjerciciosPlan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_ejercicios, viewGroup, false);
        return new AdaptadorEjerciciosPlan.ViewHolder(v,context,idPlan);
    }

    @Override
    public void onBindViewHolder(AdaptadorEjerciciosPlan.ViewHolder viewHolder, int i) {

        if(cursor.getCount()>i){

            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)));
            viewHolder.categoria.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)));
            if(cursor.getInt(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPO))==1){
                viewHolder.repeticiones.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.REPETICIONES))+" segundos  ");
            }else{
                viewHolder.repeticiones.setText(cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.REPETICIONES))+" repeticiones  ");
            }

            downloadImageTask=new DownloadImageTask(imagen2,null,1);
            downloadImageTask.execute(urlImagenes+cursor.getString(cursor.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)));
            cursor.moveToNext();
        }

    }

}