package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


public class FragmentPlanes extends Fragment   {
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorPlanes adaptador;
    private AdapterView.OnItemClickListener onItemClickListener;

    public FragmentPlanes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planes, container, false);

        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        adaptador = new AdaptadorPlanes(getActivity());


        ((AdaptadorPlanes) adaptador).setOnItemClickListener(new AdaptadorPlanes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,int idPlan,String nombre,String categoria) {
                //Log.e("idPlan", Integer.toString(idPlan));

                ///
                Fragment fragment=new FragmentPlan();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putInt("idPlan",idPlan);
                args.putString("nombre",nombre);
                args.putString("categoria",categoria);

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



}