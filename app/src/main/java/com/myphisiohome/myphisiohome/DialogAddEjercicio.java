package com.myphisiohome.myphisiohome;

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

import com.myphisiohome.myphisiohome.AsyncTask.AddEjercicioServidor;
import com.myphisiohome.myphisiohome.AsyncTask.AddPacienteServidor;
import com.myphisiohome.myphisiohome.AsyncTask.SubirArchivoHttp;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Ejercicio;
import com.myphisiohome.myphisiohome.Clases.Paciente;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Vicente on 12/6/17.
 */

public class DialogAddEjercicio extends DialogFragment {

    private FloatingActionButton mSaveButton;
    private TextView mNombre;
    private TextView mCategoria;
    private TextView mDescripcion;
    private TextView mRecomendaciones;
    private Spinner mTipo;
    private String tipoSpinner;
    private int tipo;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private ImageButton mImageButton;
    private int idEjercicio;
    private String filePath;
    private String image;
    private MyPhisioBBDDHelper myPhisioBBDDHelper;
    private Drawable d;
    private static final int SELECTED_PICTURE=1;
    private SharedPreferences prefs;
    public DialogAddEjercicio() {
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
        View view = inflater.inflate(R.layout.dialog_add_ejercicio, container, false);

        mSaveButton = (FloatingActionButton) view.findViewById(R.id.et_fab);
        mImageButton=(ImageButton) view.findViewById(R.id.et_image);
        mNombre = (TextView) view.findViewById(R.id.et_name);
        mCategoria = (TextView) view.findViewById(R.id.et_categoria);
        mDescripcion = (TextView) view.findViewById(R.id.et_descripcion);
        mRecomendaciones = (TextView) view.findViewById(R.id.et_tips);
        mTipo = (Spinner) view.findViewById(R.id.et_tipo);

        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.default_ejercicio);
        mImageButton.setImageBitmap(bitmap);
        mTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoSpinner= (String) adapterView.getItemAtPosition(i);
                tipoSpinner= (String) adapterView.getItemAtPosition(i);
                if(tipoSpinner.equals("Tiempo")){
                    tipo=1;
                }
                if(tipoSpinner.equals("Repeticiones")){
                    tipo=2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tipoSpinner="Tiempo";
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
                    Toast.makeText(getActivity(),"Ejercicio guardado correctamente",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), AdministradorActivity.class));

                }


            }
        });
        return view;
    }

    private boolean checkDatos() {
        boolean result=true;
        if(mNombre.getText().toString().isEmpty() || mDescripcion.getText().toString().isEmpty() ||
            mCategoria.getText().toString().isEmpty() || mRecomendaciones.getText().toString().isEmpty() ||
            tipoSpinner.isEmpty()){
            Toast.makeText(getActivity(),"Deben estar todos los campos rellenos",Toast.LENGTH_LONG).show();
            result=false;
            return false;
        }

        return result;
    }

    public long addPaciente(){
        if (filePath==null){
            image="default_ejercicio.jpg";
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
        Ejercicio ejercicio=new Ejercicio(Integer.valueOf(calen),mNombre.getText().toString(),mDescripcion.getText().toString(),
                mRecomendaciones.getText().toString(),mCategoria.getText().toString(),
                image,tipo);


        long result= myPhisioBBDDHelper.saveEjercicio(ejercicio);
        Log.e("idEjercicio--->",calen);

        AddEjercicioServidor addEjercicioServidor=new AddEjercicioServidor(ejercicio,getActivity());
        addEjercicioServidor.execute();

        return result;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        DialogAddEjercicio.super.onActivityResult(requestCode,resultCode,data);

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


}