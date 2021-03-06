package com.myphisiohome.myphisiohome.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Paciente;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.Clases.Seguimiento;

/**
 * Created by Vicente on 30/5/17.
 */

public class MyPhisioBBDDHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyPhisio.db";

    public MyPhisioBBDDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE pacientes (" +
                "   idPaciente INTEGER NOT NULL," +
                "   nombre TEXT NOT NULL," +
                "   email TEXT PRIMARY KEY NOT NULL ," +
                "   imagen TEXT DEFAULT NULL," +
                "   fecNacimiento DATETIME DEFAULT NULL," +
                "   estatura INTEGER DEFAULT NULL," +
                "   sexo TEXT DEFAULT NULL," +
                "   peso REAL DEFAULT NULL," +
                "   apellidos TEXT DEFAULT NULL" +
                ")");

        db.execSQL("CREATE TABLE ejercicios (" +
                "  idEjercicio INTEGER PRIMARY KEY," +
                "  nombre TEXT NOT NULL," +
                "  descripcion TEXT DEFAULT NULL," +
                "  tips TEXT DEFAULT NULL," +
                "  categoria TEXT NOT NULL," +
                "  imagen TEXT DEFAULT NULL," +
                "  tipo INTEGER NOT NULL," +
                "  repeticiones REAL DEFAULT NULL" +
                ")");
        db.execSQL("CREATE TABLE planes (" +
                "  idPlan INTEGER PRIMARY KEY," +
                "  nombre TEXT NOT NULL," +
                "  descripcion TEXT DEFAULT NULL," +
                "  categoria TEXT NOT NULL," +
                "  series INTEGER DEFAULT NULL," +
                "  tiempo REAL DEFAULT NULL," +
                "  dias TEXT DEFAULT NULL" +
                ")");
        db.execSQL("CREATE TABLE ejerciciosplanes (" +
                "  idEP INTEGER PRIMARY KEY," +
                "  idPlan INTEGER NOT NULL," +
                "  idEjercicio INTEGER NOT NULL," +
                "  repeticiones REAL NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE planesusuarios (" +
                "  idPU INTEGER PRIMARY KEY," +
                "  idPlan INTEGER NOT NULL," +
                "  idPaciente INTEGER NOT NULL," +
                "  tiempo REAL DEFAULT NULL," +
                "  series INTEGER DEFAULT NULL," +
                "  dias TEXT DEFAULT NULL" +
                ")");
        db.execSQL("CREATE TABLE seguimiento (" +
                "  idSeguimiento INTEGER PRIMARY KEY," +
                "  idPU INTEGER NOT NULL," +
                "  satisfaccion INTEGER DEFAULT NULL," +
                "  fecha TEXT DEFAULT NULL," +
                "  comentarios TEXT DEFAULT NULL" +
                ")");

        // Insertar datos ficticios para prueba inicial
        //mockData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long mockPaciente(SQLiteDatabase db, Paciente paciente) {
        return db.insert(
                PacienteBBDD.PacienteEntry.TABLE_NAME,
                null,
                paciente.toContentValues());
    }
    public long savePaciente(Paciente paciente) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();



        return sqLiteDatabase.insert(
                PacienteBBDD.PacienteEntry.TABLE_NAME,
                null,
                paciente.toContentValues());

    }
    public long saveEjercicio(Ejercicio ejercicio) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                EjercicioBBDD.EjercicioEntry.TABLE_NAME,
                null,
                ejercicio.toContentValues());

    }
    public long saveSeguimiento(Seguimiento seguimiento) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                SeguimientoBBDD.SeguimientoEntry.TABLE_NAME,
                null,
                seguimiento.toContentValues());
    }
    public long savePlanes(Plan plan) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                PlanBBDD.PlanEntry.TABLE_NAME,
                null,
                plan.toContentValues());

    }
    public long saveEjerciciosPlanes(EjercicioPlanes ePlanes) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                EjerciciosPlanesBBDD.EjerciciosPlanesEntry.TABLE_NAME,
                null,
                ePlanes.toContentValues());

    }
    public long savePlanesUsuarios(PlanesUsuario pUsuario) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                PlanesUsuarioBBDD.PlanesUsuarioEntry.TABLE_NAME,
                null,
                pUsuario.toContentValues());

    }
    public void refrescar(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM ejercicios");
        sqLiteDatabase.execSQL("DELETE FROM planes");
        sqLiteDatabase.execSQL("DELETE FROM ejerciciosplanes");
        sqLiteDatabase.execSQL("DELETE FROM planesusuarios");
        sqLiteDatabase.execSQL("DELETE FROM seguimiento");
    }

    public void loogOut(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //sqLiteDatabase.execSQL("drop table if exists pacientes");
        sqLiteDatabase.execSQL("DELETE FROM pacientes");
        sqLiteDatabase.execSQL("DELETE FROM ejercicios");
        sqLiteDatabase.execSQL("DELETE FROM planes");
        sqLiteDatabase.execSQL("DELETE FROM ejerciciosplanes");
        sqLiteDatabase.execSQL("DELETE FROM planesusuarios");
        sqLiteDatabase.execSQL("DELETE FROM seguimiento");
    }

    public Cursor getAllPaciente() {
        return getReadableDatabase()
                .query(
                        PacienteBBDD.PacienteEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public Cursor getAllPlanes() {

        return getReadableDatabase()
                .query(
                        PlanBBDD.PlanEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public Cursor getAllEjercicios() {

        return getReadableDatabase()
                .query(
                        EjercicioBBDD.EjercicioEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public Cursor getEjerciciosByPlan(int idPlan) {
        String[] args = new String[] {Integer.toString(idPlan)};
        Cursor c = getReadableDatabase().rawQuery( "SELECT * FROM ejercicios, ejerciciosplanes WHERE ejerciciosplanes.idPlan=?"+
                "AND ejercicios.idEjercicio=ejerciciosplanes.idEjercicio; ",  args);
        return c;
    }
    public Cursor getPlanesByPaciente(int idPaciente) {
        String[] args = new String[] {Integer.toString(idPaciente)};
        Cursor c = getReadableDatabase().rawQuery( "SELECT * FROM planes, planesusuarios WHERE planesusuarios.idPaciente=?"+
                "AND planes.idPlan=planesusuarios.idPlan; ",  args);
        return c;
    }
    public Cursor getSeguimientoByPaciente(int idPaciente) {
        String[] args = new String[] {Integer.toString(idPaciente)};
        Cursor c = getReadableDatabase().rawQuery( "SELECT * FROM seguimiento, planesusuarios,planes" +
                " WHERE planesusuarios.idPaciente=?"+
                "AND seguimiento.idPU=planesusuarios.idPU " +
                "AND planesusuarios.idPlan=planes.idPlan; ",  args);
        return c;
    }
    public Cursor getSeguimientoById(int idSeguimiento) {
        String[] args = new String[] {Integer.toString(idSeguimiento)};
        Cursor c = getReadableDatabase().rawQuery( "SELECT * FROM seguimiento, planesusuarios,planes" +
                " WHERE seguimiento.idSeguimiento=?"+
                "AND seguimiento.idPU=planesusuarios.idPU " +
                "AND planesusuarios.idPlan=planes.idPlan  ; ",  args);
        return c;
    }

    public Cursor getPlanById(int idPlan) {

        String[] args = new String[] {Integer.toString(idPlan)};
        Cursor c = getReadableDatabase().rawQuery( "SELECT * FROM planes, planesusuarios WHERE planes.idPlan=? "+
                "AND planes.idPlan=planesusuarios.idPlan; ",  args);
        return c;
    }
    public Cursor getPlanById2(int idPlan) {

        String[] args = new String[] {Integer.toString(idPlan)};
        Cursor c = getReadableDatabase().rawQuery( "SELECT * FROM planes WHERE planes.idPlan=? "
                ,  args);
        return c;
    }
    public Cursor getEjercicioById(int idEjercicio) {
        Cursor c = getReadableDatabase().query(
                EjercicioBBDD.EjercicioEntry.TABLE_NAME,
                null,
                EjercicioBBDD.EjercicioEntry.ID_EJERCICIO + " LIKE ?",
                new String[]{Integer.toString(idEjercicio)},
                null,
                null,
                null);
        return c;
    }

    public Cursor getPacienteById(int idPaciente) {
        Cursor c = getReadableDatabase().query(
                PacienteBBDD.PacienteEntry.TABLE_NAME,
                null,
                PacienteBBDD.PacienteEntry.ID_PACIENTE + " LIKE ?",
                new String[]{Integer.toString(idPaciente)},
                null,
                null,
                null);
        return c;
    }

    public int deletePaciente(int idPaciente) {
        return getWritableDatabase().delete(
                PacienteBBDD.PacienteEntry.TABLE_NAME,
                PacienteBBDD.PacienteEntry.ID_PACIENTE + " LIKE ?",
                new String[]{Integer.toString(idPaciente)});
    }
    public int deleteEjercicio(int idEjercicio) {
        return getWritableDatabase().delete(
                EjercicioBBDD.EjercicioEntry.TABLE_NAME,
                EjercicioBBDD.EjercicioEntry.ID_EJERCICIO + " LIKE ?",
                new String[]{Integer.toString(idEjercicio)});
    }

    public int deletePlanPacienteidPU(int idPU) {

        return getWritableDatabase().delete(
                PlanesUsuarioBBDD.PlanesUsuarioEntry.TABLE_NAME,
                PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PU + " LIKE ?",
                new String[]{Integer.toString(idPU)});
    }
    public int deletePlanPacienteidPaciente(int idPaciente) {

        return getWritableDatabase().delete(
                PlanesUsuarioBBDD.PlanesUsuarioEntry.TABLE_NAME,
                PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PACIENTE + " LIKE ?",
                new String[]{Integer.toString(idPaciente)});
    }
    public int deletePlanPacienteidPlan(int idPlan) {

        return getWritableDatabase().delete(
                PlanesUsuarioBBDD.PlanesUsuarioEntry.TABLE_NAME,
                PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PLAN + " LIKE ?",
                new String[]{Integer.toString(idPlan)});
    }
    public int deletePlan(int idPlan) {

        return getWritableDatabase().delete(
                PlanBBDD.PlanEntry.TABLE_NAME,
                PlanBBDD.PlanEntry.ID_PLAN + " LIKE ?",
                new String[]{Integer.toString(idPlan)});
    }

    public int deleteEjercicioPlan(int idEjercicio) {
        return getWritableDatabase().delete(
                EjerciciosPlanesBBDD.EjerciciosPlanesEntry.TABLE_NAME,
                EjerciciosPlanesBBDD.EjerciciosPlanesEntry.ID_EJERCICIO + " LIKE ?",
                new String[]{Integer.toString(idEjercicio)});
    }
    public int deletePlanEjercicio(int idPlan) {
        return getWritableDatabase().delete(
                EjerciciosPlanesBBDD.EjerciciosPlanesEntry.TABLE_NAME,
                EjerciciosPlanesBBDD.EjerciciosPlanesEntry.ID_PLAN + " LIKE ?",
                new String[]{Integer.toString(idPlan)});
    }

    public int deleteSeguimiento(int idPU){
        return getWritableDatabase().delete(
                SeguimientoBBDD.SeguimientoEntry.TABLE_NAME,
                SeguimientoBBDD.SeguimientoEntry.ID_PU + " LIKE ?",
                new String[]{Integer.toString(idPU)});
    }
    public int deleteSeguimiento2(int idPlan){
        String sql=("DELETE  FROM seguimiento WHERE  idPU=(SELECT idPU FROM planesusuarios WHERE idPlan="+idPlan+")");
        Log.e("idPlan:   ",""+idPlan  );
        Log.e("SQL:   ",sql  );
        getWritableDatabase().execSQL(sql);
        return idPlan;
    }
    public int deleteSeguimiento3(int idPaciente){
        String sql=("DELETE  FROM seguimiento WHERE  idPU=(SELECT idPU FROM planesusuarios WHERE idPaciente="+idPaciente+")");
        Log.e("SQL:   ",sql  );
        getWritableDatabase().execSQL(sql);
        return idPaciente;
    }


    public int updatePaciente2(Paciente paciente, int idPaciente) {
        String sql=("UPDATE pacientes SET nombre='"+paciente.getNombre()
                +"', email='"+paciente.getEmail()+"',fecNacimiento='"+paciente.getFecNacimiento()
                +"',estatura='"+paciente.getEstatura()+"',imagen='"+paciente.getImagen()
                +"',sexo='"+paciente.getSexo()+"',peso='"+paciente.getPeso()
                +"' WHERE idPaciente="+idPaciente);
        //Log.e("SQL:   ",sql  );
        getWritableDatabase().execSQL(sql);
        return idPaciente;
    }
    public int getIdPU(int idPaciente, int idPlan){
        String[] args = new String[2];
        args[0]=Integer.toString(idPaciente);
        args[1]=Integer.toString(idPlan);
        Cursor c = getReadableDatabase().rawQuery( "SELECT idPU FROM planesusuarios WHERE idPaciente=?"+
                "AND idPlan=?",  args);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(PlanesUsuarioBBDD.PlanesUsuarioEntry.ID_PU));
    }

}
