package com.example.evcarcompare2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CarList extends AppCompatActivity {


    String makeFilter, modelFilter, yearFilter, priceFilterString;
    Integer priceFilter;
    ArrayList<Car> compareCars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_car_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);


        makeFilter = getIntent().getStringExtra("keymake");
        modelFilter = getIntent().getStringExtra("keymodel");
        yearFilter = getIntent().getStringExtra("keyyear");
        priceFilterString = getIntent().getStringExtra("keymaxprice");
        Bundle bundle = getIntent().getExtras();
        compareCars = (ArrayList<Car>) bundle.getSerializable("keycars");


        for (int i = 0; i < priceFilterString.length(); i++) {
            if (priceFilterString.charAt(i) == ',') {
                priceFilterString = priceFilterString.replace(",", "");

            }
            if (priceFilterString.charAt(i) == '$') {
                priceFilterString = priceFilterString.replace("$", "");

            }
        }

        priceFilter = Integer.parseInt(priceFilterString);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SearchFragment()).commit();





    }

    public ArrayList<Car> getCompareCars(){
        return compareCars;
    }

    public String getMakeFilter() {
        return makeFilter;
    }

    public String getModelFilter() {
        return modelFilter;
    }

    public String getYearFilter() {
        return yearFilter;
    }

    public int getPriceFilter() {
        return priceFilter;
    }

    public void changeTitle(int id){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(id);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case (R.id.nav_search):
                            selectedFragment = new SearchFragment();
                            break;
                        case (R.id.nav_compare):
                            selectedFragment = new CompareFragment();
                            break;


                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

}