package com.example.ecommerce.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Employee.Admin.Activities.MainActivityAdmin;
import com.example.ecommerce.Employee.Driver.Activities.MainActivityDriver;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.LoginActivityUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.LoginActivity;

public class LoginActivityEmployee extends AppCompatActivity {

    EditText emailInput, passwordInput;
    MaterialButton loginBtn;
    TextView signInAsUser;
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
                if (email.contains("admin")){
                    SignInAsAdmin(email, password);
                }else{
                    SignInAsDriver(email, password);
                }


            }
        });

    }

    private void SignInAsDriver(String email, String password)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriversAccount");
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
                        Toast.makeText(LoginActivityEmployee.this, "Authentication Successfully.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                //Authenticating
                if (isAuthenticated) {
                    // Proceed to the next activity or show a success message
                    Intent intent = new Intent(LoginActivityEmployee.this, MainActivityDriver.class);
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

    private void SignInAsAdmin(String email, String password)
    {
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