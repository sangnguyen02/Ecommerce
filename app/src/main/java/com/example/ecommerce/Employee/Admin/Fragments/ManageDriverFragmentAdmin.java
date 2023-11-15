package com.example.ecommerce.Employee.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Employee.Admin.Activities.ManageDriverDetailActivityAdmin;
import com.example.ecommerce.Employee.Admin.Activities.ManageRegisterDriverActivityAdmin;
import com.example.ecommerce.Models.Driver;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ManageDriverViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ManageDriverFragmentAdmin extends Fragment {

    DatabaseReference DriverRef;
    RecyclerView rcvManageDriver;

    MaterialButton manageRegisterButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_driver_admin, container, false);
        DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo");
        rcvManageDriver = rootView.findViewById(R.id.rcv_manageDriver);
        manageRegisterButton = rootView.findViewById(R.id.manageRegister_btn);


        rcvManageDriver.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(),RecyclerView.VERTICAL,false);
        rcvManageDriver.setLayoutManager(linearLayoutManager);

        manageRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ManageRegisterDriverActivityAdmin.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

       /* FirebaseRecyclerOptions<Driver> options =
                new FirebaseRecyclerOptions.Builder<Driver>()
                        .setQuery(DriverRef, Driver.class)
                        .build();

        FirebaseRecyclerAdapter<Driver, ManageDriverViewHolder> adapter =
                new FirebaseRecyclerAdapter<Driver, ManageDriverViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ManageDriverViewHolder holder, int position, @NonNull Driver model) {
                        Picasso.get().load(model.getImage()).into(holder.imgDriver);
                        holder.tvName.setText(model.getName());
                        holder.tvStatus.setText(model.getStatus());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), ManageDriverDetailActivityAdmin.class);
                                intent.putExtra("pid", model.getId());
                                startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ManageDriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_driver, parent, false);
                        ManageDriverViewHolder holder = new ManageDriverViewHolder(view);
                        return holder;
                    }
                };
        rcvManageDriver.setAdapter(adapter);
        adapter.startListening();*/
    }
}