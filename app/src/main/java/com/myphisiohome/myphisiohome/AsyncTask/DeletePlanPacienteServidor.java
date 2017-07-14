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

public class DeletePlanPacienteServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    private int idPU;
    private int idPlan;
    private int idPaciente;
    private int id;;
    private Context context;
    private int estado;

    public DeletePlanPacienteServidor(int idPU,int idPlan,int idPaciente, Context context,int id){

        this.idPU=idPU;
        this.idPlan=idPlan;
        this.idPaciente=idPaciente;
        this.id=id;
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
            String url_completa = URLAPI;
            if(id==1){
                url_completa+="planesUsuario/idPU/"+idPU;
            }else if(id==2){
                url_completa+="planesUsuario/idPlan/"+idPlan;
            }else if(id==3) {
                url_completa += "planesUsuario/idPaciente/" + idPaciente;
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
                Log.e("planpaciente:",""+respJSON);
                httpclient.getConnectionManager().shutdown();
                if (resp.getStatusLine().getStatusCode() == 200){//Status = OK
                    return ("Se ha eliminado el planUsuario correctamente");
                }
                else{
                    return ("Error al planUsuario el ejercicio (1)");
                }

            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
                return ("Error al planUsuario el ejercicio (2)");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("Error al planUsuario el ejercicio (3)");
        }

    }

}