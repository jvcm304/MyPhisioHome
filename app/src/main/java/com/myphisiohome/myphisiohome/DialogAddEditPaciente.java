package com.myphisiohome.myphisiohome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Paciente;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vicente on 12/6/17.
 */

public class DialogAddEditPaciente extends DialogFragment {

    private FloatingActionButton mSaveButton;
    private TextView textFecha;
    private TextView mNombre;
    private TextView mEmail;
    private TextView mEstatura;
    private TextView mPeso;
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
    public DialogAddEditPaciente() {
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
        View view = inflater.inflate(R.layout.dialog_modificar_paciente, container, false);
        iniciarFecha(view);// Setear fecha inicial
        mSaveButton = (FloatingActionButton) view.findViewById(R.id.et_fab);
        mImageButton=(ImageButton) view.findViewById(R.id.et_image);
        mNombre = (TextView) view.findViewById(R.id.et_name);
        mEmail = (TextView) view.findViewById(R.id.et_email);
        mEstatura = (TextView) view.findViewById(R.id.et_estatura);
        mPeso = (TextView) view.findViewById(R.id.et_peso);
        mSexo = (Spinner) view.findViewById(R.id.et_sexo);
        SharedPreferences prefs = getActivity().getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);
        mEmail.setText(prefs.getString("PREF_PACIENTE_EMAIL",""));
        mNombre.setText(prefs.getString("PREF_PACIENTE_NAME",""));
        mEstatura.setText(Integer.toString(prefs.getInt("PREF_PACIENTE_ESTATURA",0)));
        idPaciente=prefs.getInt("PREF_PACIENTE_ID",-1);
        mPeso.setText((prefs.getString("PREF_PACIENTE_PESO","")));
        filePath=prefs.getString("PREF_PACIENTE_IMAGE","vicente.png");
        image =prefs.getString("PREF_PACIENTE_IMAGE","vicente.png");
        Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.myphisiohome.myphisiohome/app_fotoPerfil/foto.png");
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

                int result=modificarPaciente();
                Log.e("resultado----",Integer.toString(result));
                startActivity(new Intent(getActivity(), PrincipalActivity.class));

            }
        });
        return view;
    }
    public int modificarPaciente(){
        File file = new File(filePath);
        String nImage=image;
        if(!file.getName().equals(image)){
            //subir imagen a servidor
            nImage=file.getName();
            SubirArchivoHttp subirArchivoHttp =new SubirArchivoHttp(filePath);
            subirArchivoHttp.execute();
        }

        myPhisioBBDDHelper= new MyPhisioBBDDHelper(getContext());
        Paciente paciente=new Paciente(idPaciente,mNombre.getText().toString(), mEmail.getText().toString(),
                nImage,textFecha.getText().toString(),mPeso.getText().toString(),
                Integer.valueOf(mEstatura.getText().toString()),mSexo.getSelectedItem().toString() );
        int result= myPhisioBBDDHelper.updatePaciente2(paciente, this.idPaciente);
        UpdatePacienteServidor updatePacienteServidor=new UpdatePacienteServidor(paciente,getActivity());
        updatePacienteServidor.execute();

        SessionPrefs.get(getContext()).savePaciente(paciente);
        return result;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        DialogAddEditPaciente.super.onActivityResult(requestCode,resultCode,data);

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