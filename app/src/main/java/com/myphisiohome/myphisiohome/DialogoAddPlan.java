package com.myphisiohome.myphisiohome;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AsyncTask.AddPlanServidor;
import com.myphisiohome.myphisiohome.AsyncTask.AddSeguimientoServidor;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.Seguimiento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoAddPlan extends DialogFragment {
    private TextInputEditText nombre;
    private TextInputEditText categoria;
    private TextInputEditText descripcion;
    private Button boton;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private int aux;
    private int idPlan;

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
        View view = inflater.inflate(R.layout.dialog_add_plan, container, false);
        //aux=getArguments().getInt("aux");
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        boton =(Button)view.findViewById(R.id.button);
        nombre=(TextInputEditText) view.findViewById(R.id.edit_add_plan_nombre);
        descripcion=(TextInputEditText) view.findViewById(R.id.edit_add_plan_descripcion);
        categoria=(TextInputEditText) view.findViewById(R.id.edit_add_plan_categoria);

        Calendar calendar= new GregorianCalendar();
        String calen=Integer.toString(calendar.get(Calendar.HOUR_OF_DAY))+
                Integer.toString(calendar.get(Calendar.MINUTE))+Integer.toString(calendar.get(Calendar.SECOND));
        idPlan=Integer.valueOf(calen);
        Date date = new Date();

        //quitar dialogo
        //dismiss();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPlan();
            }
        });


        return view;
    }

    private void enviarPlan() {

        if(nombre.getText().toString().isEmpty() || categoria.getText().toString().isEmpty() ||
                descripcion.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"Los campos no pueden estar vacios",Toast.LENGTH_LONG).show();
        }else{

            Plan plan= new Plan(idPlan,nombre.getText().toString(),descripcion.getText().toString(),categoria.getText().toString());
            myPhisioBBDDHelper.savePlanes(plan);
            addPlanServidor=new AddPlanServidor(plan,getActivity());
            addPlanServidor.execute();
            dismiss();
        }

    }


}