package com.myphisiohome.myphisiohome.BBDD;

import android.provider.BaseColumns;

/**
 * Created by Vicente on 30/5/17.
 */

public class PlanBBDD {
    public static abstract class PlanEntry implements BaseColumns {
        public static final String TABLE_NAME ="planes";

        public static final String ID_PLAN = "idPlan";
        public static final String NOMBRE = "nombre";
        public static final String DESCRIPCION = "descripcion";
        public static final String CATEGORIA = "categoria";
        public static final String SERIES = "series";
        public static final String TIEMPO = "tiempo";


    }
}
