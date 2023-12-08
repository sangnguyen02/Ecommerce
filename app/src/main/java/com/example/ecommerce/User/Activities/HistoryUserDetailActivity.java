package com.example.ecommerce.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ecommerce.Employee.Driver.ReverseGeocodingTask;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HistoryUserDetailActivity extends AppCompatActivity {

    DatabaseReference orderDetailRef;
    ReverseGeocodingTask reverseGeocodingTaskFrom, reverseGeocodingTaskTo;
    String driverName;

    TextView tv_dateTime, tv_BillID, tv_price, tv_pMethod, tv_typeVehicle, tv_from, tv_to, tv_driverName, tv_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_user_detail);

        tv_dateTime = findViewById(R.id.title_date);
        tv_BillID = findViewById(R.id.tv_bill_id_order_detail);
        tv_price = findViewById(R.id.tv_price_order_detail);
        tv_pMethod = findViewById(R.id.tv_pMethod_order_detail);
        tv_typeVehicle = findViewById(R.id.tv_typeVehicle_order_detail);
        tv_from = findViewById(R.id.tv_from_order_detail);
        tv_to = findViewById(R.id.tv_to_order_detail);
        tv_driverName = findViewById(R.id.tv_driver_name_order_detail);
        tv_feedback = findViewById(R.id.tv_feedback_order_detail);

        reverseGeocodingTaskFrom = new ReverseGeocodingTask(tv_from);
        reverseGeocodingTaskTo = new ReverseGeocodingTask(tv_to);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ORDER_ID")) {
            String orderID = intent.getStringExtra("ORDER_ID");

            Log.d("ID", orderID);

            orderDetailRef = FirebaseDatabase.getInstance().getReference("Order").child(orderID);
            orderDetailRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("id").exists()) {
                        String order_ID = snapshot.child("id").getValue().toString();
                        String price = snapshot.child("price").getValue().toString();
                        String pMethod = snapshot.child("paymentMethod").getValue().toString();
                        String typeVehicle = snapshot.child("vehicleType").getValue().toString();
                        String dateTime = snapshot.child("dateTime").getValue().toString();

                        String destination_Lat = snapshot.child("destination_Latitude").getValue().toString();
                        String destination_Long = snapshot.child("destination_Longtidue").getValue().toString();
                        String pickupLocation_Lat = snapshot.child("pickupLocation_Latitude").getValue().toString();
                        String pickupLocation_Long = snapshot.child("pickupLocation_Longtitude").getValue().toString();
                        Double desLat = Double.parseDouble(destination_Lat);
                        Double desLong = Double.parseDouble(destination_Long);
                        Double pickLat = Double.parseDouble(pickupLocation_Lat);
                        Double pickLong = Double.parseDouble(pickupLocation_Long);

                        String from = reverseGeocodingTaskFrom.execute(pickLat,pickLong).toString();
                        String to = reverseGeocodingTaskTo.execute(desLat, desLong).toString();

                        String driverNo = snapshot.child("driverNo").getValue().toString();
                        String feedBack = "Good";

                        if(!order_ID.isEmpty()) {
                            tv_BillID.setText(order_ID);
                        }

                        if(!price.isEmpty()) {
                            tv_price.setText(price + " VND");
                        }

                        if(!pMethod.isEmpty()) {
                            tv_pMethod.setText(pMethod);
                        }

                        if(!typeVehicle.isEmpty()) {
                            tv_typeVehicle.setText(typeVehicle);
                        }

                        if(!dateTime.isEmpty()) {
                            tv_dateTime.setText(dateTime);
                        }

                        if(!from.isEmpty()) {
                            tv_from.setText(from);
                        }

                        if(!to.isEmpty()) {
                            tv_to.setText(to);
                        }

                        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference("DriversInfo").child(driverNo);
                        driverRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("name").exists()) {
                                    driverName = snapshot.child("name").getValue().toString();
                                    tv_driverName.setText(driverName);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        if(!feedBack.isEmpty()) {
                            tv_feedback.setText(feedBack);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}