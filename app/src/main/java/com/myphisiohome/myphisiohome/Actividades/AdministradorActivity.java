package com.myphisiohome.myphisiohome.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.myphisiohome.myphisiohome.AsyncTask.AdministradorLoginTask;
import com.myphisiohome.myphisiohome.BBDD.MyPhisioBBDDHelper;
import com.myphisiohome.myphisiohome.Fragmentos.FragmentEjercicios;
import com.myphisiohome.myphisiohome.Fragmentos.FragmentPacientes;
import com.myphisiohome.myphisiohome.Fragmentos.FragmentPlanes;
import com.myphisiohome.myphisiohome.R;
import com.myphisiohome.myphisiohome.prefs.SessionPrefs;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdministradorActivity extends AppCompatActivity {

    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;

    private TextView email;
    private TextView nombre;
    private CircleImageView imagenCircle;
    private MyPhisioBBDDHelper bdHelper;
    private AdministradorLoginTask administradorLoginTask=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
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
        email.setText("MyPhisio@Home");
        nombre.setText("Juan Jose Moreno");
        imagenCircle.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.imgs_juanjo));
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
        Bundle arg=new Bundle();
        arg.putInt("aux",1);
        switch (itemDrawer.getItemId()) {
            case R.id.nav_paciente:

                fragmentoGenerico = new FragmentPacientes();
                break;
            case R.id.nav_planes:
                fragmentoGenerico = new FragmentPlanes();
                fragmentoGenerico.setArguments(arg);
                break;

            case R.id.nav_ejercicio:
                fragmentoGenerico = new FragmentEjercicios();

                break;

            case R.id.nav_log_out:
                SessionPrefs.get(AdministradorActivity.this).logOut();
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

}
