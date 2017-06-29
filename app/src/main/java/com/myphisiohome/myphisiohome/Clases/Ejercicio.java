package com.myphisiohome.myphisiohome.Clases;

import android.content.ContentValues;
import android.media.Image;

import com.myphisiohome.myphisiohome.BBDD.EjercicioBBDD;
import com.myphisiohome.myphisiohome.BBDD.EjerciciosPlanesBBDD;

/**
 * Created by jose_ on 23/05/2017.
 */

public class Ejercicio {
    private int idEjercicio;
    private String nombre;
    private String descripcion;
    private String tips;
    private String categoria;
    private String imagen;
    private int tipo;
    private float repeticiones;
    public Ejercicio(){};
    public Ejercicio(int idEjercicio, String nombre, String descripcion, String tips,
                     String categoria, String imagen, int tipo){

        this.idEjercicio=idEjercicio;
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.tips=tips;
        this.categoria=categoria;
        this.imagen=imagen;
        this.tipo=tipo;


    }
    public Ejercicio(int idEjercicio, String nombre, String descripcion, String tips,
                     String categoria, String imagen, int tipo,float repeticiones){

        this.idEjercicio=idEjercicio;
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.tips=tips;
        this.categoria=categoria;
        this.imagen=imagen;
        this.tipo=tipo;
        this.repeticiones=repeticiones;


    }


    public int getIdEjercicio() {
        return idEjercicio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public float getRepeticiones() {
        return repeticiones;
    }

    public int getTipo() {
        return tipo;
    }

    public String getTips() {
        return tips;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setRepeticiones(float repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(EjercicioBBDD.EjercicioEntry.ID_EJERCICIO, this.idEjercicio);
        values.put(EjercicioBBDD.EjercicioEntry.NOMBRE, this.nombre);
        values.put(EjercicioBBDD.EjercicioEntry.CATEGORIA, this.categoria);
        values.put(EjercicioBBDD.EjercicioEntry.DESCRIPCION, this.descripcion);
        values.put(EjercicioBBDD.EjercicioEntry.TIPO, this.tipo);
        values.put(EjercicioBBDD.EjercicioEntry.TIPS, this.tips);
        values.put(EjercicioBBDD.EjercicioEntry.IMAGEN, this.imagen);
        values.put(EjercicioBBDD.EjercicioEntry.REPETICIONES, this.repeticiones);
        return values;
    }
}
