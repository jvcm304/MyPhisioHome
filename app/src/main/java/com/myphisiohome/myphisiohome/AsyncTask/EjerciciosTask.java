package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vicente on 25/6/17.
 */

public class EjerciciosTask extends AsyncTask<Void, Void, Integer> {

    private Integer estado;
    private String mensaje;

    //datos ejercicios planes
    private EjercicioPlanes ejercicioPlanes;
    private Ejercicio ejercicio;
    private int resul;
    private int idPlan;
    private Context context;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;

    public EjerciciosTask(int idPlan, Context context) {
        this.context=context;
        this.idPlan=idPlan;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        myPhisioBBDDHelper= new MyPhisioBBDDHelper(context);
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
                        myPhisioBBDDHelper.saveEjerciciosPlanes(ejercicioPlanes);
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
                                        //plan.addEjercicios(ejercicio);
                                        myPhisioBBDDHelper.saveEjercicio(ejercicio);
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