package com.example.ecommerce.Employee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Employee.Admin.Activities.MainActivityAdmin;
import com.example.ecommerce.Employee.Driver.Activities.MainActivityDriver;
import com.example.ecommerce.Models.DriverAccount;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.LoginActivityUser;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import utils.PasswordHasher;

public class LoginActivityEmployee extends AppCompatActivity {

    EditText emailInput, passwordInput;
    MaterialButton loginBtn;
    TextView signInAsUser;
    DriverAccount _currentDriver = null;
    boolean _isFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_employee);

        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.login_btn);
        signInAsUser = findViewById(R.id.signInUser);


        signInAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivityEmployee.this, LoginActivityUser.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                //Validating Input
                if (email.isEmpty() || password.isEmpty()) {
                    // Show an error message for empty fields
                    Toast.makeText(LoginActivityEmployee.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Validating Sign In Method
                if (email.contains("admin")) {
                    SignInAsAdmin(email, password);
                } else {
                    FindCurrentDriver(email, password);
                }


            }
        });

    }

    private void FindCurrentDriver(String email, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriversAccount");
        _isFind = false;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dbUsername = snapshot.child("username").getValue(String.class);

                    if (dbUsername.equals(email)) {
                        _currentDriver = snapshot.getValue(DriverAccount.class);
                        Toast.makeText(LoginActivityEmployee.this, "CurrentDriver: " + _currentDriver.getUsername(), Toast.LENGTH_SHORT).show();
                        _isFind = true;
                        break;
                    }
                }

                if (_isFind) {
                    //Toast.makeText(LoginActivityEmployee.this, "User Found!", Toast.LENGTH_SHORT).show();
                    String salt = _currentDriver.getSalt();
                    String hashedPassword = PasswordHasher.hashPassword(password, salt);
                    String firebaseHashedPassword = _currentDriver.getPassword();
                    if (hashedPassword.equals(firebaseHashedPassword)) {
/*                        Log.d("Salt: ", salt);
                        Log.d("hashePassword: ", hashedPassword);
                        Log.d("FireBaseHased: ", firebaseHashedPassword);*/
                        Intent intent = new Intent(LoginActivityEmployee.this, MainActivityDriver.class);
                        Bundle bundle = new Bundle();
                        String key_driver = _currentDriver.getDriverID();
                        bundle.putString("key_driver", key_driver);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish(); // Optional: Finish the current activity to prevent going back on pressing back button
                    } else {
                        Toast.makeText(LoginActivityEmployee.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivityEmployee.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivityEmployee.this, "Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void SignInAsAdmin(String email, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Find account
                boolean isAuthenticated = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dbUsername = snapshot.child("email").getValue(String.class);
                    String dbPassword = snapshot.child("password").getValue(String.class);

                    if (dbUsername.equals(email) && dbPassword.equals(password)) {
                        // Authentication successful
                        isAuthenticated = true;
                        Toast.makeText(LoginActivityEmployee.this, "Welcome Admin!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                //Authenticating
                if (isAuthenticated) {
                    Intent intent = new Intent(LoginActivityEmployee.this, MainActivityAdmin.class);
                    startActivity(intent);
                    finish(); // Optional: Finish the current activity to prevent going back on pressing back button
                } else {
                    Toast.makeText(LoginActivityEmployee.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivityEmployee.this, "Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}