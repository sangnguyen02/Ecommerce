package com.example.ecommerce.Employee.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ManageDriverDetailActivityAdmin extends AppCompatActivity {

    DatabaseReference DriverRef;
    RecyclerView rcvManageDriver;
    MaterialButton manageRegisterButton;

    private TextView  driverIdTextView, fullnameTextView, phoneTextView, emailTextView,
            balanceTextView, licenseTextView, bankAccountTextView, activityStatusTextView;

    private MaterialButton viewHistoryButton, lockButton, unlockButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_driver_detail_admin);


        DriverInfos clickedDriver = (DriverInfos) getIntent().getExtras().get("clickedDriver");

        initView(clickedDriver);

    }

    private void initView(DriverInfos clickedDriver) {
        driverIdTextView = findViewById(R.id.tv_DriverID);
        fullnameTextView = findViewById(R.id.tv_Fullname);
        phoneTextView = findViewById(R.id.tv_phone);
        emailTextView = findViewById(R.id.tv_email);
        balanceTextView = findViewById(R.id.tv_balance);
        licenseTextView = findViewById(R.id.tv_license);
        bankAccountTextView = findViewById(R.id.tv_bankAccount);
        activityStatusTextView = findViewById(R.id.tv_activityStatus);
        driverIdTextView.setText(clickedDriver.getId());
        fullnameTextView.setText(clickedDriver.getName());
        phoneTextView.setText(clickedDriver.getPhoneNo());
        emailTextView.setText(clickedDriver.getMail());
        if(clickedDriver.getBalance() != 0){
            balanceTextView.setText(String.valueOf(clickedDriver.getBalance()));
        }else{
            balanceTextView.setText("0");
        }
        licenseTextView.setText(clickedDriver.getLicense());
        bankAccountTextView.setText(clickedDriver.getBankAccount());
        activityStatusTextView.setText(clickedDriver.getDriverStatus().toString());

        viewHistoryButton = findViewById(R.id.viewHistory_btn);
        lockButton = findViewById(R.id.lock_btn);
        unlockButton = findViewById(R.id.unlock_btn);
        deleteButton = findViewById(R.id.delete_btn);

        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockDriverAccount(clickedDriver.getPhoneNo());
            }
        });

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockDriverAccount(clickedDriver.getPhoneNo());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDeleteDriverAccount(clickedDriver.getPhoneNo());
            }
        });
    }

    private void lockDriverAccount(String driverID) {
        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference driverInfoRef = database.getReference("DriversInfo"); // Change this to your actual node

        // Assuming you have a driver ID for the driver you want to lock
        String driverId = driverID;

        // Retrieve the specific DriverInfos node from the database
        DatabaseReference specificDriverRef = driverInfoRef.child(driverId);

        // Update the driverStatus field to DriverStatus.BLOCK
        specificDriverRef.child("driverStatus").setValue(MyEnum.DriverStatus.BLOCK);
        activityStatusTextView.setText(MyEnum.DriverStatus.BLOCK.toString());

        // Inform the user or handle the result as needed
        Toast.makeText(this, "Driver locked successfully", Toast.LENGTH_SHORT).show();
    }

    private void unlockDriverAccount(String driverID) {
        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference driverInfoRef = database.getReference("DriversInfo"); // Change this to your actual node

        // Assuming you have a driver ID for the driver you want to lock
        String driverId = driverID;

        // Retrieve the specific DriverInfos node from the database
        DatabaseReference specificDriverRef = driverInfoRef.child(driverId);

        // Update the driverStatus field to DriverStatus.BLOCK
        specificDriverRef.child("driverStatus").setValue(MyEnum.DriverStatus.OFFLINE);
        activityStatusTextView.setText(MyEnum.DriverStatus.OFFLINE.toString());

        // Inform the user or handle the result as needed
        Toast.makeText(this, "Driver locked successfully", Toast.LENGTH_SHORT).show();
    }

    private void deleteDriverAccount(String driverID){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting driver account...");
        progressDialog.setCancelable(false); // Prevent users from cancelling the operation

        progressDialog.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference driverInfoRef = database.getReference("DriversInfo");

        String driverId = driverID; // Replace with the actual driver ID
        DatabaseReference specificDriverRef = driverInfoRef.child(driverId);

        // Remove the driver's information from the database with a completion listener
        specificDriverRef.removeValue((error, ref) -> {
            progressDialog.dismiss();
            if (error == null) {
                // The deletion was successful
                Toast.makeText(this, "Driver account deleted successfully", Toast.LENGTH_SHORT).show();
                // Optionally, you can navigate the user back or perform other actions
                finish();
            } else {
                // There was an error during the deletion
                Toast.makeText(this, "Failed to delete driver account: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performDeleteDriverAccount(String driverIdd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Driver Account");
        builder.setMessage("Are you sure you want to delete this driver account? This action cannot be undone.");

        // Add buttons to the dialog
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // User clicked "Delete" button
            deleteDriverAccount(driverIdd);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // User clicked "Cancel" button
            dialog.dismiss();
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}