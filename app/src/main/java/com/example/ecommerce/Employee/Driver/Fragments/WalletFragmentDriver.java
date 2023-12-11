package com.example.ecommerce.Employee.Driver.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WalletFragmentDriver extends Fragment {

    View rootView;
    TextView tv_wallet_balance;
    DatabaseReference walletRef;
    String key_driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_wallet_driver, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        key_driver = preferences.getString("driver_number", "");
        tv_wallet_balance = rootView.findViewById(R.id.tv_wallet_balance);
        loadWalletBalance();


        return rootView;
    }

    private void loadWalletBalance() {
        walletRef = FirebaseDatabase.getInstance().getReference("DriversInfo").child(key_driver);
        walletRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String driverBalance = snapshot.child("balance").getValue().toString();
                    tv_wallet_balance.setText(driverBalance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}