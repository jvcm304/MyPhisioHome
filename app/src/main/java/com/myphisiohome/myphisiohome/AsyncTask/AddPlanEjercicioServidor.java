package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vicente on 14/6/17.
 */

public class AddPlanEjercicioServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    EjercicioPlanes ejercicioPlanes;

    private int estado;
    private Context context;

    public AddPlanEjercicioServidor(EjercicioPlanes ejercicioPlanes, Context context){
        this.ejercicioPlanes=ejercicioPlanes;
        this.context=context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(Void... params) {



        try {
            String url_completa = URLAPI+"ejerciciosPlanes/"+ejercicioPlanes.getIdPlan()+"/"+ejercicioPlanes.getIdEjercicio();
            //Creando cliente http
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_completa);
            httpPost.setHeader("content-type", "application/json");

            try{
                JSONObject dato = new JSONObject();
                dato.put("idEP",ejercicioPlanes.getIdEP());
                dato.put("repeticiones", ejercicioPlanes.getRepeticiones());



                StringEntity entity = new StringEntity(dato.toString());
                httpPost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httpPost);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                //estado = respJSON.getInt("estado");
                httpclient.getConnectionManager().shutdown();

                if (resp.getStatusLine().getStatusCode() == 201){//Status = Created
                    return ("Se ha asignado el ejercicio al plan correctamente");
                }
                else{
                    return ("Error Plan (1)");
                }

            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
                return ("Error Plan (2)");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("Error Plan (3)");
        }

    }

}