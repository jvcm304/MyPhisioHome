package com.myphisiohome.myphisiohome;

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

import com.myphisiohome.myphisiohome.AsyncTask.EjerciciosTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.BBDD.SeguimientoBBDD;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.Clases.Seguimiento;

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


public class FragmentSeguimientoPaciente extends Fragment   {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorSeguimientoPaciente adaptador;
    private FloatingActionButton fab;
    private SwipeRefreshLayout refreshLayout;

    private MyPhisioBBDDHelper pacienteBBDDHelper2;

    Bundle arg= new Bundle();
    private int aux;

    public FragmentSeguimientoPaciente() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        fab= (FloatingActionButton)view.findViewById(R.id.fab_add) ;
        refreshLayout =(SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setEnabled(false);
        fab.hide();
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        arg=getArguments();

        adaptador = new AdaptadorSeguimientoPaciente(getActivity(),arg.getInt("idPaciente"));
        pacienteBBDDHelper2=new MyPhisioBBDDHelper(getActivity());


        ((AdaptadorSeguimientoPaciente) adaptador).setOnItemClickListener(new AdaptadorSeguimientoPaciente.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, int idPlan, String nombre, String categoria) {

            }

            @Override
            public void onItemClick(View view, int position, Cursor seguimiento) {
                //Log.e("idPlan", Integer.toString(idPlan));

                ///
                DialogFragment fragment=new DialogoSeguimiento();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle args = new Bundle();
                seguimiento.moveToFirst();
                args.putInt("satisfaccion",seguimiento.getInt(seguimiento.getColumnIndex(SeguimientoBBDD.SeguimientoEntry.SATISFACCION)));
                args.putString("nombrePlan",seguimiento.getString(seguimiento.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
                args.putString("fecha",seguimiento.getString(seguimiento.getColumnIndex(SeguimientoBBDD.SeguimientoEntry.FECHA)));
                args.putString("comentarios",seguimiento.getString(seguimiento.getColumnIndex(SeguimientoBBDD.SeguimientoEntry.COMENTARIOS)));
                fragment.setArguments(args);

                fragment.show(getFragmentManager(),"DialogoPlayEjercicios");


            }
        });
        reciclador.setAdapter(adaptador);

        return view;
    }


}