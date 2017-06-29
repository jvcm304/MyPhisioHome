package com.myphisiohome.myphisiohome.Clases;

import android.content.ContentValues;

import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.SeguimientoBBDD;

import java.util.Date;

/**
 * Created by jose_ on 23/05/2017.
 */

public class Seguimiento {
    private int idSeguimiento;
    private String comentarios;
    private int satisfaccion;
    private String fecha;
    private int idPU;
    public Seguimiento(){};
    public Seguimiento(int idSeguimiento,int idPU, String comentarios,int satisfaccion,
                       String fecha){
        this.idPU=idPU;
        this.idSeguimiento=idSeguimiento;
        this.comentarios=comentarios;
        this.satisfaccion=satisfaccion;
        this.fecha=fecha;

    }

    public int getIdPU() {
        return idPU;
    }

    public void setIdPU(int idPU) {
        this.idPU = idPU;
    }

    public int getIdSeguimiento() {
        return idSeguimiento;
    }

    public String getFecha() {
        return fecha;
    }

    public int getSatisfaccion() {
        return satisfaccion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setIdSeguimiento(int idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public void setSatisfaccion(int satisfaccion) {
        this.satisfaccion = satisfaccion;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(SeguimientoBBDD.SeguimientoEntry.ID_PU, this.idPU);
        values.put(SeguimientoBBDD.SeguimientoEntry.ID_SEGUIMIENTO, this.idSeguimiento);
        values.put(SeguimientoBBDD.SeguimientoEntry.COMENTARIOS, this.comentarios);
        values.put(SeguimientoBBDD.SeguimientoEntry.SATISFACCION, this.satisfaccion);
        values.put(SeguimientoBBDD.SeguimientoEntry.FECHA, this.fecha);

        return values;
    }
}
