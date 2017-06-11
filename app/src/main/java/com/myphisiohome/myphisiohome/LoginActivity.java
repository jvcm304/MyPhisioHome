package com.myphisiohome.myphisiohome;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.API.MyPhisioApi;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.EjercicioPlanes;
import com.myphisiohome.myphisiohome.Clases.Paciente;
import com.myphisiohome.myphisiohome.Clases.Plan;
import com.myphisiohome.myphisiohome.Clases.PlanesUsuario;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    //private Retrofit mRestAdapter;
    private MyPhisioApi mMyPhisioApi;

    private static final String DUMMY_USER_ID = "0000000000@";
    private static final String DUMMY_PASSWORD = "dummy_password";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private EjerciciosTask ejerciciosTask=null;

    // UI references.
    private ImageView mLogoView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextInputLayout mFloatLabelEmail;
    private TextInputLayout mFloatLabelPassword;
    private View mProgressView;
    private View mLoginFormView;
    private static MyPhisioBBDDHelper pacienteBBDDHelper;
    private Paciente paciente;
    private PlanesUsuario planesUsuario;
    private Plan plan;
    private static EjercicioPlanes ejercicioPlanes;
    private static Ejercicio ejercicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirección al Login
        if (SessionPrefs.get(LoginActivity.this).isLoggedIn(getApplicationContext())) {
            startActivity(new Intent(this, PrincipalActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mLogoView = (ImageView) findViewById(R.id.image_logo);
        mFloatLabelEmail = (TextInputLayout) findViewById(R.id.float_label_email);
        mFloatLabelPassword = (TextInputLayout) findViewById(R.id.float_label_password);


        mPasswordView = (EditText) findViewById(R.id.password);
        // Setup
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (!isOnline()) {
                        showLoginError(getString(R.string.error_network));
                        return false;
                    }
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public EjerciciosTask getEjerciciosTask(int idPlan,Context context) {
        return new EjerciciosTask(idPlan, context);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;
        private Integer estado;
        private String mensaje;
        //datos paciente
        //private Paciente paciente;
        private int idPaciente;
        private String nombre;
        private String apellidos;
        private String email;
        private String imagen;
        private String fecNacimiento;
        private float peso;
        private int estatura;
        private String sexo;
        private EjerciciosTask taskEjercicios = null;
        private ArrayList<Integer> idPlanes=new ArrayList<Integer>();

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            pacienteBBDDHelper= new MyPhisioBBDDHelper(getApplicationContext());
            //
            int resul;
            String URLAPI="http://myphisio.digitalpower.es/v1/";
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post =
                    new HttpPost(URLAPI+"pacientes/login");

            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("email", mEmail);
                dato.put("password", mPassword);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                estado = respJSON.getInt("estado");
                resul=estado;
                if(estado!=1) {
                    mensaje = respJSON.getString("mensaje");


                }else{
                    try{
                        JSONObject jsonPaciente = respJSON.getJSONObject("usuario");
                        nombre =jsonPaciente.getString("nombre");
                        idPaciente = jsonPaciente.getInt("idPaciente");
                        apellidos = jsonPaciente.getString("apellidos");
                        email = jsonPaciente.getString("email");
                        fecNacimiento = jsonPaciente.getString("fecNacimiento");
                        String sPeso = jsonPaciente.getString("peso");
                        peso = Float.parseFloat(sPeso);
                        sexo = jsonPaciente.getString("sexo");
                        estatura = jsonPaciente.getInt("estatura");
                        imagen=jsonPaciente.getString("imagen");
                        paciente= new Paciente(idPaciente,nombre,apellidos,email,mPassword,imagen,
                                fecNacimiento,peso,estatura,sexo);
                        //comunicador.setPaciente(paciente);
                        pacienteBBDDHelper.savePaciente(paciente);
                        if(estado==1) {
                            //startPaciente();
                        }
                    }catch (JSONException e){
                        Log.e("ServicioRest","Error!", e);
                    }
                    //Planes pacientes
                    HttpGet getPlanesPaciente =
                            new HttpGet(URLAPI+"planesUsuario/paciente/"+Integer.toString(idPaciente));
                    //Planes



                    try
                    {
                        HttpResponse respPlanesPaciente = httpClient.execute(getPlanesPaciente);
                        String respStrPlanesPaciente = EntityUtils.toString(respPlanesPaciente.getEntity());
                        //
                        JSONObject respJSON2 = new JSONObject(respStrPlanesPaciente);
                        int estado2 = respJSON2.getInt("estado");
                        if(estado2==1){
                            JSONArray planesPaciente = respJSON2.getJSONArray("datos");
                            for (int i=0; i<planesPaciente.length() ;i++){
                                JSONObject planUsuario= planesPaciente.getJSONObject(i);
                                int idPU=planUsuario.getInt("idPU");
                                int idPlan=planUsuario.getInt("idPlan");
                                String dias=planUsuario.getString("dias");
                                //crear Plan
                                HttpGet getPlanes =
                                        new HttpGet(URLAPI+"planes/"+idPlan);
                                HttpResponse respPlanes = httpClient.execute(getPlanes);
                                String respStrPlanes = EntityUtils.toString(respPlanes.getEntity());
                                JSONArray planesA = new JSONArray(respStrPlanes);
                                for (int j=0; j<planesA.length() ;j++){
                                    JSONObject planes= planesA.getJSONObject(j);
                                    int idPlan2=planes.getInt("idPlan");
                                    String nombre=planes.getString("nombre");
                                    float tiempo=Float.valueOf(planes.getString("tiempo"));
                                    int series=planes.getInt("series");
                                    String descripcion=planes.getString("descripcion");
                                    String categoria=planes.getString("categoria");
                                    plan=new Plan(idPlan2,nombre,descripcion,categoria,series,tiempo,dias);
                                    idPlanes.add(idPlan2);
                                    pacienteBBDDHelper.savePlanes(plan);
                                }
                                //
                                int idPaciente=planUsuario.getInt("idPaciente");
                                float tiempo=Float.valueOf(planUsuario.getString("tiempo"));
                                int series=planUsuario.getInt("series");

                                planesUsuario=new PlanesUsuario(idPU,idPaciente,idPlan,tiempo,series,dias);
                                pacienteBBDDHelper.savePlanesUsuarios(planesUsuario);

                            }
                        }
                        //
                    }catch (JSONException e){
                        Log.e("ServicioRest","Error!", e);
                    }
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = 0;
            }

            return resul;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);
            for(int i=0;i<idPlanes.size();i++){
                taskEjercicios= new EjerciciosTask(idPlanes.get(i),getApplicationContext());
                taskEjercicios.execute();

            }

            switch (success) {
                case 1:
                    startPaciente();

                    //showAppointmentsScreen(nombre);
                    SessionPrefs.get(LoginActivity.this).savePaciente(paciente);


                    break;
                case 3:
                    showLoginError(mensaje);
                    break;
                case 7:
                    showLoginError(mensaje);
                    break;
                case 8:
                    showLoginError(mensaje);
                    break;
                case 0:
                    showLoginError(getString(R.string.error_server));
                    break;
            }
        }


    }



    public class EjerciciosTask extends AsyncTask<Void, Void, Integer> {

        private Integer estado;
        private String mensaje;

        //datos ejercicios planes
        //private EjercicioPlanes ejercicioPlanes;
        //private Ejercicio ejercicio;
        private int resul;
        private int idPlan;
        private Context context;

        EjerciciosTask(int idPlan,Context context) {
            this.context=context;
            this.idPlan=idPlan;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            pacienteBBDDHelper= new MyPhisioBBDDHelper(context);
            //
            int resul;
            String URLAPI="http://myphisio.digitalpower.es/v1/";
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet get =
                    new HttpGet(URLAPI+"ejerciciosPlanes/"+idPlan);

            get.setHeader("content-type", "application/json");

            try
            {

                HttpResponse resp = httpClient.execute(get);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                estado = respJSON.getInt("estado");
                resul=estado;

                if(estado!=1) {
                    mensaje = respJSON.getString("mensaje");


                }else if(estado==1){
                    try{
                        JSONArray ejerciciosPlanesJSON = respJSON.getJSONArray("datos");
                        for (int i=0; i<ejerciciosPlanesJSON.length() ;i++) {
                            JSONObject ejercicioPlan =ejerciciosPlanesJSON.getJSONObject(i);
                            int idEP=ejercicioPlan.getInt("idEP");
                            int idEjercicio=ejercicioPlan.getInt("idEjercicio");
                            float repeticiones=Float.valueOf(ejercicioPlan.getString("repeticiones"));
                            ejercicioPlanes =new EjercicioPlanes(idEP,idPlan,idEjercicio,repeticiones);
                            pacienteBBDDHelper.saveEjerciciosPlanes(ejercicioPlanes);
                            //Ejercicios
                            HttpGet getEjercicios=new HttpGet(URLAPI+"ejercicios/");
                            getEjercicios.setHeader("content-type", "application/json");
                            HttpResponse resp2 = httpClient.execute(getEjercicios);
                            String respStr2 = EntityUtils.toString(resp2.getEntity());

                            if(estado!=1) {
                                mensaje = respJSON.getString("mensaje");


                            }else if(estado==1){
                                try{
                                    //JSONArray ejerciciosJSON = respJSON2.getJSONArray("datos");
                                    JSONArray ejerciciosJSON = new JSONArray(respStr2);
                                    for (int j=0; j<ejerciciosJSON.length() ;j++) {
                                        JSONObject ejercicioObj =ejerciciosJSON.getJSONObject(j);
                                        int idE=ejercicioObj.getInt("idEjercicio");

                                        float repe;
                                        if(idEjercicio==idE){
                                            ejercicio= new Ejercicio(idEjercicio,ejercicioObj.getString("nombre"),
                                                    ejercicioObj.getString("descripcion"),
                                                    ejercicioObj.getString("tips"),
                                                    ejercicioObj.getString("categoria"),
                                                    ejercicioObj.getString("imagen"),
                                                    ejercicioObj.getInt("tipo"),
                                                    Float.valueOf(ejercicioObj.getString("repetiones")));
                                            //plan.addEjercicios(ejercicio);
                                            pacienteBBDDHelper.saveEjercicio(ejercicio);
                                        }
                                    }
                                }catch (JSONException e){
                                    Log.e("ServicioRest","Error!", e);
                                }
                            }

                        }
                    }catch (JSONException e){
                        Log.e("ServicioRest","Error!", e);
                    }
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = 0;
            }

            return resul;
        }

        @Override
        protected void onPostExecute(final Integer success) {

        }
    }
    //



    private void showLoginError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
    private void startPaciente() {

        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}
