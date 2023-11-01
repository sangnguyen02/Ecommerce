package com.example.ecommerce.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.ecommerce.R;
public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etAge, etPhoneNumber, etVehicleName;
    private Spinner spinnerVehicleType;
    private Button btnRegister;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        etName = findViewById(R.id.name_input);
        etAge = findViewById(R.id.age_input);
        etPhoneNumber = findViewById(R.id.phone_number_input);
        etVehicleName = findViewById(R.id.vehicle_name_input);
        spinnerVehicleType = findViewById(R.id.vehicle_type_input);
        btnRegister = findViewById(R.id.register_button);

        // Set click listener for register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Retrieve user input fields
        String name = etName.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString().trim());
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String vehicleName = etVehicleName.getText().toString().trim();
        String vehicleType = spinnerVehicleType.getSelectedItem().toString();

        // Validate input fields
        if (name.isEmpty() || phoneNumber.isEmpty() || vehicleName.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user details to Realtime Database
        String userId = mDatabase.child("users").push().getKey();
        User user = new User(userId, name, age, phoneNumber, vehicleName, vehicleType);
        mDatabase.child("users").child(userId).setValue(user);

        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}