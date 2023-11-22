package com.example.ecommerce.Employee.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Adapters.DriverAdapter;
import com.example.ecommerce.Employee.Admin.Activities.ManageDriverDetailActivityAdmin;
import com.example.ecommerce.Employee.Admin.Activities.ManageRegisterDriverActivityAdmin;
import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.Driver;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ManageDriverViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ManageDriverFragmentAdmin extends Fragment {

    RecyclerView rcvManageDriver;
    MaterialButton manageRegisterButton;
    private DatabaseReference driversRef;
    private DriverAdapter adapter;
    private List<DriverInfos> driverList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_driver_admin, container, false);
        rcvManageDriver = rootView.findViewById(R.id.rcv_manageDriver);
        rcvManageDriver.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false);
        rcvManageDriver.setLayoutManager(linearLayoutManager);

        driversRef = FirebaseDatabase.getInstance().getReference("DriversInfo");
        driverList = new ArrayList<>();
        loadDriversInfo();

        adapter = new DriverAdapter(driverList, getContext());
        rcvManageDriver.setAdapter(adapter);

        manageRegisterButton = rootView.findViewById(R.id.manageRegister_btn);
        manageRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ManageRegisterDriverActivityAdmin.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void loadDriversInfo() {
        driversRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    DriverInfos driver = driverSnapshot.getValue(DriverInfos.class);
                    if (driver != null && driver.getDriverStatus() != MyEnum.DriverStatus.PENDING) {
                        driverList.add(driver);
                    }
                }

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }
}
