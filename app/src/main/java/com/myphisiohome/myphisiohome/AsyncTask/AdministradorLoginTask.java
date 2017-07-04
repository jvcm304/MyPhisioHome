package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AdministradorActivity;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Paciente;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.Clases.Seguimiento;
import com.myphisiohome.myphisiohome.LoginActivity;
import com.myphisiohome.myphisiohome.PrincipalActivity;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Vicente on 25/6/17.
 */

public class AdministradorLoginTask extends AsyncTask<Void, Object, Integer> {

    private String URLAPI="http://myphisio.digitalpower.es/v1/";
    private Paciente paciente;
    private Ejercicio ejercicio;
    private EjercicioPlanes ejercicioPlanes;
    private Plan plan;
    private PlanesUsuario planesUsuario;
    private Seguimiento seguimiento;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private ArrayList<Integer> idPlanes=new ArrayList<Integer>();
    GetEjerciciosTask taskEjercicios=null;


    private int estado;
    private Context context;

    public AdministradorLoginTask(Context context){
        this.context=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        myPhisioBBDDHelper=new MyPhisioBBDDHelper(context);
        int estado2=0;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getPacientes= new HttpGet(URLAPI+"pacientes/");
        getPacientes.setHeader("content-type", "application/json");

        try{
            HttpResponse resp = httpClient.execute(getPacientes);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray pacientesJSON = new JSONArray(respStr);
            for (int i=0; i<pacientesJSON.length() ;i++) {
                JSONObject pacienteJSON = pacientesJSON.getJSONObject(i);
                String nombre = pacienteJSON.getString("nombre");
                int idPaciente = pacienteJSON.getInt("idPaciente");
                String apellidos = pacienteJSON.getString("apellidos");
                String email = pacienteJSON.getString("email");
                String fecNacimiento = pacienteJSON.getString("fecNacimiento");
                String peso = pacienteJSON.getString("peso");
                String sexo = pacienteJSON.getString("sexo");
                int estatura = pacienteJSON.getInt("estatura");
                String imagen = pacienteJSON.getString("imagen");
                paciente = new Paciente(idPaciente, nombre,  email, "", imagen,
                        fecNacimiento, peso, estatura, sexo);
                myPhisioBBDDHelper.savePaciente(paciente);

                //Planes pacientes
                HttpGet getPlanesPaciente =
                        new HttpGet(URLAPI + "planesUsuario/paciente/" + Integer.toString(idPaciente));
                HttpResponse respPlanesPaciente = httpClient.execute(getPlanesPaciente);
                String respStrPlanesPaciente = EntityUtils.toString(respPlanesPaciente.getEntity());
                //
                JSONObject respJSON2 = new JSONObject(respStrPlanesPaciente);
                estado2 = respJSON2.getInt("estado");
                if (estado2 == 1) {
                    JSONArray planesPaciente = respJSON2.getJSONArray("datos");
                    for (int j = 0; j < planesPaciente.length(); j++) {
                        JSONObject planUsuario = planesPaciente.getJSONObject(j);
                        int idPU = planUsuario.getInt("idPU");
                        int idPlan = planUsuario.getInt("idPlan");
                        String dias = planUsuario.getString("dias");
                        //seguimiento
                        HttpGet getSeguimiento =
                                new HttpGet(URLAPI + "seguimiento/"+idPU);
                        HttpResponse respSeguimiento = httpClient.execute(getSeguimiento);
                        String respStrSeguimiento = EntityUtils.toString(respSeguimiento.getEntity());
                        JSONArray SeguimientoA = new JSONArray(respStrSeguimiento);
                        for (int l = 0; l < SeguimientoA.length(); l++) {
                            JSONObject seguimientoO = SeguimientoA.getJSONObject(l);
                            int idSeguimiento=seguimientoO.getInt("idSeguimiento");
                            int satisfaccion=seguimientoO.getInt("satisfaccion");
                            String comentarios=seguimientoO.getString("comentarios");
                            String fechaActual=seguimientoO.getString("fecha");
                            seguimiento=new Seguimiento(idSeguimiento,idPU,comentarios,satisfaccion,fechaActual);
                            myPhisioBBDDHelper.saveSeguimiento(seguimiento);
                        }

                        //crear Plan
                        HttpGet getPlanes =
                                new HttpGet(URLAPI + "planes/");
                        HttpResponse respPlanes = httpClient.execute(getPlanes);
                        String respStrPlanes = EntityUtils.toString(respPlanes.getEntity());
                        JSONArray planesA = new JSONArray(respStrPlanes);
                        for (int k = 0; k < planesA.length(); k++) {
                            JSONObject planes = planesA.getJSONObject(k);
                            int idPlan2 = planes.getInt("idPlan");
                            String nombrePlan = planes.getString("nombre");
                            float tiempo;
                            //if(planes.getString("tiempo").equals("null")){
                            //    tiempo = Float.valueOf("0.0");
                            //}else{

                            tiempo=Float.valueOf(planes.getString("tiempo"));
                            //}

                            int series = planes.getInt("series");
                            String descripcion = planes.getString("descripcion");
                            String categoria = planes.getString("categoria");
                            plan = new Plan(idPlan2, nombrePlan, descripcion, categoria, series, tiempo, dias);
                            idPlanes.add(idPlan2);
                            myPhisioBBDDHelper.savePlanes(plan);
                        }
                        //
                        idPaciente = planUsuario.getInt("idPaciente");
                        float tiempo = Float.valueOf(planUsuario.getString("tiempo"));
                        int series = planUsuario.getInt("series");

                        planesUsuario = new PlanesUsuario(idPU, idPlan, idPaciente, tiempo, series, dias);
                        myPhisioBBDDHelper.savePlanesUsuarios(planesUsuario);

                    }

                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return estado2;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        //LoginActivity.showProgress(false);
        for(int i=0;i<idPlanes.size();i++){
            taskEjercicios = new GetEjerciciosTask(idPlanes.get(i), context);
            taskEjercicios.execute();

        }
        SessionPrefs.get(context).logInAdministrador();
        switch (result) {
            case 1:
                startPaciente();

                break;
            default:
                showLoginError("Se ha producido un error en el servidor");
        }

    }
    private void showLoginError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }
    private void startPaciente() {
        //Toast.makeText(context, "funciona", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, AdministradorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


    }
}
