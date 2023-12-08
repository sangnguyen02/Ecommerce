package com.example.ecommerce.Employee.Driver.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.Adapters.HistoryDriverAdapter;
import com.example.ecommerce.Adapters.HistoryUserAdapter;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragmentDriver extends Fragment {

    View rootView;
    DatabaseReference historyRef;
    RecyclerView rcv_history_driver;
    HistoryDriverAdapter adapter;
    List<Order> orderList;
    String key_driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history_driver, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        key_driver = preferences.getString("driver_number", "");

        rcv_history_driver = rootView.findViewById(R.id.rcv_history_driver);
        rcv_history_driver.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(),RecyclerView.VERTICAL,false);
        rcv_history_driver.setLayoutManager(linearLayoutManager);

        historyRef = FirebaseDatabase.getInstance().getReference("Order");
        orderList = new ArrayList<>();
        loadHistoryOrderDriver();

        adapter = new HistoryDriverAdapter(orderList, getContext());
        rcv_history_driver.setAdapter(adapter);

        return rootView;
    }

    private void loadHistoryOrderDriver() {
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    String phoneDriver = order.getDriverNo();
                    Log.d("phone", phoneDriver);
                    if(phoneDriver.equals(key_driver)) {
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