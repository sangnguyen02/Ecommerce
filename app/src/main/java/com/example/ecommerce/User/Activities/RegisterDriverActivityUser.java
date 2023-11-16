package com.example.ecommerce.User.Activities;

import static com.example.ecommerce.User.Fragments.ProfileFragmentUser.FAILED_SIGN_UP_REQUEST;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDriverActivityUser extends AppCompatActivity {

    EditText fullname, phoneNo, mail, id, license, bankNo;
    MaterialButton register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver_user);

        fullname = findViewById(R.id.editText_fullname);
        phoneNo = findViewById(R.id.editText_phoneNo);
        mail = findViewById(R.id.editText_mail);
        id = findViewById(R.id.editText_ID);
        license = findViewById(R.id.editText_license);
        bankNo = findViewById(R.id.editText_bankNo);
        register = findViewById(R.id.register_driver_btn);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PHONE_KEY")) {
            String phone = intent.getStringExtra("PHONE_KEY");
            phoneNo.setText(phone);
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    registerDriver();
                }

            }
        });

    }

    private void registerDriver() {
        String fullNameValue = fullname.getText().toString().trim();
        String phoneNoValue = phoneNo.getText().toString().trim();
        String mailValue = mail.getText().toString().trim();
        String idValue = id.getText().toString().trim();
        String licenseValue = license.getText().toString().trim();
        String bankNoValue = bankNo.getText().toString().trim();

        DriverInfos driverInfos = new DriverInfos(phoneNoValue, fullNameValue, mailValue, idValue, licenseValue, 5f, 0, "notyet", bankNoValue, MyEnum.DriverStatus.PENDING);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriversInfo").child(driverInfos.getId());
        databaseReference.setValue(driverInfos)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("Driver registered successfully");
                            // Finish the activity or navigate back to the user fragment as needed
                            returnAfterRegistation();
                        } else {
                            returnAfterRegistationFailed(task.getException().getMessage());
                        }
                    }
                });

    }

    private boolean checkFields() {
        String fullNameValue = fullname.getText().toString().trim();
        String phoneNoValue = phoneNo.getText().toString().trim();
        String mailValue = mail.getText().toString().trim();
        String idValue = id.getText().toString().trim();
        String licenseValue = license.getText().toString().trim();
        String bankNoValue = bankNo.getText().toString().trim();

        if (TextUtils.isEmpty(fullNameValue)) {
            showToast("Please enter your full name!");
            return false;
        }

        if (TextUtils.isEmpty(phoneNoValue)) {
            showToast("Please enter your phone number!");
            return false;
        }

        if (TextUtils.isEmpty(mailValue)) {
            showToast("Please enter your mail!");
            return false;
        }

        if (TextUtils.isEmpty(idValue)) {
            showToast("Please enter your identity card!");
            return false;
        }

        if (TextUtils.isEmpty(licenseValue)) {
            showToast("Please enter your phone driver license!");
            return false;
        }

        if (TextUtils.isEmpty(bankNoValue)) {
            showToast("Please enter your banking no!");
            return false;
        }


        return true;
    }

    private void returnAfterRegistation() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void returnAfterRegistationFailed(String error) {

        setResult(Activity.RESULT_CANCELED);

        Intent data = new Intent();
        data.putExtra("ERROR_CODE", FAILED_SIGN_UP_REQUEST);
        data.putExtra("ERROR_INFO", error);
        setResult(Activity.RESULT_CANCELED, data);

        // Finish the activity
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}