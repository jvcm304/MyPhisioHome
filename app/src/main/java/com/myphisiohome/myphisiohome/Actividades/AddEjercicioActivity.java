package com.myphisiohome.myphisiohome.Actividades;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.myphisiohome.myphisiohome.Dialogos.DialogAddEjercicio;
import com.myphisiohome.myphisiohome.R;


public class AddEjercicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ejercicio);
        if (savedInstanceState == null) {
            crearFullScreenDialog();
        }
    }

    private void crearFullScreenDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAddEjercicio newFragment = new DialogAddEjercicio();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment, "FullScreenFragment")
                .commit();
    }


}
