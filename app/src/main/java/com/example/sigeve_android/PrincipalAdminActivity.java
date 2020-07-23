package com.example.sigeve_android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.sigeve_android.ui.main.SectionsPagerAdapter;

public class PrincipalAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_admin);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch
                int position = tabs.getSelectedTabPosition();

                switch (position){
                    case 0:
                        Intent in1 = new Intent(PrincipalAdminActivity.this,AddUnidadActivity.class);
                        startActivity(in1);
                        break;
                    case 1:
                        Intent in2 = new Intent(PrincipalAdminActivity.this,AddConductorActivity.class);
                        startActivity(in2);
                        break;
                    case 2:
                        Intent in3 = new Intent(PrincipalAdminActivity.this,AddVehiculoActivity.class);
                        startActivity(in3);
                        break;
                    case 3:
                        Intent in4 = new Intent(PrincipalAdminActivity.this,AddUsuarioActivity.class);
                        startActivity(in4);
                        break;
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
}