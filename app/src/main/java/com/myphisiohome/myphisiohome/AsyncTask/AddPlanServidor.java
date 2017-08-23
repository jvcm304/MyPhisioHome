package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.Seguimiento;

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

public class AddPlanServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    Plan plan;
    private int estado;
    private Context context;

    public AddPlanServidor(Plan plan, Context context){
        this.plan=plan;
        this.context=context;
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
            String url_completa = URLAPI+"planes";
            //Creando cliente http
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_completa);
            httpPost.setHeader("content-type", "application/json");

            try{
                JSONObject dato = new JSONObject();
                dato.put("idPlan",plan.getIdPlan());
                dato.put("nombre", plan.getNombre());
                dato.put("descripcion", plan.getDescipcion());
                dato.put("categoria", plan.getCategoria());


                StringEntity entity = new StringEntity(dato.toString());
                httpPost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httpPost);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                //estado = respJSON.getInt("estado");
                httpclient.getConnectionManager().shutdown();
                Log.e("status" , Integer.toString(resp.getStatusLine().getStatusCode()));
                Log.e("respuesta",respStr);
                if (resp.getStatusLine().getStatusCode() == 201){//Status = Created
                    return ("Plan creado correctamente");
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