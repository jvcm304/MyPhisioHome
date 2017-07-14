package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AsyncTask.EjerciciosTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PacienteBBDD;
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


public class FragmentPacientes extends Fragment   {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorPacientes adaptador;
    private Cursor cursorPlan;
    private AdapterView.OnItemClickListener onItemClickListener;
    private SwipeRefreshLayout refreshLayout;
    private MyPhisioBBDDHelper pacienteBBDDHelper;
    private MyPhisioBBDDHelper pacienteBBDDHelper2;

    private PlanesUsuario planesUsuario;
    private Plan plan;
    private EjercicioPlanes ejercicioPlanes;
    private Ejercicio ejercicio;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        //desabilitar refrescar
        refreshLayout.setEnabled(false);
        // Setear escucha al FAB
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showSnackBar("Configuracion", v);
                    }
                }
        );
        //final SharedPreferences prefs = this.getActivity().getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);

        adaptador = new AdaptadorPacientes(getActivity());
        pacienteBBDDHelper2=new MyPhisioBBDDHelper(getActivity());

        ((AdaptadorPacientes) adaptador).setOnItemClickListener(new AdaptadorPacientes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int idPaciente) {

            }
            @Override
            public void onItemClick(View view, int position, Cursor paciente) {
                //Log.e("idPlan", Integer.toString(idPlan));

                ///
                Fragment fragment=new FragmentPacienteAdmin();
                Bundle args = new Bundle();
                paciente.moveToFirst();
                args.putInt("idPaciente",paciente.getInt(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.ID_PACIENTE)));
                args.putString("nombre",paciente.getString(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.NOMBRE)));
                args.putString("imagen",paciente.getString(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.IAMGEN)));
                args.putString("email",paciente.getString(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.EMAIL)));
                args.putString("fecNacimiento",paciente.getString(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.FEC_NACIMIENTO)));
                args.putString("sexo",paciente.getString(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.SEXO)));
                args.putString("peso",paciente.getString(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.PESO)));
                args.putInt("estatura",paciente.getInt(paciente.getColumnIndex(PacienteBBDD.PacienteEntry.ESTATURA)));

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
    private void showSnackBar(String msg,View view) {

        startActivity(new Intent(getActivity(),AddPacienteActivity.class));
    }



}