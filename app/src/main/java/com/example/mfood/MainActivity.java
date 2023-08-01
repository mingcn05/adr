package com.example.mfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    protected FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home");
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                new HomeFragment()).commit();

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseAuth.signOut();
                finish();
                break;
            case R.id.nav_search:
                Intent intent=new Intent(MainActivity.this,
                        SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_cart:
                Intent i0=new Intent(MainActivity.this,
                        CartActivity.class);
                startActivity(i0);
                break;

            case R.id.nav_add:
                Intent i1=new Intent(MainActivity.this,
                        NewFoodActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_orderStatus:
                Intent i2=new Intent(MainActivity.this,
                        OrderStatusActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_about:

                break;
            case R.id.nav_feedback:

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}