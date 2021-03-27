package com.example.modawana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.modawana.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

   public BottomNavigationView.OnNavigationItemSelectedListener m = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment=null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    selectedfragment =new Main();break;
                case R.id.navigation_dashboard:
                    selectedfragment =new grammer_list();break;
                case R.id.navigation_notifications:
                    selectedfragment =new Words();break;
                case R.id.navigation_home2:
                    selectedfragment =new Profile();break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,selectedfragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Main()).commit();
            BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view);
            navView.setOnNavigationItemSelectedListener(m);

    }


}