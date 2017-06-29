package com.myphisiohome.myphisiohome.BBDD;

import android.provider.BaseColumns;

/**
 * Created by Vicente on 30/5/17.
 */

public class SeguimientoBBDD {
    public static abstract class SeguimientoEntry implements BaseColumns {
        public static final String TABLE_NAME ="seguimiento";

        public static final String ID_SEGUIMIENTO = "idSeguimiento";
        public static final String ID_PU = "idPU";
        public static final String SATISFACCION = "satisfaccion";
        public static final String COMENTARIOS = "comentarios";
        public static final String FECHA = "fecha";


    }
}
