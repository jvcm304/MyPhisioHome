package com.myphisiohome.myphisiohome;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private TextView categoriaTV;
    private int idPlan;
    private String categoria;
    private String titulo;
    private ImageView imagen;

    public FragmentPlan() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan_ejercicios, container, false);
        super.onCreate(savedInstanceState);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) { // Habilitar up button
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        categoriaTV=(TextView) view.findViewById(R.id.categoria_plan);
        imagen=(ImageView)view.findViewById(R.id.image_plan);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapserEjercicios);

        this.idPlan=getArguments().getInt("idPlan");
        this.categoria=getArguments().getString("categoria");
        this.titulo=getArguments().getString("nombre");
        Log.e("Nombre",titulo);
        categoriaTV.setText(categoria);
        collapser.setTitle(titulo); // Cambiar título
        imagen.setImageDrawable(getResources().getDrawable(R.drawable.material_background));



        reciclador = (RecyclerView) view.findViewById(R.id.recicladorEjer);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        adaptador = new AdaptadorEjercicios(getActivity());
        reciclador.setAdapter(adaptador);



        return view;
    }
}
