package com.myphisiohome.myphisiohome.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.myphisiohome.myphisiohome.Clases.Paciente;

/**
 * Created by jose_ on 24/05/2017.
 */

public class SessionPrefs {

    private static SessionPrefs INSTANCE;
    public static final String PREFS_NAME = "MYPHISIO_PREFS";
    public static final String PREF_PACIENTE_EMAIL = "PREF_PACIENTE_EMAIL";
    public static final String PREF_PACIENTE_NAME = "PREF_PACIENTE_NAME";
    public static final String PREF_PACIENTE_ID = "PREF_PACIENTE_ID";
    public static final String PREF_PACIENTE_IMAGE = "PREF_PACIENTE_IMAGE";
    public static final String PREF_PACIENTE_APELLIDOS = "PREF_PACIENTE_APELLIDOS";
    public static final String PREF_PACIENTE_NACIMIENTO = "PREF_PACIENTE_NACIMIENTO";
    public static final String PREF_PACIENTE_ESTATURA = "PREF_PACIENTE_ESTATURA";
    public static final String PREF_PACIENTE_SEXO = "PREF_PACIENTE_SEXO";
    public static final String PREF_PACIENTE_PESO = "PREF_PACIENTE_PESO";

    public static final String PREF_PACIENTE_TOKEN = "PREF_PACIENTE_TOKEN";
    public static final String PREF_PACIENTE_LOGGED = "PREF_PACIENTE_LOGGED";
    private boolean mIsLoggedIn = false;
    private final SharedPreferences mPrefs;

    private SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_PACIENTE_TOKEN, null));
    }
    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }
    public boolean isLoggedIn(Context context){
            SharedPreferences prefs=context.getApplicationContext()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_PACIENTE_LOGGED,false);
    }

    public void savePaciente(Paciente paciente) {
        if (paciente != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_PACIENTE_EMAIL, paciente.getEmail());
            editor.putString(PREF_PACIENTE_NAME, paciente.getNombre());
            editor.putInt(PREF_PACIENTE_ID, paciente.getIdPaciente());
            editor.putString(PREF_PACIENTE_IMAGE, paciente.getImagen());
            editor.putString(PREF_PACIENTE_APELLIDOS, paciente.getApellidos());
            editor.putString(PREF_PACIENTE_NACIMIENTO, paciente.getFecNacimiento());
            editor.putFloat(PREF_PACIENTE_PESO, paciente.getPeso());
            editor.putInt(PREF_PACIENTE_ESTATURA, paciente.getEstatura());
            editor.putString(PREF_PACIENTE_SEXO, paciente.getSexo());
            editor.putBoolean(PREF_PACIENTE_LOGGED, true);
            //editor.putString(PREF_AFFILIATE_GENDER, paciente.getGender());
            //editor.putString(PREF_AFFILAITE_TOKEN, paciente.getToken());
            mIsLoggedIn = true;
            editor.commit();


        }
    }
    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_PACIENTE_EMAIL, null);
        editor.putString(PREF_PACIENTE_NAME, null);
        editor.putString(PREF_PACIENTE_IMAGE, null);
        editor.putString(PREF_PACIENTE_APELLIDOS, null);
        editor.putInt(PREF_PACIENTE_ID, -1);
        editor.putString(PREF_PACIENTE_NACIMIENTO, null);
        editor.putFloat(PREF_PACIENTE_PESO, 0);
        editor.putInt(PREF_PACIENTE_ESTATURA, 0);
        editor.putString(PREF_PACIENTE_SEXO, null);
        editor.putBoolean(PREF_PACIENTE_LOGGED, false);
        //editor.putString(PREF_AFFILAITE_TOKEN, null);
        editor.commit();
    }

}
