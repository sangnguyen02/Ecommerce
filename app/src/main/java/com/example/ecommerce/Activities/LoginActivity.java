package com.example.ecommerce.Activities;

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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    String phoneNumber;
    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken  resendingToken;
    EditText phoneInput, otpInput;
    MaterialButton sendOTPBtn, loginBtn,registerBtn;
    TextView resendOtpTextView;
    View showSnackBarView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneInput = findViewById(R.id.login_phone_number_input);
        otpInput = findViewById(R.id.otpInput);
        sendOTPBtn = findViewById(R.id.sendOTP);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);
        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        showSnackBarView = findViewById(android.R.id.content);
        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!countryCodePicker.isValidFullNumber()) {
                    phoneInput.setError("Phone number not valid");
                }
                phoneNumber = countryCodePicker.getFullNumberWithPlus();
                sendOtp(phoneNumber, true);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOtp  = otpInput.getText().toString();
                if (verificationCode != null && !verificationCode.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
                    signIn(credential);
                } else {
                    Snackbar snackbar = Snackbar.make(showSnackBarView, "OTP verification failed", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Toast.makeText(LoginActivity.this, "Verification code is invalid", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    void sendOtp(String phoneNumber,boolean isResend){
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
                                //AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;

                                Snackbar snackbar = Snackbar.make(showSnackBarView, "OTP sent successfully", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                //AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully");
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("PhoneString",phoneInput.getText().toString().trim());
                    String phoneString = phoneInput.getText().toString().trim();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("phoneString",phoneString);
                    startActivity(intent);
                }
                else {
                    //AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                    Snackbar snackbar = Snackbar.make(showSnackBarView, "OTP verification failed", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
    void startResendTimer(){
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resendOtpTextView.setText("Resend OTP in "+timeoutSeconds +" seconds");
                if(timeoutSeconds<=0){
                    timeoutSeconds =60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resendOtpTextView.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}