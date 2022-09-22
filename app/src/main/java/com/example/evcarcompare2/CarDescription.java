package com.example.evcarcompare2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarDescription extends AppCompatActivity {

    TextView disCarName, disCarPrice, disCarPower, disCarRange, disCarSeats, disCarDrivetrain, disCarType;
    Spinner trimSpinner;
    String carName, carMake, carModel, carYear;
    String makeFilter = "", modelFilter = "", yearFilter = "", priceFilterString = "";
    ArrayAdapter<String> trimAdapter;
    ToggleButton toggleFavoriteBtn;
    Button addToCompareBtn;

    ImageView disCarImage;

    String s;
    Bitmap carBitmap;

    Car chosenCar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference carRef;

    List<String> trims;
    ArrayList<Car> favoriteCars = new ArrayList<>();
    ArrayList<Car> compareCars = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public void changeTitle(int id){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_description);




        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //editor.remove("favorite list").commit();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_description_layout);


        loadData();

        disCarName = findViewById(R.id.disCarName);
        disCarPrice = findViewById(R.id.disCarPrice);
        disCarPower = findViewById(R.id.disCarPower);
        disCarRange = findViewById(R.id.disCarRange);
        disCarSeats = findViewById(R.id.disCarSeats);
        disCarDrivetrain = findViewById(R.id.disCarDrivetrain);
        disCarType = findViewById(R.id.disCarType);

        disCarImage = findViewById(R.id.disCarImage);

        trimSpinner = findViewById(R.id.trimSpinner);

        toggleFavoriteBtn = findViewById(R.id.toggleFavoriteBtn);

        addToCompareBtn = findViewById(R.id.addToCompareBtn);



        makeFilter = getIntent().getStringExtra("keymakefilter");
        modelFilter = getIntent().getStringExtra("keymodelfilter");
        yearFilter = getIntent().getStringExtra("keyyearfilter");
        priceFilterString = getIntent().getStringExtra("keypricefilterstring");

        chosenCar = (Car)getIntent().getSerializableExtra("keychosencar");
        Bundle bundle = getIntent().getExtras();
        compareCars = (ArrayList<Car>) bundle.getSerializable("keycars");

        carName = chosenCar.getName();
        carMake = chosenCar.getMake();
        carModel = chosenCar.getModel();
        carYear = String.valueOf(chosenCar.getYear());

        trimAdapter = new ArrayAdapter<String>(CarDescription.this, android.R.layout.simple_list_item_1, chosenCar.getTrimList());



        setSpinner();
        new doIt().execute();

        //disCarImage.setImageBitmap(chosenCar.getBitmap());

        loadImage(disCarImage);

        if(chosenCar.getFavorited()){
            toggleFavoriteBtn.setChecked(true);
        }
        else{
            toggleFavoriteBtn.setChecked(false);
        }


        addToCompareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(compareCars.size() + 1 > 2){
                    Toast.makeText(CarDescription.this, "There are already 2 cars to compare!", Toast.LENGTH_SHORT).show();

                }
                else {
                    compareCars.add(chosenCar);
                    Intent intent = new Intent(getApplicationContext(), CarList.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("keycars", compareCars);
                    intent.putExtras(bundle);
                    intent.putExtra("keymake", makeFilter);
                    intent.putExtra("keymodel", modelFilter);
                    intent.putExtra("keyyear", yearFilter);
                    intent.putExtra("keymaxprice", priceFilterString);
                    startActivity(intent);
                }
            }
        });

        trimSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTrim = parent.getItemAtPosition(position).toString();

                for(int i = 0; i < trims.size(); i++){
                    if(selectedTrim.equals(trims.get(i))){
                        selectedTrim = trims.get(i);
                        disCarName.setText(carName);
                        carRef = db.document("Car/" + carMake + "/" + carModel + "/" + carYear + "/Trim/" + selectedTrim);

                        chosenCar.setTrim(selectedTrim);

                        // Car/Tesla/Model 3/2021/Trim/Long Range
                    }
                }
                loadCarStats();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleFavoriteBtn.isChecked()){
                    Toast.makeText(CarDescription.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                    favoriteCars.add(chosenCar);
                    chosenCar.setFavorited(true);
//                    disCarName.setText("A " + favoriteCars.size());

                }
                else{
                    Toast.makeText(CarDescription.this, "Removed from favorites!", Toast.LENGTH_SHORT).show();
                    favoriteCars.remove(chosenCar);
                    chosenCar.setFavorited(false);
//                    disCarName.setText("A " + favoriteCars.size());

                }
                saveData();
            }
        });


    }

    public void loadImage(View view){

        class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
            private String url;
            private ImageView imageView;

            public ImageLoadTask(String url, ImageView imageView){
                this.url = url;
                this.imageView = imageView;
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    URL connection = new URL(url);
                    InputStream input = connection.openStream();
                    carBitmap = BitmapFactory.decodeStream(input);
                    Bitmap resized = Bitmap.createScaledBitmap(carBitmap, 640, 422, true);
                    return resized;
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result){
                disCarImage.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }

        String carURL;


        carURL = chosenCar.getUrl();

        ImageLoadTask obj = new ImageLoadTask(carURL, disCarImage);
        obj.execute();
    }


    public void proof(){
        String s = "";
        for(int i = 0; i < favoriteCars.size(); i++){
            s = s + favoriteCars.get(i).getMake() + ", ";
        }
        disCarName.setText(s);
    }

    public void saveData(){
        Gson gson = new Gson();
        String json = gson.toJson(favoriteCars);
        editor.putString("favorite list", json);
        editor.apply();

        MainActivity mainActivity = new MainActivity();
        mainActivity.setFavoriteCars(favoriteCars);
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


    public void setSpinner(){
//        if(carModel.equals("Model X") || carModel.equals("Model S")){
//            trimSpinner.setAdapter(teslaTrimX);
//            trims = Arrays.asList(getResources().getStringArray(R.array.teslaTrimX));
//        }
//        if(carModel.equals("Model Y")){
//            trimSpinner.setAdapter(teslaTrimY);
//            trims = Arrays.asList(getResources().getStringArray(R.array.teslaTrimY));
//        }
//        if(carModel.equals("Model 3")){
//            trimSpinner.setAdapter(teslaTrim3);
//            trims = Arrays.asList(getResources().getStringArray(R.array.teslaTrim3));
//        }

            trimSpinner.setAdapter(trimAdapter);
            trims = Arrays.asList(chosenCar.getTrimList());
    }

    public void loadCarStats(){
        carRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String dt, type;
                            Double msrp, power, range, seats;
                            dt = documentSnapshot.getString("drivetrain");
                            msrp = documentSnapshot.getDouble("msrp");
                            power = documentSnapshot.getDouble("power");
                            range = documentSnapshot.getDouble("range");
                            seats = documentSnapshot.getDouble("seats");
                            type = documentSnapshot.getString("type");


                            disCarDrivetrain.setText("Drivetrain: " + dt);
                            disCarPrice.setText("$ " + formatNum(msrp));
                            disCarPower.setText("Horsepower: " + (int)(Math.round(power)) + " hp");
                            disCarRange.setText("Range: " + (int)(Math.round(range)) + " miles");
                            disCarSeats.setText("Seats: " + (int)(Math.round(seats)) + " seats");
                            disCarType.setText("Type: " + type);
                        }
                        else {
                            Toast.makeText(CarDescription.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CarDescription.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class doIt extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect("https://www.cars.com/research/tesla-model_3-2021/specs/418767/").get();
                Elements price = doc.getElementsByClass("price-amount");

                        s = price.text();
            }
            catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            //disCarName.setText(s);
        }
    }

    public String formatNum(Double d){
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        return decimalFormat.format(d);
    }


 // price-amount
}



