package com.example.ecommerce.Employee.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ManageDriverViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ManageRegisterDriverActivityAdmin extends AppCompatActivity {

    DatabaseReference DriverRef;
    RecyclerView rcvManageDriver;

    private static final int DRIVER_REGISTER_DETAIL_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_register_driver_admin);

        rcvManageDriver = findViewById(R.id.rcv_manageRegisterDriver);
        rcvManageDriver.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rcvManageDriver.setLayoutManager(linearLayoutManager);

        DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = DriverRef.orderByChild("driverStatus").equalTo("PENDING");
        FirebaseRecyclerOptions<DriverInfos> options =
                new FirebaseRecyclerOptions.Builder<DriverInfos>()
                        .setQuery(query, DriverInfos.class)
                        .build();

        FirebaseRecyclerAdapter<DriverInfos, ManageDriverViewHolder> adapter =
                new FirebaseRecyclerAdapter<DriverInfos, ManageDriverViewHolder>(options) {
                    //View Holder
                    @NonNull
                    @Override
                    public ManageDriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_driver, parent, false);
                        ManageDriverViewHolder holder = new ManageDriverViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ManageDriverViewHolder holder, int position, @NonNull DriverInfos model) {
                        if (model.getDriverStatus() == MyEnum.DriverStatus.PENDING) {
                            Picasso.get().load(model.getPicture()).into(holder.imgDriver);
                            holder.tvName.setText(model.getName());
                            holder.tvStatus.setText(model.getDriverStatus().toString());
                        } else {
                            return;
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ManageRegisterDriverDetailActivityAdmin.class);
                                Bundle driverBundle = new Bundle();
                                driverBundle.putSerializable("RegisterDriver", (Serializable) model);
                                intent.putExtras(driverBundle);
                                //startActivity(intent);
                                startActivityForResult(intent,DRIVER_REGISTER_DETAIL_CODE);
                            }
                        });
                    }

                };
        rcvManageDriver.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DRIVER_REGISTER_DETAIL_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Driver Added!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),"Driver Deny!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}