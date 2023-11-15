package com.example.ecommerce.User.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;

import java.util.Calendar;

public class CaculateMoneyActivity extends AppCompatActivity {
    private Button motoBike,Car;
    private TextView distanceShow,moneyShow;
    private  double distanceToCaculate;
    private static final int BASE_PRICE_A2 = 20000; // Base price for Hạng A2
    private static final int BASE_PRICE_B2 = 30000; // Base price for Hạng B2
    private static final int PRICE_PER_KM_A2 = 11000; // Price per km for Hạng A2
    private static final int PRICE_PER_KM_B2 = 15000; // Price per km for Hạng B2
    private static final int NIGHT_FEE_21_23 = 6000; // Night fee from 21h to 23h
    private static final int NIGHT_FEE_23_1 = 10000; // Night fee from 23h to 1h
    private static final int NIGHT_FEE_1_4 = 15000; // Night fee from 1h to 4h

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caculate_money);
        motoBike=findViewById(R.id.motomoney);
        Car=findViewById(R.id.carmoney);
        distanceShow=findViewById(R.id.showDistance);
        moneyShow=findViewById(R.id.showMoney);
        Intent intent=getIntent();
        if(intent!=null)
        {
            distanceToCaculate=intent.getDoubleExtra("Distance",0.0);
            distanceShow.setText(String.valueOf(distanceToCaculate));
        }
        getMotoBikePrice();
        getCarPrice();


    }
    public static int calculateRidePrice(String vehicleCategory, double distance) {
        int basePrice;
        int pricePerKm;

        // Set base price and price per km based on vehicle category
        if (vehicleCategory.equals("A2")) {
            basePrice = BASE_PRICE_A2;
            pricePerKm = PRICE_PER_KM_A2;
        } else if (vehicleCategory.equals("B2")) {
            basePrice = BASE_PRICE_B2;
            pricePerKm = PRICE_PER_KM_B2;
        } else {
            // Invalid category, return an error value or handle accordingly
            return -1;
        }
        // Calculate distance-based price
        int distancePrice = (int) (distance * pricePerKm);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Calculate night fee based on the hour
        int nightFee = 0;
        if (currentHour >= 21 && currentHour < 23) {
            nightFee = NIGHT_FEE_21_23;
        } else if (currentHour >= 23 && currentHour < 1) {
            nightFee = NIGHT_FEE_23_1;
        } else if (currentHour >= 1 && currentHour < 4) {
            nightFee = NIGHT_FEE_1_4;
        }

        // Calculate the total price
        int totalPrice = basePrice + distancePrice + nightFee;

        return totalPrice;
    }
    private void getMotoBikePrice()
    {
        motoBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vehicleCategory="A2";
                String price= String.valueOf(calculateRidePrice(vehicleCategory,distanceToCaculate));
                moneyShow.setText(price);
            }
        });
    }
    private void getCarPrice()
    {
        Car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vehicleCategory="B2";
                String price= String.valueOf(calculateRidePrice(vehicleCategory,distanceToCaculate));
                moneyShow.setText(price);
            }
        });
    }


}