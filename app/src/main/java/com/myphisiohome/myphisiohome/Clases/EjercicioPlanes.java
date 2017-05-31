package com.myphisiohome.myphisiohome.Clases;

import android.content.ContentValues;

import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.EjerciciosPlanesBBDD;

/**
 * Created by Vicente on 30/5/17.
 */

public class EjercicioPlanes {
    private int idEP;
    private int idPlan;
    private int idEjercicio;
    private float repeticiones;
    public EjercicioPlanes(){}
    public EjercicioPlanes(int idEP, int idPlan, int idEjercicio, float repeticiones){
        this.idEjercicio=idEjercicio;
        this.idEP=idEP;
        this.idPlan=idPlan;
        this.repeticiones=repeticiones;

    }
    public float getRepeticiones() {
        return repeticiones;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public int getIdEP() {
        return idEP;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public void setIdEP(int idEP) {
        this.idEP = idEP;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public void setRepeticiones(float repeticiones) {
        this.repeticiones = repeticiones;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(EjerciciosPlanesBBDD.EjerciciosPlanesEntry.ID_EJERCICIO, this.idEjercicio);
        values.put(EjerciciosPlanesBBDD.EjerciciosPlanesEntry.ID_EP, this.idEP);
        values.put(EjerciciosPlanesBBDD.EjerciciosPlanesEntry.ID_PLAN, this.idPlan);
        values.put(EjerciciosPlanesBBDD.EjerciciosPlanesEntry.REPETICIONES, this.repeticiones);

        return values;
    }
}
