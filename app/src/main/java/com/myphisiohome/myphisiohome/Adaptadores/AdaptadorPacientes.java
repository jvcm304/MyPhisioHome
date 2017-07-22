package com.myphisiohome.myphisiohome.Adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PacienteBBDD;
import com.myphisiohome.myphisiohome.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Vicente on 30/5/17.
 */

public class AdaptadorPacientes extends RecyclerView.Adapter<AdaptadorPacientes.ViewHolder> {


    Context context;
    MyPhisioBBDDHelper myPhisioBBDDHelper;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    DownloadImageTask downloadImageTask=null;

    Cursor cursor;
    private static OnItemClickListener onItemClickListener;


    public static interface OnItemClickListener {
        public void onItemClick(View view, int position, int idPaciente);

        void onItemClick(View view, int position, Cursor plan);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView email;
        public CircleImageView imagen;
        public int idPlan;
        MyPhisioBBDDHelper myPhisioBBDDHelper;
        Cursor cursor,cursor2;
        Context contexto;
        public TextView dias;




        public ViewHolder(View v,Context c) {
            super(v);
            this.contexto=c;
            nombre = (TextView) v.findViewById(R.id.tv_name);
            email = (TextView) v.findViewById(R.id.tv_email);
            imagen = (CircleImageView) v.findViewById(R.id.iv_avatar);
            myPhisioBBDDHelper= new MyPhisioBBDDHelper(c);
            cursor=myPhisioBBDDHelper.getAllPaciente();


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = ViewHolder.super.getAdapterPosition();
                    cursor.moveToPosition(position);
                    if(cursor.moveToPosition(position)){

                        cursor2=myPhisioBBDDHelper.getPacienteById(cursor.getInt(cursor.getColumnIndex(PacienteBBDD.PacienteEntry.ID_PACIENTE)));
                        onItemClickListener.onItemClick(view,position,cursor2);

                    }
                }
            });

        }
    }

    public AdaptadorPacientes(Context c) {
        this.context=c;
        this.myPhisioBBDDHelper= new MyPhisioBBDDHelper(c);
        cursor=myPhisioBBDDHelper.getAllPaciente();
        cursor.moveToFirst();
    }



    @Override
    public int getItemCount() {
        myPhisioBBDDHelper= new MyPhisioBBDDHelper(context);
        return myPhisioBBDDHelper.getAllPaciente().getCount();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_paciente2, viewGroup, false);
        return new ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(i==0){
            cursor.moveToFirst();
        }
        if(cursor.getCount()>i){
            /*Glide.with(viewHolder.itemView.getContext())
                    .load(R.drawable.cargar2)
                    .centerCrop()
                    .into(viewHolder.imagen);*/
            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(PacienteBBDD.PacienteEntry.NOMBRE)));
            viewHolder.email.setText(cursor.getString(cursor.getColumnIndex(PacienteBBDD.PacienteEntry.EMAIL)));
            downloadImageTask= new DownloadImageTask(null,viewHolder.imagen,2);
            downloadImageTask.execute(urlImagenes+cursor.getString(cursor.getColumnIndex(PacienteBBDD.PacienteEntry.IAMGEN)));
            cursor.moveToNext();

        }


    }

}