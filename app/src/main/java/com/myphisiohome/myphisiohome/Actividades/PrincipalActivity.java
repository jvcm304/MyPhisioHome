package com.myphisiohome.myphisiohome.Actividades;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Fragmentos.FragmentPaciente;
import com.myphisiohome.myphisiohome.Fragmentos.FragmentPlanesPaciente;
import com.myphisiohome.myphisiohome.R;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;



public class PrincipalActivity extends AppCompatActivity {

    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;

    private TextView email;
    private TextView nombre;
    private CircleImageView imagenCircle;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private MyPhisioBBDDHelper bdHelper;
    private int idPlan;


    /**
     * Titulo inicial del drawer
     */
    private String drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        setToolbar(); // Setear Toolbar como action bar
        setNavigationDrawer();



        //
    }
    private void setNavigationDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        View headerView =navigationView.getHeaderView(0);
        email=(TextView) headerView.findViewById(R.id.email);
        nombre=(TextView) headerView.findViewById(R.id.username);
        imagenCircle=(CircleImageView) headerView.findViewById(R.id.circle_image);
        SharedPreferences prefs = getSharedPreferences("MYPHISIO_PREFS", Context.MODE_PRIVATE);
        email.setText(prefs.getString("PREF_PACIENTE_EMAIL",""));
        nombre.setText(prefs.getString("PREF_PACIENTE_NAME",""));
        String image =prefs.getString("PREF_PACIENTE_IMAGE","vicente.png");
        new DownloadImageTask().execute(urlImagenes+image);
        seleccionarItem(navigationView.getMenu().getItem(0));

    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //getMenuInflater().inflate(R.menu.nav_menu, menu);
            getMenuInflater().inflate(R.menu.menu_actionbar, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.nav_planes:
                fragmentoGenerico = new FragmentPlanesPaciente();
                break;
            case R.id.nav_usuario:
                fragmentoGenerico = new FragmentPaciente();
                break;
            /*case R.id.item_categorias:
                fragmentoGenerico = new FragmentoCategorias();
                break;*/
            case R.id.nav_log_out:
                SessionPrefs.get(PrincipalActivity.this).logOut();
                bdHelper= new MyPhisioBBDDHelper(getApplicationContext());
                bdHelper.loogOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, fragmentoGenerico)
                    .commit();
        }
        setTitle(itemDrawer.getTitle());
    }


    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {

        //final ProgressDialog progressDialog = new ProgressDialog(main.this);

        protected void onPreExecute()
        {
            imagenCircle.setImageDrawable(getResources().getDrawable(R.drawable.cargar2));
        }

        protected Bitmap doInBackground(String... urls)
        {
            Log.d("DEBUG", "drawable");

            return downloadImage(urls[0]);

        }

        protected void onPostExecute(Bitmap imagen)
        {
            //Log.e("Imagen-->", imagen.toString());

            Log.e("Imagen----->:",guardarImagen(getApplicationContext(),"foto",imagen));
            imagenCircle.setImageBitmap(imagen);


        }

        /**
         * Devuelve una imagen desde una URL
         *
         * @return Una imagen
         */
        private Bitmap downloadImage(String imageUrlS)
        {
            try
            {
                URL imageUrl = new URL(imageUrlS);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();

                return BitmapFactory.decodeStream(conn.getInputStream());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("fotoPerfil", Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre + ".png");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }




}
