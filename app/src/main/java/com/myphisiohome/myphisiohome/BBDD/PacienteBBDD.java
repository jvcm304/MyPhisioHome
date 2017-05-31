package com.myphisiohome.myphisiohome.BBDD;

import android.provider.BaseColumns;

/**
 * Created by Vicente on 30/5/17.
 */

public class PacienteBBDD {
    public static abstract class PacienteEntry implements BaseColumns {
        public static final String TABLE_NAME ="pacientes";

        public static final String ID_PACIENTE = "idPaciente";
        public static final String NOMBRE = "nombre";
        public static final String EMAIL = "email";
        public static final String IAMGEN = "imagen";
        public static final String FEC_NACIMIENTO = "fecNacimiento";
        public static final String ESTATURA = "estatura";
        public static final String SEXO = "sexo";
        public static final String PESO = "peso";
        public static final String APELLIDOS = "apellidos";

    }

}
