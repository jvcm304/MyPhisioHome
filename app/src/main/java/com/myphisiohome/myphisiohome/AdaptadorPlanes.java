package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.database.Cursor;
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
import com.myphisiohome.myphisiohome.Clases.Plan;

/**
 * Created by Vicente on 30/5/17.
 */

public class AdaptadorPlanes extends RecyclerView.Adapter<AdaptadorPlanes.ViewHolder> {

    Context context;
    MyPhisioBBDDHelper pacienteBBDDHelper;
    Cursor cursor;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView categoria;
        public ImageView imagen;



        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre_plan);
            categoria = (TextView) v.findViewById(R.id.categoria_plan);
            imagen = (ImageView) v.findViewById(R.id.miniatura_plan);
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        if(pacienteBBDDHelper.getAllPlanes().moveToNext()){

            Glide.with(viewHolder.itemView.getContext())
                    .load(R.drawable.material_background)
                    .centerCrop()
                    .into(viewHolder.imagen);
            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
            viewHolder.categoria.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));

        }


    }


}