package com.myphisiohome.myphisiohome.Dialogos;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.AsyncTask.AddPlanPacienteServidor;
import com.myphisiohome.myphisiohome.AsyncTask.AddPlanServidor;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoAddPacientePlan extends DialogFragment {
    private TextInputEditText tiempo;
    private TextInputEditText series;
    private TextInputEditText dias;
    private Button boton;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private int aux;
    private int idPU;

    private AddPlanPacienteServidor addPlanPacienteServidor=null;

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
        View view = inflater.inflate(R.layout.dialog_add_plan_paciente, container, false);
        //aux=getArguments().getInt("aux");
        myPhisioBBDDHelper=new MyPhisioBBDDHelper(getActivity());
        boton =(Button)view.findViewById(R.id.button);
        tiempo=(TextInputEditText) view.findViewById(R.id.edit_add_planPaciente_tiempo);
        series=(TextInputEditText) view.findViewById(R.id.edit_add_planPaciente_series);
        dias=(TextInputEditText) view.findViewById(R.id.edit_add_planPaciente_dias);

        Calendar calendar= new GregorianCalendar();
        String calen=Integer.toString(calendar.get(Calendar.HOUR_OF_DAY))+
                Integer.toString(calendar.get(Calendar.MINUTE))+Integer.toString(calendar.get(Calendar.SECOND));
        idPU=Integer.valueOf(calen);
        Date date = new Date();

        //quitar dialogo
        //dismiss();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asiganarPlanPaciente();
            }
        });


        return view;
    }

    private void asiganarPlanPaciente() {

        if(tiempo.getText().toString().isEmpty() || series.getText().toString().isEmpty() ||
                dias.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"Los campos no pueden estar vacios",Toast.LENGTH_LONG).show();
        }else{

            PlanesUsuario planesUsuario= new PlanesUsuario(idPU,getArguments().getInt("idPlan"),
                    getArguments().getInt("idPaciente"),Float.valueOf(tiempo.getText().toString()),
                    Integer.valueOf(series.getText().toString()),dias.getText().toString());
            myPhisioBBDDHelper.savePlanesUsuarios(planesUsuario);
            addPlanPacienteServidor=new AddPlanPacienteServidor(planesUsuario,getActivity(),
                    getArguments().getInt("idPaciente"),getArguments().getInt("idPlan"));
            addPlanPacienteServidor.execute();
            dismiss();
        }

    }


}