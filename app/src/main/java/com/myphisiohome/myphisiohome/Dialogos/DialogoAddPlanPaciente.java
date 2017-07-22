package com.myphisiohome.myphisiohome.Dialogos;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myphisiohome.myphisiohome.Adaptadores.AdaptadorPlanes;
import com.myphisiohome.myphisiohome.AsyncTask.AddPlanServidor;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.R;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoAddPlanPaciente extends DialogFragment {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorPlanes adaptador;
    private SwipeRefreshLayout refreshLayout;
    private Button boton;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private int aux;
    private int idPaciente;
    private FloatingActionButton fab;

    private AddPlanServidor addPlanServidor=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Obtener instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

        if (actionBar != null) {
            // Habilitar el Up Button
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar icono del Up Button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);
        //aux=getArguments().getInt("aux");
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        fab=(FloatingActionButton) view.findViewById(R.id.fab_add);
        fab.hide();
        refreshLayout.setEnabled(false);
        adaptador = new AdaptadorPlanes(getActivity(), 1);
        idPaciente=getArguments().getInt("idPaciente",0);

        ((AdaptadorPlanes) adaptador).setOnItemClickListener(new AdaptadorPlanes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int idPlan, String nombre, String categoria) {

            }

            @Override
            public void onItemClick(View view, int position, Cursor plan) {

                DialogFragment dialogFragment = new DialogoAddPacientePlan();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle args = new Bundle();
                plan.moveToFirst();
                args.putInt("idPlan", plan.getInt(plan.getColumnIndex(PlanBBDD.PlanEntry.ID_PLAN)));
                args.putInt("idPaciente", idPaciente);
                dialogFragment.setArguments(args);

                dialogFragment.show(getFragmentManager(), "DialogoAddPacientePlan");
                dismiss();


            }
        });
        reciclador.setAdapter(adaptador);
        return view;
    }


}