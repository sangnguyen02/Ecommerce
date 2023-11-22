package com.example.ecommerce.Employee.Driver.Activities;
import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AcceptOrderActivityDriver extends AppCompatActivity {
    String idOrder;

    TextView idOrderTextView,customerPhoneTextView,paymentMethodTextView,vehicleTypeTextView
            ,pickUpLocationTextView,destinationTextView,priceTextVIew;
    MaterialButton accept_btn,cancel_btn;
    View showSnackBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bottom_sheet_order_pop_up);
        idOrder = getIntent().getStringExtra("idOrder");
        //idOrderTextView = findViewById(R.id.idOrderTextView);
        customerPhoneTextView = findViewById(R.id.tv_uPhone);
        paymentMethodTextView = findViewById(R.id.tv_method);
        vehicleTypeTextView = findViewById(R.id.tv_typeVehicle);
        pickUpLocationTextView = findViewById(R.id.tv_From);
        destinationTextView = findViewById(R.id.tv_To);
        priceTextVIew = findViewById(R.id.tv_price);
        showSnackBarView = findViewById(android.R.id.content);
        accept_btn= findViewById(R.id.acceptBook_btn);
        cancel_btn= findViewById(R.id.cancelBook_btn);


        orderInfoDisplay(customerPhoneTextView,paymentMethodTextView,vehicleTypeTextView
                ,pickUpLocationTextView,destinationTextView,priceTextVIew);

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrder();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });

    }


    private void orderInfoDisplay(final TextView customerPhoneTextView,
                                  final TextView paymentMethodTextView, final TextView vehicleTypeTextView,
                                  final TextView pickUpLocationTextView,final TextView destinationTextView,
                                  final TextView priceTextVIew)
    {
        Log.e("Order","Find database");
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("OrderInfo").child(idOrder);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String paymentMethod = dataSnapshot.child("paymentMethod").getValue().toString();
                    Log.e("Order",paymentMethod);
                    String phoneUser = dataSnapshot.child("clientNo").getValue().toString();
                    Log.e("Order",phoneUser);
                    String price = dataSnapshot.child("price").getValue().toString();
                    Log.e("Order",price);
                    String vehicleType = dataSnapshot.child("vehicleType").getValue().toString();
                    Log.e("Order",vehicleType);
                    String pickUpLocation="Rong";
                    if (dataSnapshot.child("pickupLocation").getValue() != null) {
                        pickUpLocation = dataSnapshot.child("pickupLocation").getValue().toString();
                    }
                    Log.e("Order", pickUpLocation);
                    String destination = dataSnapshot.child("desination").getValue().toString();
                    Log.e("Order",destination);
                    customerPhoneTextView.setText(phoneUser);

                    paymentMethodTextView.setText(paymentMethod);
                    vehicleTypeTextView.setText(vehicleType);
                    pickUpLocationTextView.setText(pickUpLocation);
                    destinationTextView.setText(destination);
                    priceTextVIew.setText(price);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Order","Cannot find database");
            }
        });
    }

    private void cancelOrder() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("OrderInfo").child(idOrder);

        UsersRef.child("orderStatus").setValue("CANCELED").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Cancel successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to cancel", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void acceptOrder() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("OrderInfo").child(idOrder);

        UsersRef.child("orderStatus").setValue("ACCEPT").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Accept confirmed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to accept", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }


}
