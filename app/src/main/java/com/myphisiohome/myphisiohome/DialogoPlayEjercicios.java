package com.myphisiohome.myphisiohome;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.Clases.Plan;

/**
 * Created by Vicente on 9/6/17.
 */

public class DialogoPlayEjercicios extends DialogFragment {
    private static final String TAG = DialogoPlayEjercicios.class.getSimpleName();


    public DialogoPlayEjercicios() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogoPlayEjercicios();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como play de planes
     *
     * @return Diálogo
     */
    public AlertDialog createDialogoPlayEjercicios() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_play, null);


        int idPlan=getArguments().getInt("idPlan");
        Log.e("idPlan-->",Integer.toString(idPlan));

        ImageView imagen = (ImageView) v.findViewById(R.id.play_imagen);
        TextView nombre = (TextView) v.findViewById(R.id.play_nombre);
        TextView repeticiones = (TextView) v.findViewById(R.id.play_repeticones);
        TextView tips = (TextView) v.findViewById(R.id.play_tips);
        ProgressBar barra =(ProgressBar) v.findViewById(R.id.play_progress);
        repeticiones.setText(R.string.app_name);
        //nombre.setText(R.string.app_name);
        Log.e("idPlan-->",Integer.toString(idPlan));
        builder.setView(v);
        //dismiss();

        return builder.create();
    }

}