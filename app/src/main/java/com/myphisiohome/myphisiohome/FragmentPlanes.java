package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.myphisiohome.myphisiohome.AsyncTask.EjerciciosTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;

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


public class FragmentPlanes extends Fragment   {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorPlanes adaptador;
    private AdapterView.OnItemClickListener onItemClickListener;
    private SwipeRefreshLayout refreshLayout;
    private MyPhisioBBDDHelper pacienteBBDDHelper;
    private MyPhisioBBDDHelper pacienteBBDDHelper2;
    private FloatingActionButton fab;

    private PlanesUsuario planesUsuario;
    private Plan plan;
    private int idPaciente;
    Bundle arg= new Bundle();

    public FragmentPlanes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_add);
        final SharedPreferences prefs = this.getActivity().getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);
        arg=getArguments();
        if(prefs.getBoolean("PREF_ADMINISTRADOR_LOGGED",false)){
            idPaciente=arg.getInt("idPaciente",0);
            fab.hide();
            refreshLayout.setEnabled(false);
        }else if(prefs.getBoolean("PREF_PACIENTE_LOGGED",false)){
            idPaciente=prefs.getInt("PREF_PACIENTE_ID",0);
            fab.hide();
        }
        adaptador = new AdaptadorPlanes(getActivity());
        pacienteBBDDHelper2=new MyPhisioBBDDHelper(getActivity());
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {

                reciclador.setEnabled(false);
                reciclador.setClickable(false);
                pacienteBBDDHelper2.refrescar();
                PlanesPacienteTask planesPacientesTask= new PlanesPacienteTask( idPaciente);
                planesPacientesTask.execute();



            }
        });

        ((AdaptadorPlanes) adaptador).setOnItemClickListener(new AdaptadorPlanes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int idPlan, String nombre, String categoria) {

            }
            @Override
            public void onItemClick(View view, int position, Cursor plan) {

                Fragment fragment=new FragmentPlan();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle args = new Bundle();
                plan.moveToFirst();
                args.putInt("idPlan",plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)));
                args.putString("nombre",plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
                args.putString("categoria",plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));
                args.putString("dias",plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.DIAS)));
                args.putInt("vueltas",plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.SERIES)));
                args.putFloat("tiempo",plan.getFloat(plan.getColumnIndex(PlanBBDD.PlanEntry.TIEMPO)));
                args.putInt("aux", 1);
                fragment.setArguments(args);

                FragmentTransaction transaction=getFragmentManager().beginTransaction();

                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        reciclador.setAdapter(adaptador);

        return view;
    }


    public class PlanesPacienteTask extends AsyncTask<Void, Void, Integer> {

        private Integer estado;
        private String mensaje;

        //datos ejercicios planes
        //private EjercicioPlanes ejercicioPlanes;
        //private Ejercicio ejercicio;
        private int resul;
        private int idPaciente;
        private ArrayList<Integer> idPlanes=new ArrayList<Integer>();

        PlanesPacienteTask(int idPaciente) {
            this.idPaciente=idPaciente;
        }
        @Override
        protected void onPreExecute(){


        }
        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            pacienteBBDDHelper= new MyPhisioBBDDHelper(getActivity());
            //
            int resul=0;
            Log.e("ID paciente desde planes: ",Integer.toString(idPaciente));
            String URLAPI="http://myphisio.digitalpower.es/v1/";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getPlanesPaciente =
                    new HttpGet(URLAPI+"planesUsuario/paciente/"+Integer.toString(idPaciente));
            //Planes



            try
            {
                HttpResponse respPlanesPaciente = httpClient.execute(getPlanesPaciente);
                String respStrPlanesPaciente = EntityUtils.toString(respPlanesPaciente.getEntity());
                //
                JSONObject respJSON2 = new JSONObject(respStrPlanesPaciente);
                int estado2 = respJSON2.getInt("estado");
                resul=estado2;
                if(estado2==1){
                    JSONArray planesPaciente = respJSON2.getJSONArray("datos");
                    for (int i=0; i<planesPaciente.length() ;i++){
                        JSONObject planUsuario= planesPaciente.getJSONObject(i);
                        int idPU=planUsuario.getInt("idPU");
                        int idPlan=planUsuario.getInt("idPlan");
                        String dias=planUsuario.getString("dias");
                        //crear Plan
                        HttpGet getPlanes =
                                new HttpGet(URLAPI+"planes/"+idPlan);
                        HttpResponse respPlanes = httpClient.execute(getPlanes);
                        String respStrPlanes = EntityUtils.toString(respPlanes.getEntity());
                        JSONArray planesA = new JSONArray(respStrPlanes);
                        for (int j=0; j<planesA.length() ;j++){
                            JSONObject planes= planesA.getJSONObject(j);
                            int idPlan2=planes.getInt("idPlan");
                            String nombre=planes.getString("nombre");
                            float tiempo=Float.valueOf(planes.getString("tiempo"));
                            int series=planes.getInt("series");
                            String descripcion=planes.getString("descripcion");
                            String categoria=planes.getString("categoria");
                            plan=new Plan(idPlan2,nombre,descripcion,categoria,series,tiempo,dias);
                            idPlanes.add(idPlan2);
                            Log.e("ID PLAN desde planes: ",Integer.toString(idPlan2));
                            pacienteBBDDHelper.savePlanes(plan);
                        }
                        //
                        int idPaciente=planUsuario.getInt("idPaciente");
                        float tiempo=Float.valueOf(planUsuario.getString("tiempo"));
                        int series=planUsuario.getInt("series");

                        planesUsuario=new PlanesUsuario(idPU,idPaciente,idPlan,tiempo,series,dias);
                        pacienteBBDDHelper.savePlanesUsuarios(planesUsuario);

                    }
                }
                //
            }catch (JSONException e){
                Log.e("ServicioRest","Error!", e);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resul;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            LoginActivity login = new LoginActivity();

            for(int i=0;i<idPlanes.size();i++){
                EjerciciosTask ejerciciosTask = login.getEjerciciosTask(idPlanes.get(i),getActivity());
                ejerciciosTask.execute();

            }
            // Parar la animación del indicador
            refreshLayout.setRefreshing(false);
            reciclador.setEnabled(true);
            reciclador.setClickable(true);
            adaptador = new AdaptadorPlanes(getActivity());
            reciclador.setAdapter(adaptador);

        }
    }



}