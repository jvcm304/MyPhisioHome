package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Clases.Paciente;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vicente on 27/6/17.
 */

public class DeletePacienteServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    private int idPaciente;
    private int estado;
    private Context context;

    public DeletePacienteServidor(int idPaciente, Context context){
        this.idPaciente=idPaciente;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e("resultado: :",result);
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(Void... params) {



        try {

            String url_completa = URLAPI+"pacientes/"+idPaciente;
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
                httpclient.getConnectionManager().shutdown();
                if (resp.getStatusLine().getStatusCode() == 200){//Status = OK
                    return ("Se ha eliminado el paciente correctamente");
                }
                else{
                    return ("Error al eliminar el paciente (1)");
                }

            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
                return ("Error al eliminar el paciente (2)");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("Error al eliminar el paciente (3)");
        }

    }

}