package com.myphisiohome.myphisiohome.Clases;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.BBDD.PacienteBBDD;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jose_ on 23/05/2017.
 */

public class Paciente {

    private int idPaciente;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String imagen;
    private String fecNacimiento;
    private float peso;
    private int estatura;
    private String sexo;
    private ArrayList<Plan> Planes;
    public Paciente(){

    }
    public Paciente (int idPaciente, String nombre, String apellidos,String email, String password,
                     String imagen,String fecNacimiento,float peso, int estatura, String sexo){

        this.idPaciente=idPaciente;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.email=email;
        this.password=password;
        this.imagen=imagen;
        this.fecNacimiento=fecNacimiento;
        this.estatura=estatura;
        this.peso=peso;
        this.sexo=sexo;
    }
    public Paciente ( String nombre, String email){
        this.nombre=nombre;
        this.email=email;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImagen() {
        return imagen;
    }

    public String getFecNacimiento() {
        return fecNacimiento;
    }

    public float getPeso() {
        return peso;
    }

    public int getEstatura() {
        return estatura;
    }

    public String getSexo() {
        return sexo;
    }

    public ArrayList<Plan> getPlanes() {
        return Planes;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setFecNacimiento(String fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEstatura(int estatura) {
        this.estatura = estatura;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setPlanes(ArrayList<Plan> planes) {
        this.Planes = planes;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PacienteBBDD.PacienteEntry.ID_PACIENTE, this.idPaciente);
        values.put(PacienteBBDD.PacienteEntry.NOMBRE, this.nombre);
        values.put(PacienteBBDD.PacienteEntry.APELLIDOS, this.apellidos);
        values.put(PacienteBBDD.PacienteEntry.EMAIL, this.email);
        values.put(PacienteBBDD.PacienteEntry.IAMGEN, this.imagen);
        values.put(PacienteBBDD.PacienteEntry.FEC_NACIMIENTO, this.fecNacimiento);
        values.put(PacienteBBDD.PacienteEntry.SEXO, this.sexo);
        values.put(PacienteBBDD.PacienteEntry.ESTATURA, this.estatura);
        values.put(PacienteBBDD.PacienteEntry.PESO, this.peso);
        return values;
    }


}
