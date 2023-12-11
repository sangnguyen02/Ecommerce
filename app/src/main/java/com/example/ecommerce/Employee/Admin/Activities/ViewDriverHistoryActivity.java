package com.example.ecommerce.Employee.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ecommerce.Adapters.HistoryDriverAdapter;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDriverHistoryActivity extends AppCompatActivity {
    DatabaseReference historyRef;
    RecyclerView rcv_driver_history_admin;
    HistoryDriverAdapter adapter;
    List<Order> orderList;
    String driver_clicked_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_driver_history);


        rcv_driver_history_admin = findViewById(R.id.rcv_driver_history_admin);
        rcv_driver_history_admin.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        rcv_driver_history_admin.setLayoutManager(linearLayoutManager);

        historyRef = FirebaseDatabase.getInstance().getReference("Order");
        orderList = new ArrayList<>();
        adapter = new HistoryDriverAdapter(orderList, getApplicationContext());
        rcv_driver_history_admin.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("driver_clicked_number")) {
            driver_clicked_number = intent.getStringExtra("driver_clicked_number");
            loadHistoryDriver();
        }
    }

    private void loadHistoryDriver() {
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    String phoneDriver = order.getDriverNo();
                    Log.d("phone", phoneDriver);
                    if(phoneDriver.equals(driver_clicked_number)) {
                        if (order != null) {
                            orderList.add(order);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}