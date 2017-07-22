package com.myphisiohome.myphisiohome.Fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.myphisiohome.myphisiohome.Adaptadores.AdaptadorPlanes;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.Dialogos.DialogoAddPlan;
import com.myphisiohome.myphisiohome.R;


public class FragmentPlanes extends Fragment {
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
    private int aux;
    Bundle arg = new Bundle();

    public FragmentPlanes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        final SharedPreferences prefs = this.getActivity().getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);
        arg = getArguments();
        aux = arg.getInt("aux", 0);
        if (prefs.getBoolean("PREF_ADMINISTRADOR_LOGGED", false)) {
            if (arg.getInt("aux", 0) == 1) {
                fab.show();
                refreshLayout.setEnabled(false);
            } else {
                idPaciente = arg.getInt("idPaciente", 0);
                fab.show();
                refreshLayout.setEnabled(false);
                aux = 2;
            }
        } else if (prefs.getBoolean("PREF_PACIENTE_LOGGED", false)) {
            idPaciente = prefs.getInt("PREF_PACIENTE_ID", 0);
            fab.hide();
            aux = 3;
        }
        adaptador = new AdaptadorPlanes(getActivity(), arg.getInt("aux", 0));
        pacienteBBDDHelper2 = new MyPhisioBBDDHelper(getActivity());
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addPlan(v);
                    }
                }
        );
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {

                reciclador.setEnabled(false);
                reciclador.setClickable(false);
                pacienteBBDDHelper2.refrescar();
                PlanesPacienteTask planesPacientesTask= new PlanesPacienteTask( idPaciente);
                planesPacientesTask.execute();



            }
        });*/

        ((AdaptadorPlanes) adaptador).setOnItemClickListener(new AdaptadorPlanes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int idPlan, String nombre, String categoria) {

            }

            @Override
            public void onItemClick(View view, int position, Cursor plan) {

                Fragment fragment = new FragmentPlan();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle args = new Bundle();
                plan.moveToFirst();
                args.putInt("idPlan", plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)));
                args.putString("nombre", plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.NOMBRE)));
                args.putString("categoria", plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.CATEGORIA)));
                args.putString("dias", plan.getString(plan.getColumnIndex(PlanBBDD.PlanEntry.DIAS)));
                args.putInt("vueltas", plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.SERIES)));
                args.putFloat("tiempo", plan.getFloat(plan.getColumnIndex(PlanBBDD.PlanEntry.TIEMPO)));
                args.putInt("aux", 1);
                fragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();


            }
        });
        reciclador.setAdapter(adaptador);

        return view;
    }

    private void addPlan(View v) {
        if (aux == 1) {
            Bundle args = new Bundle();
            args.putInt("aux", aux);
            DialogFragment dialogFragment = new DialogoAddPlan();
            dialogFragment.setArguments(args);
            dialogFragment.show(getFragmentManager(), "DialogoAddPlan");

        } else {
            /*Bundle args = new Bundle();
            args.putInt("aux", aux);
            args.putInt("idPaciente", idPaciente);
            DialogFragment dialogFragment = new DialogoAddPlanPaciente();
            dialogFragment.setArguments(args);
            dialogFragment.show(getFragmentManager(), "DialogoAddPlanPaciente");*/

        }


    }
}
