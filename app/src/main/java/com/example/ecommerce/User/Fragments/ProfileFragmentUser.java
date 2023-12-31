package com.example.ecommerce.User.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.Bill;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.SplashActivity;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.example.ecommerce.User.Activities.FaqActivityUser;
import com.example.ecommerce.User.Activities.RegisterDriverActivityUser;
import com.example.ecommerce.User.Activities.SavedLocationActivityUser;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.LoginActivity;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class ProfileFragmentUser extends Fragment {

    TextView tv_fullname;
    MaterialButton editProfile, savedLocation, faq, registerDriver, logout;
    String phone, name;
    public static final int SIGN_UP_REQUEST = 1;
    public static final int FAILED_SIGN_UP_REQUEST = -1;

    private DatabaseReference driversRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_fullname = rootView.findViewById(R.id.tv_fullname);
        editProfile = rootView.findViewById(R.id.editProfile_btn);
        savedLocation = rootView.findViewById(R.id.savedLocation_btn);
        faq = rootView.findViewById(R.id.faq_btn);
        registerDriver = rootView.findViewById(R.id.registerDriver_btn);
        logout = rootView.findViewById(R.id.log_out_btn);
        phone = getArguments().getString("phone_number");
        name = getArguments().getString("user_name");
        tv_fullname.setText(name);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        driversRef = database.getReference("DriversInfo");

        initButton(rootView);

        return rootView;
    }


    private void initButton(View rootView) {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), EditProfileActivityUser.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });

        savedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), SavedLocationActivityUser.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), FaqActivityUser.class);
                startActivity(intent);
            }
        });

        registerDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(rootView.getContext(), RegisterDriverActivityUser.class);
                signUpIntent.putExtra("PHONE_KEY", phone);
                signUpIntent.putExtra("NAME_KEY", name);
                startActivityForResult(signUpIntent, SIGN_UP_REQUEST);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), SplashActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        IsDriver(phone);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show();
                registerDriver.setEnabled(false);
                registerDriver.setText("Your Request Has Been Sented!");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle registration failure
                if (data != null && data.hasExtra("ERROR_CODE")) {
                    String errorInfo = data.getStringExtra("ERROR_INFO");
                    Toast.makeText(getContext(), errorInfo, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void IsDriver(String userPhone) {

        // Retrieve the reference to the specific driver
        Query query = driversRef.orderByChild("phoneNo").equalTo(userPhone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the driver exists in the database
                if (dataSnapshot.exists()) {
                    registerDriver.setEnabled(false);
                    registerDriver.setText("You Already Registered");
                } else {
                    registerDriver.setEnabled(true);
                    registerDriver.setText("Register Driver");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}