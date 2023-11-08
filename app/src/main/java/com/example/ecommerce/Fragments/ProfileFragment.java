package com.example.ecommerce.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.FragmentProfileBinding;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.example.ecommerce.Models.User;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private TextView nameTextView;
    private TextView ageTextView;
    private TextView vehicleTypeView;
    private TextView vehicleNameView;
    private TextView phoneNumberView;
    private DatabaseReference databaseRef;
    FragmentProfileBinding binding;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        String phoneNumber = getActivity().getIntent().getStringExtra("phoneString");
        Log.d("Phone Number",phoneNumber);
        nameTextView = view.findViewById(R.id.name_view);
        ageTextView = view.findViewById(R.id.age_view);
        vehicleTypeView = view.findViewById(R.id.vehicle_type_view);
        vehicleNameView = view.findViewById(R.id.vehicle_name_view);
        phoneNumberView = view.findViewById(R.id.phone_number_view);

        databaseRef = FirebaseDatabase.getInstance().getReference("phoneNumber").child(phoneNumber);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange","Khong the doc Data");
                if (dataSnapshot.exists())
                {
                    Log.d("onDataChange","Khong the doc Data 2");
                    if (dataSnapshot.child("phoneNumber").exists())
                    {
                        String age = dataSnapshot.child("age").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String vehicleType = dataSnapshot.child("vehicleType").getValue().toString();
                        String vehicleName = dataSnapshot.child("vehicleName").getValue().toString();


                        nameTextView.setText(name);
                        ageTextView.setText(age);
                        vehicleNameView.setText(vehicleName);
                        vehicleNameView.setText(vehicleType);
                        phoneNumberView.setText(phoneNumber);
                    }
                    Log.d("onDataChange","Khong the doc Data 3");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        return view;
    }
}