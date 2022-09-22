package com.example.evcarcompare2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CompareFragment extends Fragment {

    TextView disCarCompareName, disCarComparePrice, disCarComparePower, disCarCompareRange, disCarCompareSeats, disCarCompareDrivetrain, disCarCompareType, disCarCompareTrim, disBestCar;
    ImageView disCarCompareImage;


    ArrayList<Car> compareCarList = new ArrayList<>();

    Car car, car2, doppleCar, selectedCar;
    Bitmap carBitmap;



    Button removeCarCompare, carCompareBtn, carCompareBtn2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference carRef;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_compare, container, false);

        CarList carList = (CarList) getActivity();
        carList.changeTitle(R.layout.toolbar_compare_layout);

        disCarCompareName = v.findViewById(R.id.disCarCompareName);
        disCarComparePrice = v.findViewById(R.id.disCarComparePrice);
        disCarComparePower = v.findViewById(R.id.disCarComparePower);
        disCarCompareRange = v.findViewById(R.id.disCarCompareRange);
        disCarCompareSeats = v.findViewById(R.id.disCarCompareSeats);
        disCarCompareDrivetrain = v.findViewById(R.id.disCarCompareDrivetrain);
        disCarCompareType = v.findViewById(R.id.disCarCompareType);
        disCarCompareTrim = v.findViewById(R.id.disCarCompareTrim);
        disBestCar = v.findViewById(R.id.disBestCar);

        disCarCompareImage = v.findViewById(R.id.disCarCompareImage);

        removeCarCompare = v.findViewById(R.id.removeCarCompareBtn);
        carCompareBtn = v.findViewById(R.id.carCompareBtn);
        carCompareBtn2 = v.findViewById(R.id.carCompareBtn2);

        carCompareBtn2.setBackgroundColor(Color.WHITE);
        carCompareBtn2.setTextColor(Color.BLACK);

        compareCarList = carList.getCompareCars();

        enoughCars();
        if(compareCarList.size() >= 1){
            loadImage(disCarCompareImage);
            loadCarStats();
        }

        removeCarCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(compareCarList.size() > 0){
                    compareCarList.remove(car);
                    Toast.makeText(getContext(), "Removed from Compare List!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        carCompareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCar = car;

                carCompareBtn2.setBackgroundColor(Color.WHITE);
                carCompareBtn2.setTextColor(Color.BLACK);

                carCompareBtn.setBackgroundColor(Color.parseColor("#236477"));
                carCompareBtn.setTextColor(Color.WHITE);

                if(compareCarList.size() < 1){
                    Toast.makeText(getContext(), "Missing 2 Cars to Compare!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadImage(disCarCompareImage);
                    loadCarStats();

                }

            }
        });

        carCompareBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCar = car2;

                carCompareBtn.setBackgroundColor(Color.WHITE);
                carCompareBtn.setTextColor(Color.BLACK);

                carCompareBtn2.setBackgroundColor(Color.parseColor("#236477"));
                carCompareBtn2.setTextColor(Color.WHITE);

                if(compareCarList.size() < 1){
                    Toast.makeText(getContext(), "Missing 2 Cars to Compare!", Toast.LENGTH_SHORT).show();
                }
                else if (compareCarList.size() == 1){
                    Toast.makeText(getContext(), "Missing 1 Car to Compare!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadImage(disCarCompareImage);
                    loadCarStats();

                }

            }
        });



        return v;
    }




    public void enoughCars(){
        if(compareCarList.size() < 1){
            Toast.makeText(getContext(), "Missing 2 Cars to Compare!", Toast.LENGTH_SHORT).show();
        }
        else if (compareCarList.size() == 1){
            Toast.makeText(getContext(), "Missing 1 Car to Compare!", Toast.LENGTH_SHORT).show();

            car = compareCarList.get(0);
            selectedCar = car;

        }
        else {


            car = compareCarList.get(0);
            car2 = compareCarList.get(1);
            selectedCar = car;

        }
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
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result){
                disCarCompareImage.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }

        String carURL;


        carURL = selectedCar.getUrl();

        ImageLoadTask obj = new ImageLoadTask(carURL, disCarCompareImage);
        obj.execute();
    }


    public void loadCarStats() {
        carRef = db.document("Car/" + selectedCar.getMake() + "/" + selectedCar.getModel() + "/" + selectedCar.getYear() + "/Trim/" + selectedCar.getTrim());

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

                            disCarCompareName.setText(selectedCar.getName());
                            disCarCompareTrim.setText("Trim: " + selectedCar.getTrim());
                            disCarCompareDrivetrain.setText("Drivetrain: " + dt);
                            disCarComparePrice.setText("$ " + formatNum(msrp));
                            disCarComparePower.setText("Horsepower: " + (int)(Math.round(power)) + " hp");
                            disCarCompareRange.setText("Range: " + (int)(Math.round(range)) + " miles");
                            disCarCompareSeats.setText("Seats: " + (int)(Math.round(seats)) + " seats");
                            disCarCompareType.setText("Type: " + type);

                        }
                        else {
                            Toast.makeText(getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });


        if(compareCarList.size() > 1) {
            compareCars();
        }

    }



    public String formatNum(Double d){
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        return decimalFormat.format(d);
    }


    public void compareCars(){
        int car1Count = 0, car2Count = 0;

        disCarComparePrice.setTextColor(Color.GRAY);
        disCarComparePower.setTextColor(Color.GRAY);
        disCarCompareRange.setTextColor(Color.GRAY);
        disCarCompareSeats.setTextColor(Color.GRAY);


        if(car.getPrice() < car2.getPrice() && selectedCar == car){
            car1Count++;
            disCarComparePrice.setTextColor(Color.parseColor("#138808"));
        }
        else if(car.getPrice() > car2.getPrice() && selectedCar == car2){
            car2Count++;
            disCarComparePrice.setTextColor(Color.parseColor("#138808"));
        }

        if(car.getHorsepower() > car2.getHorsepower() && selectedCar == car){
            car1Count++;
            disCarComparePower.setTextColor(Color.parseColor("#138808"));

        }
        else if(car.getHorsepower() < car2.getHorsepower() && selectedCar == car2){
            car2Count++;
            disCarComparePower.setTextColor(Color.parseColor("#138808"));
        }

        if(car.getRange() > car2.getRange() && selectedCar == car){
            car1Count++;
            disCarCompareRange.setTextColor(Color.parseColor("#138808"));

        }
        else if(car.getRange() < car2.getRange() && selectedCar == car2){
            car2Count++;
            disCarCompareRange.setTextColor(Color.parseColor("#138808"));
        }


        if(car.getSeats() > car2.getSeats() && selectedCar == car){
            car1Count++;
            disCarCompareSeats.setTextColor(Color.parseColor("#138808"));

        }
        else if(car.getSeats() < car2.getSeats() && selectedCar == car2){
            car2Count++;
            disCarCompareSeats.setTextColor(Color.parseColor("#138808"));
        }

            disBestCar.setText("Best Car: " + car.getName());



    }

}


