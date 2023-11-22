package com.example.ecommerce.User.Activities;

import static com.example.ecommerce.User.Fragments.ProfileFragmentUser.FAILED_SIGN_UP_REQUEST;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterDriverActivityUser extends AppCompatActivity {

    EditText fullname, phoneNo, mail, id, license, bankNo, bankName;
    MaterialButton register;

    CircleImageView circleImageView;

    ProgressDialog progressDialog;

    // Constants
    private static final int PICK_IMAGE_REQUEST = 454;

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
        bankName = findViewById(R.id.editText_bankName);
        register = findViewById(R.id.register_driver_btn);
        circleImageView = findViewById(R.id.imageUser);
        _localPictureUrl = null;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PHONE_KEY")) {
            String phone = intent.getStringExtra("PHONE_KEY");
            String name = intent.getStringExtra("NAME_KEY");
            phoneNo.setText(phone);
            fullname.setText(name);
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    UploadDriverToFireBase(_localPictureUrl);
                }
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            _localPictureUrl = data.getData();
            LoadImageToCircleImageView(selectedImageUri);
        }
    }

    private void LoadImageToCircleImageView(Uri selectedImageUri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            circleImageView.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    String _firebaseDownloadUrl;
    Uri _localPictureUrl;

    private void UploadDriverToFireBase(Uri selectedLocalImageUri) {

        //Get data from UI
        String fullNameValue = fullname.getText().toString().trim();
        String phoneNoValue = phoneNo.getText().toString().trim();
        String mailValue = mail.getText().toString().trim();
        String idValue = id.getText().toString().trim();
        String licenseValue = license.getText().toString().trim();
        String bankNoValue = bankNo.getText().toString().trim();
        String bankNameValue = bankName.getText().toString().trim();

        //Generate FireBase File Name
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String filename = formatter.format(now);
        filename = filename + "_" + fullNameValue;

        //ProgressDialog Be Responsive
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering...");
        progressDialog.show();

        //Upload Image To FireBase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("DriverImage/" + filename);

        imageRef.putFile(selectedLocalImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL to Firebase Database or use it as needed
                        _firebaseDownloadUrl = uri.toString();

                        DriverInfos driverInfos = new DriverInfos(phoneNoValue, fullNameValue, mailValue, idValue, licenseValue, 5f, 0, _firebaseDownloadUrl,bankNameValue ,bankNoValue, MyEnum.DriverStatus.PENDING);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriversInfo").child(driverInfos.getPhoneNo());
                        databaseReference.setValue(driverInfos)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            showToast("Driver registered successfully");
                                            // Finish the activity or navigate back to the user fragment as needed
                                            returnAfterRegistation();
                                        } else {
                                            returnAfterRegistationFailed(task.getException().getMessage());
                                        }
                                    }
                                });

                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean checkFields() {
        String fullNameValue = fullname.getText().toString().trim();
        String phoneNoValue = phoneNo.getText().toString().trim();
        String mailValue = mail.getText().toString().trim();
        String idValue = id.getText().toString().trim();
        String licenseValue = license.getText().toString().trim();
        String bankNoValue = bankNo.getText().toString().trim();
        String bankNameValue = bankName.getText().toString().trim();
        Uri imageUriAfterSelected = _localPictureUrl;
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

        if (TextUtils.isEmpty(bankNameValue)) {
            showToast("Please enter choose your bank!");
            return false;
        }

        if (imageUriAfterSelected == null) {
            showToast("Please select picture");
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