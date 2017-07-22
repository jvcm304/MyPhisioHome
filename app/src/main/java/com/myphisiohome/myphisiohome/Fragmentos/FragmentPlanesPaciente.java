package com.myphisiohome.myphisiohome.Fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.myphisiohome.myphisiohome.Adaptadores.AdaptadorPlanesPaciente;
import com.myphisiohome.myphisiohome.AsyncTask.EjerciciosTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.BBDD.PlanesUsuarioBBDD;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.Dialogos.DialogoAddPlanPaciente;
import com.myphisiohome.myphisiohome.Actividades.LoginActivity;
import com.myphisiohome.myphisiohome.R;

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


public class FragmentPlanesPaciente extends Fragment   {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorPlanesPaciente adaptador;
    private Cursor cursorPlan;
    private AdapterView.OnItemClickListener onItemClickListener;
    private SwipeRefreshLayout refreshLayout;
    private MyPhisioBBDDHelper pacienteBBDDHelper;
    private MyPhisioBBDDHelper pacienteBBDDHelper2;
    private FloatingActionButton fab;

    private PlanesUsuario planesUsuario;
    private Plan plan;
    private EjercicioPlanes ejercicioPlanes;
    private Ejercicio ejercicio;
    private int idPaciente;
    Bundle arg= new Bundle();
    private int aux;
    private int idPU;

    public FragmentPlanesPaciente() {
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
            aux=2;
            if(idPaciente==0){
                aux=1;
            }
            fab.show();
            refreshLayout.setEnabled(false);
        }else if(prefs.getBoolean("PREF_PACIENTE_LOGGED",false)){
            idPaciente=prefs.getInt("PREF_PACIENTE_ID",0);
            aux=3;
            fab.hide();
        }
        adaptador = new AdaptadorPlanesPaciente(getActivity(),idPaciente);
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
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args=new Bundle();
                        args.putInt("idPaciente",idPaciente);
                        DialogFragment dialogFragment=new DialogoAddPlanPaciente();
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentManager(),"DialogoAddPlanPaciente");

                    }
                }
        );


        ((AdaptadorPlanesPaciente) adaptador).setOnItemClickListener(new AdaptadorPlanesPaciente.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int idPlan, String nombre, String categoria) {

            }
            @Override
            public void onItemClick(View view, int position, Cursor plan) {
                //Log.e("idPlan", Integer.toString(idPlan));

                ///
                Fragment fragment=new FragmentPlan();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle args = new Bundle();
                plan.moveToFirst();
                String dias="";
                Float tiempo;
                tiempo=plan.getFloat(plan.getColumnIndex(PlanBBDD.PlanEntry.TIEMPO));
                int vueltas=0;

                if(aux==2){
                    //Desde admin paciente
                    dias=plan.getString(plan.getColumnIndex(PlanesUsuarioBBDD.PlanesUsuarioEntry.DIAS));
                    tiempo=plan.getFloat(plan.getColumnIndex(PlanesUsuarioBBDD.PlanesUsuarioEntry.TIEMPO));
                    vueltas=plan.getInt(plan.getColumnIndex(PlanesUsuarioBBDD.PlanesUsuarioEntry.SERIES));
                    idPU=plan.getInt(plan.getColumnIndex(PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PU));
                    args.putInt("idPU",idPU);

                }else if(aux==3){
                    //Desde paciente
                    dias=plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.DIAS));
                    vueltas=plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.SERIES));
                    tiempo=plan.getFloat(plan.getColumnIndex(PlanBBDD.PlanEntry.TIEMPO));
                }
                args.putInt("idPlan",plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)));
                args.putString("nombre",plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
                args.putString("categoria",plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));
                args.putString("dias",dias);
                args.putFloat("tiempo",tiempo);
                args.putInt("vueltas",vueltas);
                args.putInt("aux",aux);

                args.putInt("idPaciente",idPaciente);


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
                            pacienteBBDDHelper.savePlanes(plan);
                        }
                        //
                        int idPaciente=planUsuario.getInt("idPaciente");
                        float tiempo=Float.valueOf(planUsuario.getString("tiempo"));
                        int series=planUsuario.getInt("series");

                        planesUsuario=new PlanesUsuario(idPU,idPlan,idPaciente,tiempo,series,dias);
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
            adaptador = new AdaptadorPlanesPaciente(getActivity(),idPaciente);
            reciclador.setAdapter(adaptador);

        }
    }



}