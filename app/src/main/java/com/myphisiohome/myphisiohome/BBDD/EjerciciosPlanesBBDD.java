package com.myphisiohome.myphisiohome.BBDD;

import android.provider.BaseColumns;

/**
 * Created by Vicente on 30/5/17.
 */

public class EjerciciosPlanesBBDD {
    public static abstract class EjerciciosPlanesEntry implements BaseColumns {
        public static final String TABLE_NAME ="ejerciciosplanes";

        public static final String ID_EP = "idEP";
        public static final String ID_PLAN = "idPlan";
        public static final String ID_EJERCICIO = "idEjercicio";
        public static final String REPETICIONES = "repeticiones";

    }
}
