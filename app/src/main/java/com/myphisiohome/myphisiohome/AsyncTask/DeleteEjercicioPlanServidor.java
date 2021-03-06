package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vicente on 27/6/17.
 */

public class DeleteEjercicioPlanServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    private int idEjercicio;
    private int idPlan;
    private int estado;
    private Context context;
    private int id;//1=idEjercico, 2=idPlan

    public DeleteEjercicioPlanServidor(int idEjercicio,int idPlan, Context context,int id){
        this.idEjercicio=idEjercicio;
        this.idPlan=idPlan;
        this.context=context;
        this.id=id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(Void... params) {



        try {
            String url_completa = URLAPI;
            if(id==1){
                url_completa+="ejerciciosPlanes/idEjercicio/"+idEjercicio;
            }else if(id==2){
                url_completa+="ejerciciosPlanes/idPlan/"+idPlan;
            }
            //Creando cliente http
            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httpDelete = new HttpDelete(url_completa);
            httpDelete.setHeader("content-type", "application/json");

            try{

                HttpResponse resp;
                resp = httpclient.execute(httpDelete);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                estado = respJSON.getInt("estado");
                Log.e("ejerciciosplanes:",""+respJSON);
                httpclient.getConnectionManager().shutdown();
                if (resp.getStatusLine().getStatusCode() == 200){//Status = OK
                    return ("Se ha eliminado el ejercicioPlan correctamente");
                }
                else{
                    return ("Error al ejercicioPlan el ejercicio (1)");
                }

            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
                return ("Error al ejercicioPlan el ejercicio (2)");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("Error al ejercicioPlan el ejercicio (3)");
        }

    }

}