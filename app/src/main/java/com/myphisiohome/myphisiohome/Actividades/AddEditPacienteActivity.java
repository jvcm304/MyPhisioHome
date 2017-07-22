package com.myphisiohome.myphisiohome.Actividades;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import com.myphisiohome.myphisiohome.Dialogos.DialogAddEditPaciente;
import com.myphisiohome.myphisiohome.R;


public class AddEditPacienteActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_paciente);
        if (savedInstanceState == null) {
            crearFullScreenDialog();
        }
    }

    private void crearFullScreenDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAddEditPaciente newFragment = new DialogAddEditPaciente();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment, "FullScreenFragment")
                .commit();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        DialogAddEditPaciente fragment = (DialogAddEditPaciente) getSupportFragmentManager().findFragmentByTag("FullScreenFragment");
        if (fragment != null) {
            fragment.setDateView(year, monthOfYear, dayOfMonth);
        }
    }

}
