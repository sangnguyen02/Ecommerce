package com.example.ecommerce.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecommerce.Employee.LoginActivityEmployee;

import android.Manifest;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginActivityUser extends AppCompatActivity {

    private static final String TAG = LoginActivityUser.class.getName();
    CountryCodePicker countryCodePicker;
    String phoneNumber, verificationCode;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Long timeoutSeconds = 30L;
    PhoneAuthProvider.ForceResendingToken  resendingToken;
    EditText phoneInput, otpInput, input_username_bottom_sheet;
    MaterialButton sendOTPBtn, loginBtn, submitBtn;
    TextView resendOtpTextView, signInAsEmployee;
    View showSnackBarView;
    CardView layoutBottomSheetInputUsername;
    BottomSheetBehavior bottomSheetBehaviorInputUsername;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        countryCodePicker = findViewById(R.id.login_country_code);
        phoneInput = findViewById(R.id.login_phone_number_input);
        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        otpInput = findViewById(R.id.otpInput);
        sendOTPBtn = findViewById(R.id.sendOTP);
        loginBtn = findViewById(R.id.login_btn);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);
        signInAsEmployee = findViewById(R.id.signInEmployee);
        showSnackBarView = findViewById(android.R.id.content);

        layoutBottomSheetInputUsername = findViewById(R.id.bottom_sheet_input_username);
        bottomSheetBehaviorInputUsername = BottomSheetBehavior.from(layoutBottomSheetInputUsername);

        input_username_bottom_sheet = findViewById(R.id.inputText_fullname);
        submitBtn = findViewById(R.id.submitName_btn);


        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = countryCodePicker.getFullNumberWithPlus();
                if(!countryCodePicker.isValidFullNumber()) {
                    phoneInput.setError("Phone number not valid");
                }
                else {
                    sendOtp(phoneNumber, false);
                    //sendOTPBtn.setEnabled(false);
                }


            }
        });

        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp(phoneNumber, true);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOtp  = otpInput.getText().toString().trim();
                if (!verificationCode.isEmpty() && !enteredOtp.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
                    signIn(credential);
                } else {
                    Snackbar snackbar = Snackbar.make(showSnackBarView, "Verification code is invalid", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        signInAsEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivityUser.this, LoginActivityEmployee.class);
                startActivity(intent);
            }
        });

    }
    private void sendOtp(String phoneNumber, boolean isResend){
        startResendTimer();
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Snackbar snackbar = Snackbar.make(showSnackBarView, "OTP verification failed", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                resendOtpTextView.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar.make(showSnackBarView, "OTP sent successfully", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    private void signIn(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            createCredentialToFirebase(user.getPhoneNumber(), user.getUid());
                        } else {

                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar snackbar = Snackbar.make(showSnackBarView, "The verification code entered was invalid", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }
                });
    }
    private void goToMainActivity(String phoneNumber, String userName) {
        Intent intent = new Intent(LoginActivityUser.this, MainActivityUser.class);
        Bundle bundle = new Bundle();
        bundle.putString("phone_number", phoneNumber);
        bundle.putString("user_name", userName);
        requestLocationPermission();
        intent.putExtras(bundle);
        startActivity(intent);

    }
    private void startResendTimer() {
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resendOtpTextView.setText("Resend OTP in "+ timeoutSeconds +" seconds");
                if(timeoutSeconds<=0) {
                    timeoutSeconds =30L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resendOtpTextView.setEnabled(true);
                        resendOtpTextView.setText("Resend the OTP Here");
                    });
                }
            }
        },0,1000);
    }
    private void createCredentialToFirebase(final String phone, final String idUser) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("Users").child(phone).exists()) {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phoneNum", phone);
                    userdataMap.put("nameUser", "empty");
                    userdataMap.put("idUser", idUser);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    showNameUserForm(phone);
                                }

                            });
                }
                else
                {
                    String nameUser = snapshot.child("Users").child(phone).child("nameUser").getValue(String.class);
                    if (nameUser != null && nameUser.equals("empty")) {
                        // NameUser is empty, show the form to fill in the nameUser
                        showNameUserForm(phone);
                    } else {
                        // NameUser is not empty, proceed to the main activity
                        goToMainActivity(phone, nameUser);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showNameUserForm(String phone) {
        bottomSheetBehaviorInputUsername.setState(BottomSheetBehavior.STATE_EXPANDED);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUser = input_username_bottom_sheet.getText().toString().trim();
                if (!nameUser.isEmpty()) {
                    // Update the nameUser in Firebase
                    updateNameUserInFirebase(phone, nameUser);
                    // Hide the BottomSheetDialog
                    bottomSheetBehaviorInputUsername.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    // Handle empty nameUser
                    // You can show a message or handle it as needed
                    Snackbar snackbar = Snackbar.make(showSnackBarView, "The input name field is empty", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
    private void updateNameUserInFirebase(String phone, String nameUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        userRef.child("nameUser").setValue(nameUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update successful, proceed to the main activity
                            goToMainActivity(phone, nameUser);
                        } else {
                            // Handle the error, show a message or handle it as needed
                            Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }
    private void requestLocationPermission() {
        // Check if the app has location permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If not, request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        // You may add an else block here if you want to handle the case when the permission is already granted
    }

}