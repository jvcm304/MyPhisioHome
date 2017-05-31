package com.myphisiohome.myphisiohome.Clases;

import android.content.ContentValues;

import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;
import com.myphisiohome.myphisiohome.BBDD.PlanesUsuarioBBDD;

/**
 * Created by Vicente on 30/5/17.
 */

public class PlanesUsuario {

    private int idPU;
    private int idPlan;
    private int idPaciente;
    private float tiempo;
    private int series;
    private String dias;

    public PlanesUsuario(){}
    public PlanesUsuario(int idPU, int idPlan, int idPaciente, float tiempo, int series, String dias){
        this.idPU=idPU;
        this.idPlan=idPlan;
        this.idPaciente=idPaciente;
        this.tiempo=tiempo;
        this.series=series;
        this.dias=dias;


    }
    public int getIdPU() {
        return idPU;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public float getTiempo() {
        return tiempo;
    }

    public int getSeries() {
        return series;
    }

    public String getDias() {
        return dias;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public void setIdPU(int idPU) {
        this.idPU = idPU;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PLAN, this.idPlan);
        values.put(PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PACIENTE, this.idPaciente);
        values.put(PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PU, this.idPU);
        values.put(PlanesUsuarioBBDD.PlanesUsuarioEntry.DIAS, this.dias);
        values.put(PlanesUsuarioBBDD.PlanesUsuarioEntry.SERIES, this.series);
        values.put(PlanesUsuarioBBDD.PlanesUsuarioEntry.TIEMPO, this.tiempo);
        return values;
    }

}
