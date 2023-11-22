package com.example.ecommerce.Employee.Driver.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragmentDriver extends Fragment {
    CardView layoutBottomSheetRequestPopUp;
    BottomSheetBehavior bottomSheetBehaviorRequestPopUp;

    private DatabaseReference databaseReference;

    private String driverID = "+84352419724";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_driver, container, false);
        layoutBottomSheetRequestPopUp = rootView.findViewById(R.id.bottom_sheet_order_pop_up);
        bottomSheetBehaviorRequestPopUp = BottomSheetBehavior.from(layoutBottomSheetRequestPopUp);
        // bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_EXPANDED);
        // bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_COLLAPSED);


        databaseReference = FirebaseDatabase.getInstance().getReference("Order");

        // Add a ValueEventListener to listen for changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method will be called whenever data at the specified location changes
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Access each child node
                    Order dataObject = snapshot.getValue(Order.class);
                    if (dataObject != null && dataObject.getClientNo() == driverID) {
                        Toast.makeText(rootView.getContext(),"requested order",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("MainActivity", "Error: " + databaseError.getMessage());
            }
        });


        return rootView;
    }
}