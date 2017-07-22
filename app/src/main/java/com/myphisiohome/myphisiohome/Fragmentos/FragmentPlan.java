package com.myphisiohome.myphisiohome.Fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.Actividades.AdministradorActivity;
import com.myphisiohome.myphisiohome.Adaptadores.AdaptadorEjerciciosPlan;
import com.myphisiohome.myphisiohome.AsyncTask.DeleteEjercicioPlanServidor;
import com.myphisiohome.myphisiohome.AsyncTask.DeletePlanPacienteServidor;
import com.myphisiohome.myphisiohome.AsyncTask.DeletePlanServidor;
import com.myphisiohome.myphisiohome.AsyncTask.DeleteSeguimientoServidor;
import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Dialogos.DialogoAddEjercicioPlan;
import com.myphisiohome.myphisiohome.Dialogos.DialogoPlayEjercicios;
import com.myphisiohome.myphisiohome.R;

/**
 * Created by Vicente on 31/5/17.
 */

public class FragmentPlan extends Fragment {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorEjerciciosPlan adaptador;
    private TextView categoriaTV, dias, vueltas;
    private FloatingActionButton fad_add;
    private int idPlan;
    private String categoria;
    private String titulo;
    private static String diasString;
    private ImageView imagen;
    private int aux;
    private int idPaciente;
    private int idPU;
    private Float tiempo;
    private int auxEjercicio=0;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private DeletePlanPacienteServidor deletePlanPacienteServidor=null;
    private DeleteSeguimientoServidor deleteSeguimientoServidor=null;
    private DeleteEjercicioPlanServidor deleteEjercicioPlanServidor=null;
    private DeletePlanServidor deletePlanServidor=null;

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
        fad_add=(FloatingActionButton) view.findViewById(R.id.fab_add);
        this.idPlan=getArguments().getInt("idPlan");
        this.categoria=getArguments().getString("categoria");
        this.titulo=getArguments().getString("nombre");
        this.tiempo=getArguments().getFloat("tiempo");
        vueltas.setText("Series: "+Integer.toString(getArguments().getInt("vueltas")));
        this.diasString=getArguments().getString("dias")+"   ";
        dias.setText((diasString));
        aux=getArguments().getInt("aux");
        categoriaTV.setText(categoria);
        collapser.setTitle(titulo); // Cambiar título
        imagen.setImageDrawable(getResources().getDrawable(R.drawable.material_background));
        idPaciente=getArguments().getInt("idPaciente");
        idPU=getArguments().getInt("idPU");

        reciclador = (RecyclerView) view.findViewById(R.id.recicladorEjer);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);

        adaptador = new AdaptadorEjerciciosPlan(getActivity(),idPlan);
        reciclador.setAdapter(adaptador);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_play);
        final FragmentManager fragmentManager=getFragmentManager();
        final Bundle args = new Bundle();
        args.putInt("idPlan",idPlan);
        args.putInt("series", getArguments().getInt("vueltas"));
        args.putFloat("tiempo",tiempo);
        if(aux==2){
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_grey));
            fad_add.hide();
            auxEjercicio=2;
            //Borrar planUsaurio
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deletePlanPaciente();

                        }
                    }
            );

         }else if(aux==1){
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_grey));
            vueltas.setText("");
            dias.setText("");
            //borrar plan, borrar planusuario y ejerciciosPlanes
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           deletePlan();

                        }
                    }
            );

            auxEjercicio=3;
            fad_add.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addEjerciciosPlan();


                        }
                    }
            );

        }else if(aux==3){
            auxEjercicio=0;
            fad_add.setEnabled(false);
            fab.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //startActivity(new Intent(getActivity(),PlayPlanActivity.class));
                            DialogFragment dialogFragment=new DialogoPlayEjercicios();
                            dialogFragment.setArguments(args);
                            dialogFragment.show(getFragmentManager(),"DialogoPlayEjercicios");

                        }
                    }
            );
        }


        ((AdaptadorEjerciciosPlan) adaptador).setOnItemClickListener(new AdaptadorEjerciciosPlan.OnItemClickListener() {


            @Override
            public void onItemClick(AdaptadorEjerciciosPlan.ViewHolder view, int position) {

            }

            @Override
            public void onItemClick(View view, int position, Cursor ejercicio) {

                Fragment fragment=new FragmentEjercicioP();
                Bundle args = new Bundle();
                ejercicio.moveToFirst();
                args.putString("nombre",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.NOMBRE)));
                args.putString("categoria",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.CATEGORIA)));
                args.putString("descripcion",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.DESCRIPCION)));
                args.putString("tips",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.TIPS)));
                args.putString("imagen",ejercicio.getString(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.IMAGEN)));
                args.putInt("aux",auxEjercicio);
                args.putInt("idEjercicio",ejercicio.getInt(ejercicio.getColumnIndex(EjercicioBBDD.EjercicioEntry.ID_EJERCICIO)));

                fragment.setArguments(args);

                FragmentTransaction transaction=getFragmentManager().beginTransaction();

                transaction.replace(R.id.main_content, fragment);
                //transaction.addToBackStack(null);
                transaction.commit();


            }
        });


        return view;
    }

    private void deletePlan() {
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        myPhisioBBDDHelper.deleteSeguimiento2(idPlan);
        myPhisioBBDDHelper.deletePlanPacienteidPlan(idPlan);
        myPhisioBBDDHelper.deletePlanEjercicio(idPlan);
        myPhisioBBDDHelper.deletePlan(idPlan);


        deleteSeguimientoServidor= new DeleteSeguimientoServidor(0,idPlan,0,getActivity(),2);
        deleteSeguimientoServidor.execute();

        deletePlanPacienteServidor= new DeletePlanPacienteServidor(0,idPlan,0,getActivity(),2);
        deletePlanPacienteServidor.execute();

        deleteEjercicioPlanServidor=new DeleteEjercicioPlanServidor(0,idPlan,getActivity(),2);
        deleteEjercicioPlanServidor.execute();

        deletePlanServidor=new DeletePlanServidor(idPlan,getActivity());
        deletePlanServidor.execute();

        startActivity(new Intent(getActivity(),AdministradorActivity.class));



    }

    private void deletePlanPaciente() {

        //delete seguimiento

        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        myPhisioBBDDHelper.deleteSeguimiento(idPU);
        deleteSeguimientoServidor= new DeleteSeguimientoServidor(idPU,0,0,getActivity(),1);
        deleteSeguimientoServidor.execute();

        //deletePlanUsuarios
        myPhisioBBDDHelper.deletePlanPacienteidPU(idPU);
        deletePlanPacienteServidor= new DeletePlanPacienteServidor(idPU,0,0,getActivity(),1);
        deletePlanPacienteServidor.execute();
        startActivity(new Intent(getActivity(),AdministradorActivity.class));
    }

    private void addEjerciciosPlan() {
        Bundle args=new Bundle();
        args.putInt("idPlan",idPlan);
        DialogFragment dialogFragment=new DialogoAddEjercicioPlan();
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(),"DialogoAddEjercicioPlan");

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

}
