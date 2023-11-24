package com.example.ecommerce.Employee.Driver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
            driverInfoDisplay();
        }
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadInfo();
            }
        });

    }


    private void driverInfoDisplay()
    {
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriverInfo").child(key_driver);

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


        if (TextUtils.isEmpty(newName)||TextUtils.isEmpty(newPhone)||TextUtils.isEmpty(newMail)||TextUtils.isEmpty(newId)||
                TextUtils.isEmpty(newLicense)||TextUtils.isEmpty(newBank)) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Please fill all required information", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        UsersRef.child("name").setValue(newName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update other user information
                        UsersRef.child("phoneNo").setValue(newPhone);
                        UsersRef.child("mail").setValue(newMail);
                        UsersRef.child("id").setValue(newId);
                        UsersRef.child("license").setValue(newLicense);
                        UsersRef.child("bankAccount").setValue(newBank);

                        Snackbar snackbar = Snackbar.make(showSnackBarView, "Update profile successfully", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to update profile", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });







    }

}