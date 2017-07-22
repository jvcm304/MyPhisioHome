package com.myphisiohome.myphisiohome.Fragmentos;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myphisiohome.myphisiohome.Adaptadores.AdaptadorSeguimientoPaciente;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.BBDD.SeguimientoBBDD;
import com.myphisiohome.myphisiohome.Dialogos.DialogoSeguimiento;
import com.myphisiohome.myphisiohome.R;


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