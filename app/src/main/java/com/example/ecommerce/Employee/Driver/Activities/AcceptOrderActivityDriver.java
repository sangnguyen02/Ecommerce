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

import android.widget.EditText;
import android.widget.TextView;

public class AcceptOrderActivityDriver extends AppCompatActivity {
    String idOrder;

    EditText idOrderTextView,customerPhoneTextView,paymentMethodTextView,vehicleTypeTextView
            ,pickUpLocationTextView,priceTextVIew;
    MaterialButton save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order_layout);
        idOrder = getIntent().getStringExtra("idOrder");
        idOrderTextView = findViewById(R.id.idOrderTextView);
        customerPhoneTextView = findViewById(R.id.customerPhoneTextView);
        paymentMethodTextView = findViewById(R.id.paymentMethodTextView);
        vehicleTypeTextView = findViewById(R.id.vehicleTypeTextView);
        pickUpLocationTextView = findViewById(R.id.pickeUpLocationTextView);
        priceTextVIew = findViewById(R.id.priceTextVIew);


        orderInfoDisplay(idOrderTextView,customerPhoneTextView,paymentMethodTextView,vehicleTypeTextView
                ,pickUpLocationTextView,priceTextVIew);
    }


    private void orderInfoDisplay(final EditText idOrderTextView, final EditText customerPhoneTextView,
                                  final EditText paymentMethodTextView, final EditText vehicleTypeTextView,
                                  final EditText pickUpLocationTextView, final EditText priceTextVIew)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(idOrder);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String paymentMethod = dataSnapshot.child("paymentMethod").getValue().toString();
                    String phoneUser = dataSnapshot.child("phoneNum").getValue().toString();
                    String price = dataSnapshot.child("price").getValue().toString();
                    String vehicleType = dataSnapshot.child("vehicleType").getValue().toString();
                    String pickUpLocation = dataSnapshot.child("pLocation").getValue().toString();

                    customerPhoneTextView.setText(phoneUser);
                    idOrderTextView.setText(idOrder);
                    paymentMethodTextView.setText(paymentMethod);
                    vehicleTypeTextView.setText(vehicleType);
                    pickUpLocationTextView.setText(pickUpLocation);
                    priceTextVIew.setText(price);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cancelOrder() {


        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(idOrder);

        UsersRef.child("Status").setValue("Decline").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Cancel successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
                finish();
            }
            else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to cancel", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }


}
