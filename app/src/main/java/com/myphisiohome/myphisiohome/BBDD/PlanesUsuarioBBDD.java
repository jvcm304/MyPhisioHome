package com.myphisiohome.myphisiohome.BBDD;

import android.provider.BaseColumns;

/**
 * Created by Vicente on 30/5/17.
 */

public class PlanesUsuarioBBDD {
    public static abstract class PlanesUsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME ="planesusuarios";

        public static final String ID_PU = "idPU";
        public static final String ID_PLAN = "idPlan";
        public static final String ID_PACIENTE = "idPaciente";
        public static final String TIEMPO = "tiempo";
        public static final String SERIES = "series";
        public static final String DIAS = "dias";


    }
}
