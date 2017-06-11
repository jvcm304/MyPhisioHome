package com.myphisiohome.myphisiohome;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vicente on 31/5/17.
 */

public class FragmentPlan extends Fragment {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorEjercicios adaptador;
    private TextView categoriaTV, dias, vueltas;
    private int idPlan;
    private String categoria;
    private String titulo;
    private static String diasString;
    private ImageView imagen;

    public FragmentPlan() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan_ejercicios, container, false);
        super.onCreate(savedInstanceState);
        setToolbar(view);// Añadir action bar
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        dias=(TextView) view.findViewById(R.id.dias_plan_detail);
        vueltas=(TextView) view.findViewById(R.id.vueltas);
        categoriaTV=(TextView) view.findViewById(R.id.categoria_plan);
        imagen=(ImageView)view.findViewById(R.id.image_plan);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapserEjercicios);

        this.idPlan=getArguments().getInt("idPlan");
        this.categoria=getArguments().getString("categoria");
        this.titulo=getArguments().getString("nombre");
        vueltas.setText("Series: "+Integer.toString(getArguments().getInt("vueltas")));
        this.diasString=getArguments().getString("dias");
        dias.setText(reemplazarDias(diasString));

        categoriaTV.setText(categoria);
        collapser.setTitle(titulo); // Cambiar título
        imagen.setImageDrawable(getResources().getDrawable(R.drawable.material_background));



        reciclador = (RecyclerView) view.findViewById(R.id.recicladorEjer);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        adaptador = new AdaptadorEjercicios(getActivity(),idPlan);
        reciclador.setAdapter(adaptador);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_play);
        final FragmentManager fragmentManager=getFragmentManager();
        final Bundle args = new Bundle();
        args.putInt("idPlan",idPlan);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment dialogFragment=new DialogoPlayEjercicios();
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentManager(),"DialogoPlayEjercicios");

                    }
                }
        );


        return view;
    }
    private void setToolbar(View view) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity =(AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (activity.getSupportActionBar() != null) { // Habilitar up button
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        activity.getSupportActionBar();

    }
    public static String reemplazarDias(String dias){
        String diasOK="l-m-x-j-v-s-d   ";
        String diasOK2=null;
        if(diasString.contains("L")){
            diasOK2=diasOK.replace('l','L');

        }
        if(diasString.contains("M")){
            diasOK2=diasOK.replace('m','M');

        }
        if(diasString.contains("X")){
            diasOK2=diasOK.replace('x','X');

        }
        if(diasString.contains("J")){
            diasOK2=diasOK.replace('j','J');

        }
        if(diasString.contains("V")){
            diasOK2=diasOK.replace('v','V');

        }
        if(diasString.contains("S")){
            diasOK2=diasOK.replace('s','S');

        }
        if(diasString.contains("D")){
            diasOK2=diasOK.replace('d','D');

        }

        return diasOK2;
    }
}
