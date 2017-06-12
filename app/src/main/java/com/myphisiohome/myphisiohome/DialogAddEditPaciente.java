package com.myphisiohome.myphisiohome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vicente on 12/6/17.
 */

public class DialogAddEditPaciente extends DialogFragment {

    private FloatingActionButton mSaveButton;
    private TextView textFecha;
    public DialogAddEditPaciente() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.dialog_modificar_paciente, container, false);
        iniciarFecha(view);// Setear fecha inicial
        mSaveButton = (FloatingActionButton) view.findViewById(R.id.et_fab);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Actualizar----","asdsd");
            }
        });
        return view;
    }


    private void iniciarFecha(View view) {
        textFecha = (TextView) view.findViewById(R.id.et_fecNacimiento);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("E MMM d yyyy");
        textFecha.setText(format.format(c.getTime()));

        textFecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DateDialog().show(getFragmentManager(), "DatePickerInFull");
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fullscreen_dialog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;
            case R.id.action_save:
                // procesarGuardar()
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Actualiza la fecha del view {@code fecha_text}
     * @param year Nuevo Año
     * @param monthOfYear Nuevo Mes
     * @param dayOfMonth Nuevo día
     */
    public void setDateView(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("E MMM d yyyy");
        textFecha.setText(format.format(c.getTime()));
    }

}