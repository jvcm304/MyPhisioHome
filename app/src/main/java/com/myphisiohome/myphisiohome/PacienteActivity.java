package com.myphisiohome.myphisiohome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import android.widget.Toast;

import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Clases.Paciente;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class PacienteActivity extends AppCompatActivity {

    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;

    private TextView email;
    private TextView nombre;
    private CircleImageView imagenCircle;
    private String urlImagenes="http://myphisio.digitalpower.es/imagenes/";
    private MyPhisioBBDDHelper pacienteBBDDHelper;


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
        nombre.setText(prefs.getString("PREF_PACIENTE_NOMBRE","")+" "+prefs.getString("PREF_PACIENTE_APELLIDOS",""));
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
            getMenuInflater().inflate(R.menu.nav_menu, menu);
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
                fragmentoGenerico = new FragmentPlanes();
                break;
            /*case R.id.item_cuenta:
                fragmentoGenerico = new FragmentoCuenta();
                break;
            case R.id.item_categorias:
                fragmentoGenerico = new FragmentoCategorias();
                break;*/
            case R.id.nav_log_out:
                SessionPrefs.get(PacienteActivity.this).logOut();
                pacienteBBDDHelper= new MyPhisioBBDDHelper(getApplicationContext());
                pacienteBBDDHelper.loogOut();
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


}
