package com.example.evcarcompare2;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class CalculatorFragment extends Fragment {

    Spinner termSpinner;
    EditText enterCarPrice, enterDownPayment, enterAPR;
    TextView disMonthlyPrice,carPriceTxt;
    Button affordabilityBtn, paymentsBtn;
    int carCost, downPayment, term, carPrice;
    double interestRate, monthlyCost, totalCost;
    boolean isPayments = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);
        termSpinner = v.findViewById(R.id.termSpinner);
        enterCarPrice = v.findViewById(R.id.enterCarPrice);
        enterDownPayment = v.findViewById(R.id.enterDownPayment);
        enterAPR = v.findViewById(R.id.enterAPR);
        disMonthlyPrice = v.findViewById(R.id.disMonthlyPrice);
        carPriceTxt = v.findViewById(R.id.carPriceTxt);
        affordabilityBtn = v.findViewById(R.id.affordabilityBtn);
        paymentsBtn = v.findViewById(R.id.paymentsBtn);

        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.terms));

        termSpinner.setAdapter(termAdapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.changeTitle(R.layout.toolbar_calculator_layout);

        affordabilityBtn.setBackgroundColor(Color.WHITE);
        affordabilityBtn.setTextColor(Color.BLACK);

        enterAPR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isPayments) {
                    calculateMonthlyCost();
                }
                else {
                    calculateCarCost();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTerm = parent.getItemAtPosition(position).toString();

                if(selectedTerm.equals("12 months")){
                    term = 12;
                }
                if(selectedTerm.equals("24 months")){
                    term = 24;
                }
                if(selectedTerm.equals("36 months")){
                    term = 36;
                }
                if(selectedTerm.equals("48 months")){
                    term = 48;
                }
                if(selectedTerm.equals("60 months")){
                    term = 60;
                }
                if(isPayments) {
                    calculateMonthlyCost();
                }
                else {
                    calculateCarCost();
                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        affordabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carPriceTxt.setText("Monthly Payment");
                disMonthlyPrice.setText("Estimated Car Price: $" + carPrice);
                isPayments = false;


                paymentsBtn.setBackgroundColor(Color.WHITE);
                paymentsBtn.setTextColor(Color.BLACK);

                affordabilityBtn.setBackgroundColor(Color.parseColor("#236477"));
                affordabilityBtn.setTextColor(Color.WHITE);
                calculateCarCost();
            }
        });

        paymentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carPriceTxt.setText("Car Price");
                disMonthlyPrice.setText("Estimated Monthly Price: $" + monthlyCost);
                isPayments = true;

                affordabilityBtn.setBackgroundColor(Color.WHITE);
                affordabilityBtn.setTextColor(Color.BLACK);

                paymentsBtn.setBackgroundColor(Color.parseColor("#236477"));
                paymentsBtn.setTextColor(Color.WHITE);

                calculateMonthlyCost();
            }
        });



        return v;
    }


    public void calculateMonthlyCost(){

        if(enterCarPrice.getText().toString().equals("")){
            carCost = 0;
        }
        else{
            carCost = Integer.parseInt(enterCarPrice.getText().toString());
        }

        if(enterDownPayment.getText().toString().equals("")){
            downPayment = 0;
        }
        else{
            downPayment = Integer.parseInt(enterDownPayment.getText().toString());
        }

        if(enterAPR.getText().toString().equals("") || enterAPR.getText().toString().equals(".") ){
            interestRate = 0;
        }
        else{
            interestRate = (Double.parseDouble(enterAPR.getText().toString())) / 100.0;
        }


//        carCost = 32000;
//        downPayment = 4500;
//        interestRate = .049;
//        term = 60;

        monthlyCost = ( (carCost - downPayment) * (interestRate / 12) ) / (1 - Math.pow(1 + (interestRate / 12), -1 * term));
        monthlyCost = Math.round(monthlyCost * 100.0) / 100.0;

        totalCost =  ( Math.round(monthlyCost * term * 100.0) / 100.0) + downPayment;


        disMonthlyPrice.setText("Estimated Monthly Price: $" + formatNum(monthlyCost));


        System.out.println(monthlyCost);
        System.out.println(totalCost);

    }

    public void calculateCarCost(){

        if(enterCarPrice.getText().toString().equals("")){
            monthlyCost = 0;
        }
        else{
            monthlyCost = Integer.parseInt(enterCarPrice.getText().toString());
        }

        if(enterDownPayment.getText().toString().equals("")){
            downPayment = 0;
        }
        else{
            downPayment = Integer.parseInt(enterDownPayment.getText().toString());
        }

        if(enterAPR.getText().toString().equals("") || enterAPR.getText().toString().equals(".") ){
            interestRate = 0;
        }
        else{
            interestRate = (Double.parseDouble(enterAPR.getText().toString())) / 100.0;
        }


//        carCost = 32000;
//        downPayment = 4500;
//        interestRate = .049;
//        term = 60;

        carCost = (int)( ( ( (1 - Math.pow(1 + (interestRate / 12), -1 * term)) * monthlyCost ) / (interestRate / 12) ) + downPayment );


        disMonthlyPrice.setText("Estimated Car Price: $" + formatNum(Double.valueOf(carCost)));


        System.out.println(monthlyCost);

    }

    public String formatNum(Double d){
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
        return decimalFormat.format(d);
    }


}


