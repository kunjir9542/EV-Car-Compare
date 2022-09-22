package com.example.evcarcompare2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    String selectedMake = "", selectedModel = "", selectedYear = "", selectedMaxPrice = "";
    ArrayList<Car> cars = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Button searchBtn;
        Spinner makeSpinner, modelSpinner, yearSpinner, maxPriceSpinner;

        searchBtn = v.findViewById(R.id.searchBtn);
        makeSpinner = v.findViewById(R.id.make_spinner);
        modelSpinner = v.findViewById(R.id.model_spinner);
        yearSpinner = v.findViewById(R.id.year_spinner);
        maxPriceSpinner = v.findViewById(R.id.price_spinner);

        ArrayAdapter<String> makeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.makes));
        ArrayAdapter<String> allModelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allModels));
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allYears));
        ArrayAdapter<String>  maxPriceAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allMaxPrices));

        ArrayAdapter<String> teslaModelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allTeslaModels));
        ArrayAdapter<String> porscheModelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allPorscheModels));
        ArrayAdapter<String> audiModelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allAudiModels));
        ArrayAdapter<String> bmwModelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.allBMWModels));


        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxPriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        teslaModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        makeSpinner.setAdapter(makeAdapter);
        modelSpinner.setAdapter(allModelAdapter);
        yearSpinner.setAdapter(yearAdapter);
        maxPriceSpinner.setAdapter(maxPriceAdapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.changeTitle(R.layout.toolbar_title_layout);

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMake = parent.getItemAtPosition(position).toString();

                if(selectedMake.equals("Tesla")){
                    modelSpinner.setAdapter(teslaModelAdapter);
                }
                else if(selectedMake.equals("Audi")){
                    modelSpinner.setAdapter(audiModelAdapter);
                }
                else if(selectedMake.equals("BMW")){
                    modelSpinner.setAdapter(bmwModelAdapter);
                }
                else if(selectedMake.equals("Porsche")){
                    modelSpinner.setAdapter(porscheModelAdapter);
                }
                else{
                    modelSpinner.setAdapter(allModelAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        maxPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaxPrice = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), CarList.class);

                if(selectedMaxPrice.equals("No Max Price")){
                    selectedMaxPrice = "1000000";
                }
                if(selectedMaxPrice.equals("$100,000+")){
                    selectedMaxPrice = "1000000";
                }

                System.out.println(selectedMaxPrice);
                intent.putExtra("keymake", selectedMake);
                intent.putExtra("keymodel", selectedModel);
                intent.putExtra("keyyear", selectedYear);
                intent.putExtra("keymaxprice", selectedMaxPrice);
                Bundle bundle = new Bundle();
                bundle.putSerializable("keycars", cars);
                intent.putExtras(bundle);
                startActivity(intent);

//                Toast.makeText(getApplicationContext(), selectedMake, Toast.LENGTH_LONG).show();
            }
        });


        return v;
    }
}
