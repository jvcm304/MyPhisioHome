package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Clases.Plan;
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

public class AddPlanPacienteServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    PlanesUsuario planesUsuario;
    int idPaciente;
    int idPlan;
    private int estado;
    private Context context;

    public AddPlanPacienteServidor(PlanesUsuario planesUsuario, Context context,int idPaciente,int idPlan){
        this.planesUsuario=planesUsuario;
        this.context=context;
        this.idPaciente=idPaciente;
        this.idPlan=idPlan;
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
            String url_completa = URLAPI+"planesUsuario/"+idPlan+"/"+idPaciente;
            //Creando cliente http
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_completa);
            httpPost.setHeader("content-type", "application/json");

            try{
                JSONObject dato = new JSONObject();
                dato.put("idPU",planesUsuario.getIdPU());
                dato.put("tiempo", planesUsuario.getTiempo());
                dato.put("dias", planesUsuario.getDias());
                dato.put("series", planesUsuario.getSeries());


                StringEntity entity = new StringEntity(dato.toString());
                httpPost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httpPost);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                //estado = respJSON.getInt("estado");
                httpclient.getConnectionManager().shutdown();

                if (resp.getStatusLine().getStatusCode() == 201){//Status = Created
                    return ("Se ha asignado el plan al paciente correctamente");
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