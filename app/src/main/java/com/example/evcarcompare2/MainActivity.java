package com.example.evcarcompare2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<Car> favoriteCars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();




        loadData();



    }

    public void loadData(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite list", null);
        Type type = new TypeToken<ArrayList<Car>>() {}.getType();
        favoriteCars = gson.fromJson(json, type);

        if(favoriteCars == null){
            favoriteCars = new ArrayList<>();
        }

    }

    public void changeTitle(int id){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(id);
    }


    public ArrayList<Car> getFavoriteCars(){
        return favoriteCars;
    }

    public void setFavoriteCars(ArrayList<Car> c){
        favoriteCars = c;
    }

    public void clearFavoriteCars(){
        editor.remove("favorite list").commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case (R.id.nav_home):
                            selectedFragment = new HomeFragment();
                            break;
                        case (R.id.nav_favorites):
                            selectedFragment = new FavoritesFragment();
                            break;
                        case (R.id.nav_calculate):
                            selectedFragment = new CalculatorFragment();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
