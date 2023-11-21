package com.example.ecommerce.Employee.Driver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.Employee.Driver.Activities.ReceiveOrderActivityDriver;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragmentDriver extends Fragment {

    ImageView driver_available, driver_unavailable;
    TextView status;
    String key_driver="0";
    View showSnackBarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_driver, container, false);
        //key_driver = getArguments().getString("key_driver");
        driver_available = rootView.findViewById(R.id.img_connect);
        driver_unavailable = rootView.findViewById(R.id.disconnect);
        showSnackBarView = rootView.findViewById(android.R.id.content);
        status = rootView.findViewById(R.id.status_driver);
        driver_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ReceiveOrderActivityDriver.class);
                intent.putExtra("key_driver", key_driver);
                startActivity(intent);
            }
        });

        driver_unavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ReceiveOrderActivityDriver.class);
                intent.putExtra("key_driver", key_driver);
                startActivity(intent);
            }
        });
        return rootView;
    }
    private void setDriver_available() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        UsersRef.child("driverStatus").setValue("Unavailable").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Change status successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to change status", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void setDriver_unavailable() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(key_driver);

        UsersRef.child("Status").setValue("Accept").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Accept confirmed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to accept", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }



}