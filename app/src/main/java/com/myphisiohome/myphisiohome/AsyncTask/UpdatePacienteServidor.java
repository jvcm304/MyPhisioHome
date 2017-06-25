package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Clases.Paciente;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vicente on 14/6/17.
 */

public class UpdatePacienteServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    Paciente paciente;
    private int estado;
    private Context context;

    public UpdatePacienteServidor(Paciente paciente, Context context){
        this.paciente=paciente;
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
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(Void... params) {



        try {

            String url_completa = URLAPI+"pacientes/"+paciente.getIdPaciente();
            Log.e("url:",URLAPI+"pacientes/"+paciente.getIdPaciente());
            //Creando cliente http
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url_completa);
            httpPut.setHeader("content-type", "application/json");

            try{
                JSONObject dato = new JSONObject();

                dato.put("nombre", paciente.getNombre());
                dato.put("email", paciente.getEmail());
                dato.put("imagen", paciente.getImagen());
                dato.put("peso", paciente.getPeso());
                dato.put("estatura", paciente.getEstatura());
                dato.put("fecNacimiento", paciente.getFecNacimiento());
                dato.put("sexo", paciente.getSexo());
                StringEntity entity = new StringEntity(dato.toString());
                httpPut.setEntity(entity);
                HttpResponse resp = httpclient.execute(httpPut);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                estado = respJSON.getInt("estado");
                httpclient.getConnectionManager().shutdown();
                if (resp.getStatusLine().getStatusCode() == 200){//Status = OK
                    return ("Se ha modificado el paciente correctamente");
                }
                else{
                    return ("Error al modificar el paciente1");
                }

            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
                return ("Error al modificar el paciente2");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("Error al modificar el paciente3");
        }

    }

}