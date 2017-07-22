package com.myphisiohome.myphisiohome.Actividades;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.myphisiohome.myphisiohome.Dialogos.DialogoPlayEjercicios;
import com.myphisiohome.myphisiohome.R;


public class PlayPlanActivity extends AppCompatActivity {
    private int idPlan;
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
        DialogoPlayEjercicios newFragment = new DialogoPlayEjercicios();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment, "Play plan")
                .commit();
    }


}
