package com.example.ecommerce.Employee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

import java.util.concurrent.Executor;

import utils.PasswordHasher;

public class LoginActivityEmployee extends AppCompatActivity {

    EditText emailInput, passwordInput;
    MaterialButton loginBtn;
    TextView signInAsUser;
    String key_driver;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    DriverAccount _currentDriver = null;
    boolean _isFind;
    ImageView fingerprintBtn;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_employee);

        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.login_btn);
        signInAsUser = findViewById(R.id.signInUser);
        fingerprintBtn = findViewById(R.id.fingerprint_btn);
        InitBiometricLogin();
        fingerprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authenticate")
                        .setDescription("Use FingerPrint To Login").setDeviceCredentialAllowed(true).build();
                biometricPrompt.authenticate(promptInfo);
            }
        });

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
                requestPermission();
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

    private void InitBiometricLogin() {
        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device doesn't have fingerprint", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Not working", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No fingerprint assigned", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "FingerPrint Error", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "FingerPrint Accepted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "FingerPrint Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
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
                        /* Log.d("Salt: ", salt);
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