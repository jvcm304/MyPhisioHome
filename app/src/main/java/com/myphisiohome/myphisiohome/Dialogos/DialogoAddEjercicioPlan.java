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

import com.myphisiohome.myphisiohome.Adaptadores.AdaptadorEjercicios;
import com.myphisiohome.myphisiohome.AsyncTask.AddPlanServidor;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.R;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoAddEjercicioPlan extends DialogFragment {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorEjercicios adaptador;
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
        adaptador = new AdaptadorEjercicios(getActivity());
        idPaciente=getArguments().getInt("idPaciente",0);

        ((AdaptadorEjercicios) adaptador).setOnItemClickListener(new AdaptadorEjercicios.OnItemClickListener() {


            @Override
            public void onItemClick(AdaptadorEjercicios.ViewHolder view, int position) {

            }

            @Override
            public void onItemClick(View view, int position, Cursor ejercicio) {
                DialogFragment dialogFragment = new DialogoAddPlanEjercicio();
                Bundle args = new Bundle();
                ejercicio.moveToFirst();
                args.putInt("idEjercicio", ejercicio.getInt(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.ID_EJERCICIO)));
                args.putInt("tipo", ejercicio.getInt(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPO)));
                args.putInt("idPlan", getArguments().getInt("idPlan"));

                dialogFragment.setArguments(args);

                dialogFragment.show(getFragmentManager(), "DialogoAddPlanEjercicio");
                dismiss();
            }

        });
        reciclador.setAdapter(adaptador);
        return view;
    }


}