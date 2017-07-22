package com.myphisiohome.myphisiohome.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.BBDD.SeguimientoBBDD;
import com.myphisiohome.myphisiohome.R;


/**
 * Created by Vicente on 30/5/17.
 */

public class AdaptadorSeguimientoPaciente extends RecyclerView.Adapter<AdaptadorSeguimientoPaciente.ViewHolder> {


    Context context;
    MyPhisioBBDDHelper pacienteBBDDHelper;
    Cursor cursor;
    int idPaciente;
    private static OnItemClickListener onItemClickListener;


    public static interface OnItemClickListener {
        public void onItemClick(View view, int position, int idPlan, String nombre, String categoria);

        void onItemClick(View view, int position, Cursor plan);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombrePlan;
        public TextView fecha;
        public RatingBar satisfaccion;
        public int satisfaccionInt;
        MyPhisioBBDDHelper pacienteBBDDHelper;
        Cursor cursor,cursor2;
        Context contexto;
        public int idPaciente2;




        public ViewHolder(View v,Context c,int idPaciente) {
            super(v);
            this.contexto=c;
            this.idPaciente2=idPaciente;
            fecha = (TextView) v.findViewById(R.id.seg_fecha);
            nombrePlan = (TextView) v.findViewById(R.id.seg_nombrePlan);
            satisfaccion = (RatingBar) v.findViewById(R.id.seg_satisfaccion);
            satisfaccion.setNumStars(5);
            Log.e("Estrellas:",satisfaccion.getNumStars()+"");
            satisfaccion.setStepSize(1);
            satisfaccion.setEnabled(false);
            pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
            cursor=pacienteBBDDHelper.getSeguimientoByPaciente(idPaciente2);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = ViewHolder.super.getAdapterPosition();
                    cursor.moveToPosition(position);
                    if(cursor.moveToPosition(position)){
                        cursor2=pacienteBBDDHelper.getSeguimientoById(cursor.getInt(cursor.getColumnIndex(SeguimientoBBDD.SeguimientoEntry.ID_SEGUIMIENTO)));
                        onItemClickListener.onItemClick(view,position,cursor2);
                    }
                }
            });

        }
    }

    public AdaptadorSeguimientoPaciente(Context c, int idPaciente) {
        this.context=c;
        this.idPaciente=idPaciente;
        this.pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
        cursor=pacienteBBDDHelper.getSeguimientoByPaciente(idPaciente);
        cursor.moveToFirst();
    }



    @Override
    public int getItemCount() {
        pacienteBBDDHelper= new MyPhisioBBDDHelper(context);
        return pacienteBBDDHelper.getSeguimientoByPaciente(idPaciente).getCount();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_seguimiento, viewGroup, false);
        return new ViewHolder(v,context,idPaciente);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(i==0){
            cursor.moveToFirst();
        }
        if(cursor.getCount()>i){
            viewHolder.nombrePlan.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
            int satis=cursor.getInt(cursor.getColumnIndex(SeguimientoBBDD.SeguimientoEntry.SATISFACCION));

            viewHolder.satisfaccion.setRating(satis);
            viewHolder.fecha.setText((cursor.getString(cursor.getColumnIndex(SeguimientoBBDD.SeguimientoEntry.FECHA))));
            cursor.moveToNext();

        }


    }

}