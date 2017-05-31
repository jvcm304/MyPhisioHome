package com.myphisiohome.myphisiohome.API;

import android.util.Log;

import com.myphisiohome.myphisiohome.Clases.Paciente;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Vicente on 29/5/17.
 */

public class MyPhisioApi {

    private Paciente paciente;
    private String URLAPI="http://myphisio.digitalpower.es/v1/";


    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public int login(String email, String password){
        int resul;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost post =
                new HttpPost(URLAPI+"pacientes/login");

        post.setHeader("content-type", "application/json");

        try
        {
            //Construimos el objeto cliente en formato JSON
            JSONObject dato = new JSONObject();

            dato.put("email", email);
            dato.put("password", password);

            StringEntity entity = new StringEntity(dato.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONObject respJSON = new JSONObject(respStr);
            int estado = respJSON.getInt("estado");
            resul=estado;
            if(estado!=1) {
                String mensaje = respJSON.getString("mensaje");
            }
        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);
            resul = 0;
        }

        return resul;

    }




}
