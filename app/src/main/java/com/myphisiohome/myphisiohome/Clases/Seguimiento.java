package com.myphisiohome.myphisiohome.Clases;

import java.util.Date;

/**
 * Created by jose_ on 23/05/2017.
 */

public class Seguimiento {
    private int idSeguimiento;
    private String comentarios;
    private int satisfaccion;
    private String fecha;
    public Seguimiento(){};
    public Seguimiento(int idSeguimiento, String comentarios,int satisfaccion,
                       String fecha){
        this.idSeguimiento=idSeguimiento;
        this.comentarios=comentarios;
        this.satisfaccion=satisfaccion;
        this.fecha=fecha;

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
}
