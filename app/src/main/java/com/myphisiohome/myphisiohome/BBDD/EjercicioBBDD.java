package com.myphisiohome.myphisiohome.BBDD;

import android.provider.BaseColumns;

/**
 * Created by Vicente on 30/5/17.
 */

public class EjercicioBBDD {
    public static abstract class EjercicioEntry implements BaseColumns {
        public static final String TABLE_NAME ="ejercicios";

        public static final String ID_EJERCICIO = "idEjercicio";
        public static final String NOMBRE = "nombre";
        public static final String DESCRIPCION = "descripcion";
        public static final String TIPS = "tips";
        public static final String CATEGORIA = "categoria";
        public static final String IMAGEN = "imagen";
        public static final String TIPO = "tipo";
        public static final String REPETICIONES = "repeticiones";

    }
}
