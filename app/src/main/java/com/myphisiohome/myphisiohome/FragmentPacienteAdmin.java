package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.AsyncTask.DeletePacienteServidor;
import com.myphisiohome.myphisiohome.AsyncTask.DownloadImageTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicente on 31/5/17.
 */

public class FragmentPacienteAdmin extends android.support.v4.app.Fragment{

    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private ImageView imagen;
    private DownloadImageTask downloadImageTask=null;
    private DeletePacienteServidor deletePacienteServidor=null;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    int idPaciente;

    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    private Bundle arg=new Bundle();
    public FragmentPacienteAdmin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.paciente_detail_admin, container, false);
        setToolbar(view);// Añadir action bar
        if (savedInstanceState == null) {
            //insertarTabs(container);
            // Setear adaptador al viewpager.
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            poblarViewPager(viewPager);
            pestanas=(TabLayout) view.findViewById(R.id.tabs);
            pestanas.setupWithViewPager(viewPager);
        }

        imagen=(ImageView) view.findViewById(R.id.image_paralax);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapser);
        arg=getArguments();
        String titulo=arg.getString("nombre");
        collapser.setTitle(titulo); // Cambiar título
        String image=arg.getString("imagen");
        idPaciente=arg.getInt("idPaciente");
        downloadImageTask=new DownloadImageTask(imagen,null,1);
        downloadImageTask.execute(urlImagenes+image);


        // Setear escucha al FAB
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSnackBar("Borrar", v);
                    }
                }
        );
        return view;
    }
    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBar = (AppBarLayout) padre.findViewById(R.id.app_bar);
        pestanas = new TabLayout(getActivity());
        if(appBar==null){
            Log.e("asdsfdfdsdsf","nulllllll");
        }
        pestanas.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        appBar.addView(pestanas);
    }
    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        //SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Fragment fragmentPacienteDatos=new FragmentPacienteDatos();
        Fragment fragmentPlanes=new FragmentPlanesPaciente();
        arg=getArguments();
        fragmentPacienteDatos.setArguments(arg);
        fragmentPlanes.setArguments(arg);
        adapter.addFragment(fragmentPacienteDatos, "Datos");
        adapter.addFragment(fragmentPlanes, "Planes");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //appBar.removeView(pestanas);
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


    private void showSnackBar(String msg,View view) {
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        myPhisioBBDDHelper.deletePaciente(idPaciente);
        deletePacienteServidor= new DeletePacienteServidor(idPaciente,getActivity());
        deletePacienteServidor.execute();
        startActivity(new Intent(getActivity(),AdministradorActivity.class));
    }

    public class AdaptadorSecciones extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentos = new ArrayList<>();
        private final List<String> titulosFragmentos = new ArrayList<>();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
    }


}