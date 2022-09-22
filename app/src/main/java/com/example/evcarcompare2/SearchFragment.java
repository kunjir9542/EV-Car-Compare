package com.example.evcarcompare2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements ContactAdapter.RecyclerViewClickListener {

    ArrayList<Car> compareCarList = new ArrayList<>();
    ArrayList<Car> allCars = new ArrayList<>();
    ArrayList<Car> allSelectedCars = new ArrayList<>();
    String makeFilter, modelFilter, yearFilter, priceFilterString;
    Integer priceFilter;

    RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    ContactAdapter.RecyclerViewClickListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);

        CarList carList = (CarList) getActivity();

        carList.changeTitle(R.layout.toolbar_search_layout);

        makeFilter = carList.getMakeFilter();
        modelFilter = carList.getModelFilter();
        yearFilter = carList.getYearFilter();
        priceFilter = carList.getPriceFilter();
        compareCarList = carList.getCompareCars();

        addCarsToAllCars();
        setAdapter();
        assignBitmaps();
        //filterCar();

        //System.out.println(allCars.get(0).getBitmap());

        return v;
    }



    public void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        contactAdapter = new ContactAdapter(getContext(), allSelectedCars, this);
        recyclerView.setAdapter(contactAdapter);


    }

    public void filterCar(){
        String compareMake = "";
        String compareModel = "";
        String compareYear = "";

        for(int i = 0; i < allCars.size(); i++){
            if(makeFilter.equals("All Makes")){
                compareMake = allCars.get(i).getMake();
            }
            else {
                compareMake = makeFilter;
            }

            if(modelFilter.equals("All Models")){
                compareModel = allCars.get(i).getModel();
            }
            else {
                compareModel = modelFilter.replace(compareMake + " ", "");

            }

            if(yearFilter.equals("All Years")){
                compareYear = Integer.toString(allCars.get(i).getYear());
            }
            else{
                compareYear = yearFilter;
            }




            if(compareMake.equals(allCars.get(i).getMake()) && compareModel.equals(allCars.get(i).getModel()) && compareYear.equals(Integer.toString( allCars.get(i).getYear()) ) && priceFilter > allCars.get(i).getPrice()   ){
                allSelectedCars.add(allCars.get(i));
            }


        }

    }

    public void addCarsToAllCars(){


        allCars.add(new Car("2021 Tesla Model 3", "Tesla", "Model 3", 2021, 44990, "$44,990 - $58,990", new String[]{"Long Range", "Performance", "Standard Range Plus"},"https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/10f6e7ef-24cb-4344-9ea0-6efa4252d357/5e0ccefb-b0b3-4e93-80aa-55cbed3f8cbe.png", 449, 358, 5));
        allCars.add(new Car("2021 Tesla Model S", "Tesla", "Model S", 2021, 69420, "$69,420 - $129,990", new String[]{"Long Range Plus", "Plaid"} ,"https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/667b4e31-d10d-4509-bc02-a593f5df908e/f041ba64-318c-4dd5-9a5f-b3daffa0a743.png", 534, 402, 5));
        allCars.add(new Car("2021 Tesla Model X", "Tesla", "Model X", 2021, 79990, "$79,990 - $119,990", new String[]{"Long Range Plus", "Plaid"} ,"https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/cd5a4bc1-721c-4176-8a31-72d27ec08eb6/0b6b80c3-2aa2-4ff5-aed8-f143ef460b39.png", 534, 371, 5));
        allCars.add(new Car("2021 Tesla Model Y", "Tesla", "Model Y", 2021, 39990, "$39,990 - $63,990", new String[]{"Long Range", "Performance", "Standard Range"}, "https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/a457eac7-0944-4072-a7b7-4d99283b0373/58d2e744-1797-4c7c-875d-5fed4aca190d.png", 384, 330, 5));

        allCars.add(new Car("2021 Porsche Taycan Cross Turismo", "Porsche" , "Taycan Cross Turismo", 2021, 90900, "$90,900 - $187,600", new String[]{"4", "4S", "Turbo", "Turbo S"} ,"https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/39bc4b32-09ca-43ff-8850-7f15d87440cd/97d70df4-2ccf-41e9-8c98-867ce6d50c2b.png", 469, 215, 5));

        allCars.add(new Car("2021 Audi E-Tron", "Audi", "E-Tron", 2021, 65900, "$65,900 - $69,100", new String[]{"Premium", "Prestige"}, "https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/2035031f-f6d7-4b04-937e-49f8e866d114/13ffa2c4-42cd-4756-8182-48ac17de9e43.png", 355, 222, 5));
        allCars.add(new Car("2021 Audi E-Tron GT", "Audi", "E-Tron GT", 2021, 102400, "$102,400 - $142,400", new String[]{"E-Tron GT", "RS E-Tron GT"}, "https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/b2a647b8-8b5d-4ca8-bc08-71c93e113b98/cc514430-3cf9-4bce-9523-9c01d130cd50.png", 522, 249, 5));

        allCars.add(new Car("2021 BMW i3", "BMW", "i3", 2021, 44450, "$44,450 - $51,500", new String[]{"120 Ah", "120 Ah s", "120 Ah Range Extender", "120 Ah s Range Extender"}, "https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/bc5d4734-5bf0-4fda-acf3-265023f70310/bcea62da-664e-47ba-8b0a-fe618ea07a47.png", 170, 153, 4));
        allCars.add(new Car("2022 BMW i4 Grand Coupe", "BMW", "i4 Grand Coupe", 2022, 55400, "$55,400 - $65,900", new String[]{"eDrive 40", "M50"}, "https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/c5a0d13b-af67-48d1-a674-8381f0c819a2/4e675db1-d900-4023-adb6-02a49adcbf10.png", 335, 301, 5));
        allCars.add(new Car("2022 BMW iX", "BMW", "iX", 2022, 83200, "$83200", new String[]{"xDrive 50"}, "https://platform.cstatic-images.com/xlarge/in/v2/stock_photos/aa289914-9340-4a65-85de-e975129a2bcb/16d532bf-6a36-4b57-995c-c2a5cdb45be5.png", 516, 324, 5));



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
                filterCar();
                setAdapter();
            }
        }


            ImageLoadTask obj = new ImageLoadTask(allCars);
            obj.execute();
    }


    @Override
    public void onClick(int position) {
        Car chosenCar = allSelectedCars.get(position);

        Intent intent = new Intent(getContext(), CarDescription.class);

        intent.putExtra("keymakefilter", makeFilter);
        intent.putExtra("keymodelfilter", modelFilter);
        intent.putExtra("keyyearfilter", yearFilter);
        intent.putExtra("keypricefilterstring", Integer.toString(priceFilter));
        intent.putExtra("keychosencar", chosenCar);
        Bundle bundle = new Bundle();
        bundle.putSerializable("keycars", compareCarList);
        intent.putExtras(bundle);


        startActivity(intent);
    }
}
