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
import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CancelOrderActivityDriver extends AppCompatActivity {
    String idOrder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idOrder = getIntent().getStringExtra("idOrder");

        // Retrieve order information from the intent

        cancelOrder();
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
