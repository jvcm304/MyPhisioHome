package com.myphisiohome.myphisiohome;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PacienteBBDD;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;


public class FragmentEjercicios extends Fragment   {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorEjercicios adaptador;
    private AdapterView.OnItemClickListener onItemClickListener;
    private SwipeRefreshLayout refreshLayout;
    private MyPhisioBBDDHelper pacienteBBDDHelper;
    private MyPhisioBBDDHelper pacienteBBDDHelper2;




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

        adaptador = new AdaptadorEjercicios(getActivity());
        pacienteBBDDHelper2=new MyPhisioBBDDHelper(getActivity());
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {

                reciclador.setEnabled(false);
                reciclador.setClickable(false);
                pacienteBBDDHelper2.refrescar();
                FragmentPlanes.PlanesPacienteTask planesPacientesTask= new FragmentPlanes.PlanesPacienteTask( prefs.getInt("PREF_PACIENTE_ID",0));
                planesPacientesTask.execute();



            }
        });*/

        ((AdaptadorEjercicios) adaptador).setOnItemClickListener(new AdaptadorEjercicios.OnItemClickListener() {
            @Override
            public void onItemClick(AdaptadorEjercicios.ViewHolder view, int position) {

            }

            @Override
            public void onItemClick(View view, int position, Cursor ejercicio) {
                //Log.e("idPlan", Integer.toString(idPlan));

                ///
                Fragment fragment=new FragmentEjercicioP();
                Bundle args = new Bundle();
                ejercicio.moveToFirst();
                Log.e("IdEjercico seleccionado:",Integer.toString(ejercicio.getInt(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.ID_EJERCICIO))));

                args.putInt("idEjercicio",ejercicio.getInt(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.ID_EJERCICIO)));
                args.putString("nombre",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)));
                args.putString("imagen",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)));
                args.putString("categoria",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)));
                args.putString("descripcion",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.DESCRIPCION)));
                args.putString("tips",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPS)));


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

        startActivity(new Intent(getActivity(),AddEjercicioActivity.class));
    }


}