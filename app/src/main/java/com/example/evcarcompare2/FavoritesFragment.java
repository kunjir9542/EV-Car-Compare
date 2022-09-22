package com.example.evcarcompare2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements ContactAdapter.RecyclerViewClickListener {

    RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    ArrayList<Car> favoriteCars = new ArrayList<>();
    Button clearFavoritesBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = v.findViewById(R.id.favoritesRecyclerView);
        clearFavoritesBtn = v.findViewById(R.id.clearFavoritesBtn);
        MainActivity mainActivity = (MainActivity) getActivity();
        favoriteCars = mainActivity.getFavoriteCars();
        mainActivity.changeTitle(R.layout.toolbar_favorites_layout);


        setAdapter();
        assignBitmaps();

        clearFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.clearFavoriteCars();
                favoriteCars.clear();
                Toast.makeText(getContext(), "Cleared favorites!", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }



    public void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        contactAdapter = new ContactAdapter(getContext(), favoriteCars, this);
        recyclerView.setAdapter(contactAdapter);

//        recyclerView.setLayoutManager(new LinearLayoutManager(CarList.this));
//        ContactAdapter adapter = new ContactAdapter(CarList.this, allCars, listener);
//        recyclerView.setAdapter(adapter);
    }

    public void assignBitmaps(){
        class ImageLoadTask extends AsyncTask<Void, Void, ArrayList<Bitmap> > {
            private ArrayList <Car> cars = new ArrayList<>();
            public ImageLoadTask(ArrayList <Car> cars) {
                this.cars = cars;
            }

            @Override
            protected ArrayList<Bitmap> doInBackground(Void... params) {
                ArrayList<Bitmap> bList = new ArrayList<>();
                try {

                    for(Car c : cars) {
                        URL connection = new URL(c.getUrl());
                        InputStream input = connection.openStream();
                        Bitmap b = BitmapFactory.decodeStream(input);
                        Bitmap resized = Bitmap.createScaledBitmap(b, 640, 422, true);
                        bList.add(resized);
                    }
                    return bList;
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Bitmap> result){
                super.onPostExecute(result);
                for(int i = 0; i< cars.size(); i++){
                    cars.get(i).setBitmap(result.get(i));
                }
                setAdapter();
            }
        }


        ImageLoadTask obj = new ImageLoadTask(favoriteCars);
        obj.execute();
    }

    @Override
    public void onClick(int position) {
        Car chosenCar = favoriteCars.get(position);

        Intent intent = new Intent(getContext(), CarDescription.class);
        intent.putExtra("keycarname",chosenCar.getName());
        intent.putExtra("keycarmake",chosenCar.getMake());
        intent.putExtra("keycarmodel",chosenCar.getModel());
        intent.putExtra("keycaryear",Integer.toString(chosenCar.getYear()));
        intent.putExtra("keychosencar", chosenCar);



        startActivity(intent);
    }
}
