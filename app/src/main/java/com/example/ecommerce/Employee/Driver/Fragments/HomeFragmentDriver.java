package com.example.ecommerce.Employee.Driver.Fragments;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Employee.Driver.Activities.ReceiveOrderActivityDriver;
import com.example.ecommerce.Employee.LoginActivityEmployee;
import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.LoginActivityUser;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragmentDriver extends Fragment {
    String key_driver,orderId;
    CardView layoutBottomSheetRequestPopUp;
    BottomSheetBehavior bottomSheetBehaviorRequestPopUp;
    View showSnackBarView;
    ImageView setAvailable, setUnavailable;
    TextView tvFrom, tvTo, tvPrice, tvMethod, tvUPhone, tvTypeVehicle;
    MaterialButton acceptBook_btn,cancelBook_btn;
    public static final String CHANNEL_ID = "notification_driver";

    private DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_home_driver, container, false);
        key_driver = getArguments().getString("key_driver");
        orderId = getArguments().getString("orderId");

        Log.e("HomeFrag", key_driver);

        layoutBottomSheetRequestPopUp = rootView.findViewById(R.id.bottom_sheet_order_pop_up);
        bottomSheetBehaviorRequestPopUp = BottomSheetBehavior.from(layoutBottomSheetRequestPopUp);
        setAvailable = rootView.findViewById(R.id.img_connect);
        setUnavailable = rootView.findViewById(R.id.img_disconnect);
        bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_COLLAPSED);

        showSnackBarView = rootView.findViewById(android.R.id.content);



        // Find the TextViews in your bottom sheet layout
        tvFrom = rootView.findViewById(R.id.tv_From);
        tvTo = rootView.findViewById(R.id.tv_To);
        tvPrice = rootView.findViewById(R.id.tv_price);
        tvMethod = rootView.findViewById(R.id.tv_method);
        tvUPhone = rootView.findViewById(R.id.tv_uPhone);
        tvTypeVehicle = rootView.findViewById(R.id.tv_typeVehicle);
        acceptBook_btn= rootView.findViewById(R.id.acceptBook_btn);
        cancelBook_btn= rootView.findViewById(R.id.cancelBook_btn);

        if (orderId != null) {
            Log.e("Update after notify",orderId);
            updateBottomSheet(orderId);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 500); // 1000 milliseconds = 1 second
        }
        else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");

            // Add a ValueEventListener to listen for changes
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("HomeFrag","searchForDatabase");
                    // This method will be called whenever data at the specified location changes
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String clientNo = snapshot.child("clientNo").getValue(String.class);
                        String orderStatus = snapshot.child("orderStatus").getValue(String.class);

                        if (clientNo.equals(key_driver) && orderStatus.equals("PENDING")) {
                            // Authentication successful
                            Order dataObject = snapshot.getValue(Order.class);
                            orderId=snapshot.getKey();
                            Toast.makeText(rootView.getContext(), "requested order", Toast.LENGTH_SHORT).show();
                            updateBottomSheet(orderId);
                            bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_EXPANDED);
                            createNotificationChannel();
                            sendNotification();
                            break;
                        }



//                    Order dataObject = snapshot.getValue(Order.class);
//                    if (dataObject != null && dataObject.getClientNo() == key_driver && dataObject.getOrderStatus() == MyEnum.OrderStatus.PENDING) {
//                        orderId=snapshot.getKey();
//                        Toast.makeText(rootView.getContext(), "requested order", Toast.LENGTH_SHORT).show();
//                        //updateBottomSheet(dataObject);
//
//                        createNotificationChannel();
//                        sendNotification();
//
//                    }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("MainActivity", "Error: " + databaseError.getMessage());
                }
            });
        }



//        setAvailable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createNotificationChannel();
//                sendNotification();
//
//            }
//        });

        return rootView;
    }

    private void setDriver_available() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        UsersRef.child("driverStatus").setValue("Unavailable").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Change status successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to change status", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void setDriver_unavailable() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(key_driver);

        UsersRef.child("Status").setValue("Accept").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Accept confirmed", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to accept", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = (NotificationManager) requireActivity().getSystemService(requireContext().NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


    public void sendNotification() {
        Log.e("Home","Check send notify");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("New Order Received")
                .setContentText("You have a new order.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Create an Intent for the notification to open an activity (if needed)
        Log.e("Dua key driver vao","Bat dau");
        Intent intent = new Intent(requireContext(), ReceiveOrderActivityDriver.class);
        Bundle bundle = new Bundle();
        bundle.putString("key_driver", key_driver);
        bundle.putString("orderId", orderId);
        Log.e("Dua key driver vao",key_driver);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        // Show the notification
        notificationManager = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Home","Check Failed");
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void updateBottomSheet(String orderId) {
        // Extract relevant information from the Order object and update the TextViews
        Log.e("Set information","Starting");

        if (orderId != null) {
            Log.e("Set information","True");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");

            // Add a ValueEventListener to listen for changes
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("HomeFrag","searchForDatabase");
                    // This method will be called whenever data at the specified location changes
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String orderNum =snapshot.getKey();


                        if (orderNum.equals(orderId)) {
                            // Authentication successful
                            Order dataObject = snapshot.getValue(Order.class);
                            tvFrom.setText(dataObject.getPickupLocation_Longtitude());
                            tvTo.setText(dataObject.getDestination_Latitude());
                            tvPrice.setText(String.valueOf(dataObject.getPrice()));
                            tvMethod.setText(String.valueOf(dataObject.getPaymentMethod()));
                            tvUPhone.setText(dataObject.getClientNo());
                            tvTypeVehicle.setText(String.valueOf(dataObject.getVehicleType()));
                            break;
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("MainActivity", "Error: " + databaseError.getMessage());
                }
            });



        }
    }
}