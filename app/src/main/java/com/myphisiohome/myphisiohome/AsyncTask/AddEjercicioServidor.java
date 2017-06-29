package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.Paciente;

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

public class AddEjercicioServidor extends AsyncTask<Void, Object, String> {

    String URLAPI="http://myphisio.digitalpower.es/v1/";
    Ejercicio ejercicio;
    private int estado;
    private Context context;

    public AddEjercicioServidor(Ejercicio ejercicio, Context context){
        this.ejercicio=ejercicio;
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

            String url_completa = URLAPI+"ejercicios";
            //Creando cliente http
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_completa);
            httpPost.setHeader("content-type", "application/json");

            try{
                JSONObject dato = new JSONObject();
                dato.put("idEjercicio",ejercicio.getIdEjercicio());
                dato.put("nombre", ejercicio.getNombre());
                dato.put("categoria", ejercicio.getCategoria());
                dato.put("descripcion", ejercicio.getDescripcion());
                dato.put("imagen", ejercicio.getImagen());
                dato.put("tips", ejercicio.getTips());
                dato.put("tipo", ejercicio.getTipo());

                StringEntity entity = new StringEntity(dato.toString());
                httpPost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httpPost);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                estado = respJSON.getInt("estado");
                httpclient.getConnectionManager().shutdown();
                if (resp.getStatusLine().getStatusCode() == 201){//Status = Created
                    return ("Se ha creado el ejercicio correctamente");
                }
                else{
                    return ("Error al crear el ejercicio (1)");
                }

            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
                return ("Error al crear el ejercicio (2)");

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ("Error al crear el ejercicio (3)");
        }

    }

}