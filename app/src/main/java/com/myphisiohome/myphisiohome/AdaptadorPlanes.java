package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Plan;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Cursor cursor;
        Context contexto;
        private EjerciciosTask taskEjercicios = null;




        public ViewHolder(View v,Context c) {
            super(v);
            this.contexto=c;
            nombre = (TextView) v.findViewById(R.id.nombre_plan);
            categoria = (TextView) v.findViewById(R.id.categoria_plan);
            imagen = (ImageView) v.findViewById(R.id.miniatura_plan);
            pacienteBBDDHelper= new MyPhisioBBDDHelper(c);
            cursor=pacienteBBDDHelper.getAllPlanes();


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = ViewHolder.super.getAdapterPosition();
                    cursor.moveToPosition(position);
                    if(cursor.moveToPosition(position)){


                        //Guardar ejerciciones planes y ejercicios
                        taskEjercicios =new EjerciciosTask(cursor.getInt(cursor.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)));
                        taskEjercicios.execute();
                        onItemClickListener.onItemClick(view,position,
                                cursor.getInt(cursor.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)),
                                cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)),
                                cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));




                    }
                }
            });

        }


        //
        //
        public class EjerciciosTask extends AsyncTask<Void, Void, Integer> {

            private Integer estado;
            private String mensaje;

            //datos ejercicios planes
            private EjercicioPlanes ejercicioPlanes;
            private Ejercicio ejercicio;
            private int resul;
            private int idPlan;

            EjerciciosTask(int idPlan) {
                this.idPlan=idPlan;
            }

            @Override
            protected Integer doInBackground(Void... params) {
                // TODO: attempt authentication against a network service.

                pacienteBBDDHelper= new MyPhisioBBDDHelper(contexto);
                //
                int resul;
                String URLAPI="http://myphisio.digitalpower.es/v1/";
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet get =
                        new HttpGet(URLAPI+"ejerciciosPlanes/"+idPlan);

                get.setHeader("content-type", "application/json");

                try
                {

                    HttpResponse resp = httpClient.execute(get);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    JSONObject respJSON = new JSONObject(respStr);
                    estado = respJSON.getInt("estado");
                    resul=estado;

                    if(estado!=1) {
                        mensaje = respJSON.getString("mensaje");


                    }else if(estado==1){
                        try{
                            JSONArray ejerciciosPlanesJSON = respJSON.getJSONArray("datos");
                            for (int i=0; i<ejerciciosPlanesJSON.length() ;i++) {
                                JSONObject ejercicioPlan =ejerciciosPlanesJSON.getJSONObject(i);
                                int idEP=ejercicioPlan.getInt("idEP");
                                int idEjercicio=ejercicioPlan.getInt("idEjercicio");
                                float repeticiones=Float.valueOf(ejercicioPlan.getString("repeticiones"));
                                ejercicioPlanes =new EjercicioPlanes(idEP,idPlan,idEjercicio,repeticiones);
                                pacienteBBDDHelper.saveEjerciciosPlanes(ejercicioPlanes);
                                //Ejercicios
                                HttpGet getEjercicios=new HttpGet(URLAPI+"ejercicios/");
                                getEjercicios.setHeader("content-type", "application/json");
                                HttpResponse resp2 = httpClient.execute(getEjercicios);
                                String respStr2 = EntityUtils.toString(resp2.getEntity());

                                if(estado!=1) {
                                    mensaje = respJSON.getString("mensaje");


                                }else if(estado==1){
                                    try{
                                        //JSONArray ejerciciosJSON = respJSON2.getJSONArray("datos");
                                        JSONArray ejerciciosJSON = new JSONArray(respStr2);
                                        for (int j=0; j<ejerciciosJSON.length() ;j++) {
                                            JSONObject ejercicioObj =ejerciciosJSON.getJSONObject(j);
                                            int idE=ejercicioObj.getInt("idEjercicio");

                                            float repe;
                                            if(idEjercicio==idE){
                                                ejercicio= new Ejercicio(idEjercicio,ejercicioObj.getString("nombre"),
                                                        ejercicioObj.getString("descripcion"),
                                                        ejercicioObj.getString("tips"),
                                                        ejercicioObj.getString("categoria"),
                                                        ejercicioObj.getString("imagen"),
                                                        ejercicioObj.getInt("tipo"),
                                                        Float.valueOf(ejercicioObj.getString("repetiones")));
                                                pacienteBBDDHelper.saveEjercicio(ejercicio);
                                            }
                                        }
                                    }catch (JSONException e){
                                        Log.e("ServicioRest","Error!", e);
                                    }
                                }

                            }
                        }catch (JSONException e){
                            Log.e("ServicioRest","Error!", e);
                        }
                    }

                }
                catch(Exception ex)
                {
                    Log.e("ServicioRest","Error!", ex);
                    resul = 0;
                }

                return resul;
            }

            @Override
            protected void onPostExecute(final Integer success) {
            }
        }
        //
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
        if(pacienteBBDDHelper.getAllPlanes().moveToNext() && pacienteBBDDHelper.getAllPlanes().getCount()>0){

            Glide.with(viewHolder.itemView.getContext())
                    .load(R.drawable.material_background)
                    .centerCrop()
                    .into(viewHolder.imagen);
            viewHolder.nombre.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
            viewHolder.categoria.setText(cursor.getString(cursor.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));

        }


    }

}