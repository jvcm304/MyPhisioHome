package com.myphisiohome.myphisiohome;

import android.content.Context;

import android.database.Cursor;

import android.support.v4.app.FragmentManager;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;


/**
 * Created by Vicente on 30/5/17.
 */

public class AdaptadorPlanes extends RecyclerView.Adapter<AdaptadorPlanes.ViewHolder> {


    Context context;
    MyPhisioBBDDHelper pacienteBBDDHelper;
    Cursor cursor;
    int idPlan;
    FragmentManager fragmentManager;
    private static OnItemClickListener onItemClickListener;


    public static interface OnItemClickListener {
        public void onItemClick(View view, int position,int idPlan,String nombre,String categoria);

        void onItemClick(View view, int position, Cursor plan);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView categoria;
        public ImageView imagen;
        public int idPlan;
        MyPhisioBBDDHelper pacienteBBDDHelper;
        Cursor cursor,cursor2;
        Context contexto;
        public TextView dias;




        public ViewHolder(View v,Context c) {
            super(v);
            this.contexto=c;
            nombre = (TextView) v.findViewById(R.id.nombre_plan);
            categoria = (TextView) v.findViewById(R.id.categoria_plan);
            imagen = (ImageView) v.findViewById(R.id.miniatura_plan);
            dias = (TextView) v.findViewById(R.id.dias_plan);
            pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
            cursor=pacienteBBDDHelper.getAllPlanes();


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = ViewHolder.super.getAdapterPosition();
                    cursor.moveToPosition(position);
                    if(cursor.moveToPosition(position)){

                        cursor2=pacienteBBDDHelper.getPlanById(cursor.getInt(cursor.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)));
                        onItemClickListener.onItemClick(view,position,cursor2);

                    }
                }
            });

        }
    }

    public AdaptadorPlanes(Context c) {
        this.context=c;
        this.pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
        cursor=pacienteBBDDHelper.getAllPlanes();
        cursor.moveToFirst();
    }



    @Override
    public int getItemCount() {
        pacienteBBDDHelper= new MyPhisioBBDDHelper(context);
        return pacienteBBDDHelper.getAllPlanes().getCount();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_plan, viewGroup, false);
        return new ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(i==0){
            cursor.moveToFirst();
        }
        if(cursor.getCount()>i){
            Glide.with(viewHolder.itemView.getContext())
                    .load(R.drawable.material_background)
                    .centerCrop()
                    .into(viewHolder.imagen);
            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
            viewHolder.categoria.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));
            viewHolder.dias.setText((cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.DIAS))+"  "));
            cursor.moveToNext();

        }


    }

}