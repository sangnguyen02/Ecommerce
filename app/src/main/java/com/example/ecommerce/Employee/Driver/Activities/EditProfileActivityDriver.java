package com.example.ecommerce.Employee.Driver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfileActivityDriver extends AppCompatActivity {
    EditText fullNameEditText,phoneNoEditText, mailEditText, idEditText,licenseEditText, bankNoEditText ;
    MaterialButton save_btn;
    ImageView imageDriver;
    String key_driver;
    View showSnackBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_driver);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key_driver = extras.getString("key_driver");
            Log.e("Edit Profile Driver",key_driver);
        }

        fullNameEditText = findViewById(R.id.editText_fullname);
        phoneNoEditText = findViewById(R.id.editText_phoneNo);
        mailEditText = findViewById(R.id.editText_mail);
        idEditText = findViewById(R.id.editText_ID);
        licenseEditText = findViewById(R.id.editText_license);
        bankNoEditText = findViewById(R.id.editText_bankNo);
        save_btn = findViewById(R.id.save_btn);
        imageDriver = findViewById(R.id.imageDriver);


        showSnackBarView = findViewById(android.R.id.content);
        if ( key_driver!= null){
            driverInfoDisplay(fullNameEditText,phoneNoEditText,mailEditText,
                    idEditText,licenseEditText, bankNoEditText,imageDriver);
        }
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadInfo();
            }
        });

    }


    private void driverInfoDisplay(final EditText fullNameEditText,final EditText phoneNoEditText,
                                   final EditText mailEditText,final EditText idEditText,
                                   final EditText licenseEditText,final EditText bankNoEditText,
                                   final ImageView imageDriver)
    {
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        DriverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Log.e("Profile Driver","Da kiem thay driver");
                    if (dataSnapshot.hasChild("name")){
                        String nameDriver = dataSnapshot.child("name").getValue().toString();
                        fullNameEditText.setText(nameDriver);
                    }
                    if (dataSnapshot.hasChild("mail")){
                        String mailDriver = dataSnapshot.child("mail").getValue().toString();
                        mailEditText.setText(mailDriver);
                    }
                    if (dataSnapshot.hasChild("license")){
                        String licenseDriver = dataSnapshot.child("license").getValue().toString();
                        licenseEditText.setText(licenseDriver);
                    }
                    if (dataSnapshot.hasChild("id")){
                        String idDriver = dataSnapshot.child("id").getValue().toString();
                        idEditText.setText(idDriver);
                    }
                    if (dataSnapshot.hasChild("phoneNo")){
                        String phoneDriver = dataSnapshot.child("phoneNo").getValue().toString();
                        phoneNoEditText.setText(phoneDriver);
                    }
                    if (dataSnapshot.hasChild("bankAccount")){
                        String bankDriver = dataSnapshot.child("bankAccount").getValue().toString();
                        bankNoEditText.setText(bankDriver);
                    }
                    if (dataSnapshot.hasChild("picture")){
                        String picDriver = dataSnapshot.child("picture").getValue().toString();
                        Picasso.get().load(picDriver).into(imageDriver);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Profile Driver Fragment","Error database");
            }
        });
    }

    private void uploadInfo() {
        String newName = fullNameEditText.getText().toString();
        String newPhone = phoneNoEditText.getText().toString();
        String newMail = mailEditText.getText().toString();
        String newId = idEditText.getText().toString();
        String newLicense = licenseEditText.getText().toString();
        String newBank = bankNoEditText.getText().toString();


        if (!isContainOnlyChar(newName)) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Invalid name (should only contain letters)", Snackbar.LENGTH_LONG);
            snackbar.show();
            return; // Exit the method if the name is invalid
        }

        // Validate the email
        /*if (!isValidEmail(newMail)) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Invalid email (should only contain valid characters)", Snackbar.LENGTH_LONG);
            snackbar.show();
            return; // Exit the method if the email is invalid
        }*/

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        if (!TextUtils.isEmpty(newName)) {
            UsersRef.child("name").setValue(newName);
        }

        if (!TextUtils.isEmpty(newPhone)) {
            UsersRef.child("phoneNo").setValue(newPhone);
        }

        if (!TextUtils.isEmpty(newMail)) {
            UsersRef.child("mail").setValue(newMail);
        }

        if (!TextUtils.isEmpty(newId)) {
            UsersRef.child("id").setValue(newId);
        }

        if (!TextUtils.isEmpty(newLicense)) {
            UsersRef.child("license").setValue(newLicense);
        }

        if (!TextUtils.isEmpty(newBank)) {
            UsersRef.child("bankAccount").setValue(newBank);
        }

        UsersRef.updateChildren(new HashMap<String, Object>())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(showSnackBarView, "Update profile successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish(); // Finish the activity after the delay
                            }
                        }, 3000);
                    } else {
                        Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to update profile", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });



    }
    private boolean isContainOnlyChar(String input) {
        // Check if the input does not contain any special characters except for "@"
        return input.matches("^[a-zA-Z ]+$");
    }


    private boolean isValidEmail(String email) {
        // Check if the email contains only valid characters
        return email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9.]+$");
    }



}