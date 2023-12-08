package com.example.ecommerce.Employee.Driver.Activities;

import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.example.ecommerce.User.Activities.LoginActivityUser;
import com.example.ecommerce.User.Activities.MainActivityUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class ReceiveOrderActivityDriver extends AppCompatActivity {
    String key_driver,orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key_driver = extras.getString("key_driver");
            orderId = extras.getString("orderId");
        }
        else key_driver = "Null";
        // Open the HomeFragmentDriver when this activity is launched
        Intent intent = new Intent(this, MainActivityDriver.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("key_driver", key_driver);
        bundle1.putString("orderId", orderId);
        Log.e("ReceiveOrder","halo");
        Log.e("ReceiveOrder",key_driver);
        intent.putExtras(bundle1);
        startActivity(intent);

        // Finish this activity so that it doesn't stay in the back stack
        finish();

    }





//    private Runnable searchTask = new Runnable() {
//        @Override
//        public void run() {
//            // Your search logic here
//            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
//
//            Query query = ordersRef.orderByChild("clientNo_orderStatus").equalTo("097546128_ACCEPT");
//
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Iterate through the results
//                    boolean foundFirstOrder = false;
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String orderId = snapshot.getKey();
//                        String nameDriver = snapshot.child("nameDriver").getValue(String.class);
//                        String status = snapshot.child("Status").getValue(String.class);
//
//
//
//                        break;
//                    }
//
//                    // If you want to do something after finding the first order, you can add additional code here
//                    if (foundFirstOrder) {
//                        // Additional code after finding the first order
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Handle errors
//                }
//            });
//
//            // Schedule the next execution of this task after the specified delay
//            handler.postDelayed(this, delayMillis);
//        }
//    };
}




