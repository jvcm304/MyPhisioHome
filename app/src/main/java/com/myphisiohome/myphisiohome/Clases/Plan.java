package com.myphisiohome.myphisiohome.Clases;

import android.content.ContentValues;

import com.myphisiohome.myphisiohome.BBDD.PlanBBDD;

import java.util.ArrayList;

/**
 * Created by jose_ on 23/05/2017.
 */

public class Plan {

    private int idPlan;
    private String nombre;
    private String descipcion;
    private String categoria;
    private int series;
    private float tiempo;
    private String dias;
    private ArrayList<Seguimiento> seguimiento;
    private ArrayList<Ejercicio> ejercicios;
    public Plan(){};
    public Plan(int idPlan, String nombre, String descipcion,String categoria, int series,
                float tiempo,String dias){
        this.descipcion=descipcion;
        this.idPlan=idPlan;
        this.nombre=nombre;
        this.dias=dias;
        this.categoria=categoria;
        this.series=series;
        this.tiempo=tiempo;

    }
    public Plan(int idPlan, String nombre, String descipcion,String categoria, int series,
                float tiempo){
        this.descipcion=descipcion;
        this.idPlan=idPlan;
        this.nombre=nombre;
        this.categoria=categoria;
        this.series=series;
        this.tiempo=tiempo;

    }

    public int getIdPlan() {
        return idPlan;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public int getSeries() {
        return series;
    }

    public float getTiempo() {
        return tiempo;
    }

    public String getDias() {
        return dias;
    }

    public ArrayList<Seguimiento> getSeguimiento() {
        return seguimiento;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setSeguimiento(ArrayList<Seguimiento> seguimiento) {
        this.seguimiento = seguimiento;
    }
    public void addEjercicios(Ejercicio ejercicio){
        this.ejercicios.add(ejercicio);
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PlanBBDD.PlanEntry.ID_PLAN, this.idPlan);
        values.put(PlanBBDD.PlanEntry.NOMBRE, this.nombre);
        values.put(PlanBBDD.PlanEntry.CATEGORIA, this.categoria);
        values.put(PlanBBDD.PlanEntry.DESCRIPCION, this.descipcion);
        values.put(PlanBBDD.PlanEntry.SERIES, this.series);
        values.put(PlanBBDD.PlanEntry.DIAS, this.dias);
        values.put(PlanBBDD.PlanEntry.TIEMPO, this.tiempo);
        return values;
    }
}
