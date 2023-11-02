package com.example.ecommerce.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
//import com.example.ecommerce.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.hbb20.CountryCodePicker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    CountryCodePicker countryCodePicker;
    String phoneNumber, verificationCode;;
    Long timeoutSeconds = 30L;
    PhoneAuthProvider.ForceResendingToken  resendingToken;
    EditText phoneInput, otpInput;
    MaterialButton sendOTPBtn, loginBtn;
    TextView resendOtpTextView;
    View showSnackBarView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_country_code);
        phoneInput = findViewById(R.id.login_phone_number_input);
        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        otpInput = findViewById(R.id.otpInput);
        sendOTPBtn = findViewById(R.id.sendOTP);
        loginBtn = findViewById(R.id.login_btn);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);
        showSnackBarView = findViewById(android.R.id.content);

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
                if (verificationCode != null && !verificationCode.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
                    signIn(credential);
                } else {
                    Snackbar snackbar = Snackbar.make(showSnackBarView, "Verification code is invalid", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
    private void sendOtp(String phoneNumber, boolean isResend){
        startResendTimer();
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNumber)
//                        .setTimeout(60L,TimeUnit.SECONDS)
//                        .setActivity(this)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                signIn(phoneAuthCredential);
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Snackbar snackbar = Snackbar.make(showSnackBarView, "Verification failed", Snackbar.LENGTH_LONG);
//                                snackbar.show();
//                            }
//                            @Override
//                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(verificationId, forceResendingToken);
//                                verificationCode = verificationId;
//                                resendingToken = forceResendingToken;
//                                Snackbar snackbar = Snackbar.make(showSnackBarView, "OTP sent successfully", Snackbar.LENGTH_LONG);
//                                snackbar.show();
//                            }
//                        })
//                        .build();
//
//        PhoneAuthProvider.verifyPhoneNumber(options);
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
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToMainActivity(user.getPhoneNumber());
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

    private void goToMainActivity(String phoneNumber) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("phone_number",phoneNumber);
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


}