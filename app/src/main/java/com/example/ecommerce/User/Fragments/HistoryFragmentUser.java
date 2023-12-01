package com.example.ecommerce.User.Fragments;

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

import com.example.ecommerce.Adapters.HistoryUserAdapter;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragmentUser extends Fragment {
    View rootView;
    DatabaseReference historyRef;
    RecyclerView rcv_history_user;

    HistoryUserAdapter adapter;
    List<Order> orderList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String phoneNumber = preferences.getString("phone_number","" );

//        DatabaseReference billID = FirebaseDatabase.getInstance().getReference().child("Order").child("bId");
//        billID.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String bId = snapshot.getValue(String.class);
//
//                    Log.d("Bid", bId);
//                } else {
//                    Log.d("Bid", "null");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Error", "Failed to read value", error.toException());
//            }
//        });




        rcv_history_user = rootView.findViewById(R.id.rcv_history_user);
        rcv_history_user.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(),RecyclerView.VERTICAL,false);
        rcv_history_user.setLayoutManager(linearLayoutManager);

        historyRef = FirebaseDatabase.getInstance().getReference("Order");
        orderList = new ArrayList<>();
        loadHistoryOrderUser();

        adapter = new HistoryUserAdapter(orderList, getContext());
        rcv_history_user.setAdapter(adapter);


        return rootView;
    }

    private void loadHistoryOrderUser() {
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orderList.add(order);
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