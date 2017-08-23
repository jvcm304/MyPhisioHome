package com.myphisiohome.myphisiohome.Dialogos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myphisiohome.myphisiohome.Actividades.AdministradorActivity;
import com.myphisiohome.myphisiohome.AsyncTask.AddPacienteServidor;
import com.myphisiohome.myphisiohome.AsyncTask.SubirArchivoHttp;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Paciente;
import com.myphisiohome.myphisiohome.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Vicente on 12/6/17.
 */

public class DialogAddPaciente extends DialogFragment {

    private FloatingActionButton mSaveButton;
    private TextView textFecha;
    private TextView mNombre;
    private TextView mEmail;
    private TextView mEstatura;
    private TextView mPeso;
    private TextView mPass;
    private TextView mPass2;
    private Spinner mSexo;
    private String sexoSpinner;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private ImageButton mImageButton;
    private int idPaciente;
    private String filePath;
    private String image;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private Drawable d;
    private static final int SELECTED_PICTURE=1;
    private SharedPreferences prefs;
    public DialogAddPaciente() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Obtener instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

        if (actionBar != null) {
            // Habilitar el Up Button
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar icono del Up Button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_paciente, container, false);
        iniciarFecha(view);// Setear fecha inicial
        mSaveButton = (FloatingActionButton) view.findViewById(R.id.et_fab);
        mImageButton=(ImageButton) view.findViewById(R.id.et_image);
        mNombre = (TextView) view.findViewById(R.id.et_name);
        mEmail = (TextView) view.findViewById(R.id.et_email);
        mEstatura = (TextView) view.findViewById(R.id.et_estatura);
        mPeso = (TextView) view.findViewById(R.id.et_peso);
        mSexo = (Spinner) view.findViewById(R.id.et_sexo);
        mPass = (TextView) view.findViewById(R.id.et_pass);
        mPass2 = (TextView) view.findViewById(R.id.et_pass2);
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.default_user);
        mImageButton.setImageBitmap(bitmap);
        mSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexoSpinner= (String) adapterView.getItemAtPosition(i);
                sexoSpinner= (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sexoSpinner="Masculino";
            }


        });

        mImageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                    Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,SELECTED_PICTURE);

            }

        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDatos()){
                    long result=addPaciente();
                    //Log.e("resultado----",Integer.toString(result));

                    startActivity(new Intent(getActivity(), AdministradorActivity.class));

                }


            }
        });
        return view;
    }

    private boolean checkDatos() {
        boolean result=true;
        if(mNombre.getText().toString().isEmpty() || mEmail.getText().toString().isEmpty() ||
            mEstatura.getText().toString().isEmpty() || mPeso.getText().toString().isEmpty() ||
            mPass.getText().toString().isEmpty() || mPass2.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"Deben estar todos los campos rellenos",Toast.LENGTH_LONG).show();
            result=false;
            return false;
        }
        if(!mEmail.getText().toString().contains("@")){
            Toast.makeText(getActivity(),"Debe poner un email valido",Toast.LENGTH_LONG).show();
            result=false;
            return false;
        }
        if(!mPass.getText().toString().equals(mPass2.getText().toString())){
            Toast.makeText(getActivity(),"Las contraseñas deben coincidir",Toast.LENGTH_LONG).show();
            result=false;
            return false;
        }else if(mPass.length()<=4){
            Toast.makeText(getActivity(),"Las contraseñas debe tener una longitud mayor a 4",Toast.LENGTH_LONG).show();
            result=false;
            return false;
        }
        return result;
    }

    public long addPaciente(){
        if (filePath==null){
            image="default_user.png";
        }else{
            File file = new File(filePath);
            //subir imagen a servidor
            image=file.getName();
            SubirArchivoHttp subirArchivoHttp =new SubirArchivoHttp(filePath);
            subirArchivoHttp.execute();
        }


        Calendar calendar= new GregorianCalendar();
        String calen=Integer.toString(calendar.get(Calendar.HOUR_OF_DAY))+
                Integer.toString(calendar.get(Calendar.MINUTE))+Integer.toString(calendar.get(Calendar.SECOND));
        myPhisioBBDDHelper= new MyPhisioBBDDHelper(getContext());
        Paciente paciente=new Paciente(Integer.valueOf(calen),mNombre.getText().toString(), mEmail.getText().toString(),
                mPass.getText().toString(),image,textFecha.getText().toString(),mPeso.getText().toString(),
                Integer.valueOf(mEstatura.getText().toString()),mSexo.getSelectedItem().toString() );


        long result= myPhisioBBDDHelper.savePaciente(paciente);
        Log.e("idPaciente--->",calen);

        AddPacienteServidor addPacienteServidor=new AddPacienteServidor(paciente,getActivity());
        addPacienteServidor.execute();

        return result;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        DialogAddPaciente.super.onActivityResult(requestCode,resultCode,data);
        try{
            switch (requestCode){
                case SELECTED_PICTURE:
                    Uri uri=data.getData();
                    String[]projection={MediaStore.Images.Media.DATA};

                    Cursor cursor=getActivity().getContentResolver().query(uri,projection,null,null,null);
                    cursor.moveToFirst();

                    int columIndex=cursor.getColumnIndex(projection[0]);
                    filePath=cursor.getString(columIndex);
                    cursor.close();
                    Bitmap yourSlectedImage=BitmapFactory.decodeFile(filePath);

                    d=resizeImage(yourSlectedImage);//new BitmapDrawable(yourSlectedImage);
                    mImageButton.setImageDrawable(d);
                    break;
                default:

                    break;
            }
        }catch (NullPointerException e){

        }

    }

    private Drawable resizeImage(Bitmap yourSlectedImage) {


        int width = yourSlectedImage.getWidth();
        int height = yourSlectedImage.getHeight();
        int newWidth = 400;
        int newHeight = 350;

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(yourSlectedImage, 0, 0,
                width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return new BitmapDrawable(resizedBitmap);
    }

    private void iniciarFecha(View view) {
        textFecha = (TextView) view.findViewById(R.id.et_fecNacimiento);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        textFecha.setText(format.format(c.getTime()));

        textFecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DateDialog().show(getFragmentManager(), "DatePickerInFull");
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fullscreen_dialog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;
            case R.id.action_save:
                // procesarGuardar()
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Actualiza la fecha del view {@code fecha_text}
     * @param year Nuevo Año
     * @param monthOfYear Nuevo Mes
     * @param dayOfMonth Nuevo día
     */
    public void setDateView(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        textFecha.setText(format.format(c.getTime()));
    }



}