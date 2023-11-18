package com.example.ecommerce.Employee.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageRegisterDriverDetailActivityAdmin extends AppCompatActivity {

    private CircleImageView imageRegister;
    private EditText editTextFullname;
    private EditText editTextPhoneNo;
    private EditText editTextMail;
    private EditText editTextID;
    private EditText editTextLicense;
    private EditText editTextBankNo;
    private com.google.android.material.button.MaterialButton acceptBtn;
    private com.google.android.material.button.MaterialButton denyBtn;
    private DatabaseReference driversRef;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_register_driver_detail_admin);

        imageRegister = findViewById(R.id.imageRegister);
        editTextFullname = findViewById(R.id.editText_fullname);
        editTextPhoneNo = findViewById(R.id.editText_phoneNo);
        editTextMail = findViewById(R.id.editText_mail);
        editTextID = findViewById(R.id.editText_ID);
        editTextLicense = findViewById(R.id.editText_license);
        editTextBankNo = findViewById(R.id.editText_bankNo);

        acceptBtn = findViewById(R.id.accept_btn);
        denyBtn = findViewById(R.id.deny_btn);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        driversRef = database.getReference("DriversInfo");

        Bundle extras = getIntent().getExtras();
        DriverInfos driverInfos = new DriverInfos();
        if (extras != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                //driverInfos = extras.getSerializable("RegisterDriver", DriverInfos.class);
                driverInfos = (DriverInfos) extras.get("RegisterDriver");
            }else{
                driverInfos = (DriverInfos) extras.get("RegisterDriver");
            }
        }

        Picasso.get().load(driverInfos.getPicture()).into(imageRegister);
        editTextFullname.setText(driverInfos.getName());
        editTextPhoneNo.setText(driverInfos.getPhoneNo());
        editTextMail.setText(driverInfos.getMail());
        editTextID.setText(driverInfos.getId());
        editTextLicense.setText(driverInfos.getLicense());
        editTextBankNo.setText(driverInfos.getBankAccount());


        DriverInfos finalDriverInfos = driverInfos;
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptDriverRegistration(finalDriverInfos.getId());
                setResult(RESULT_OK);
                finish();
            }
        });

        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private void AcceptDriverRegistration( String driverId){

        // Retrieve the reference to the specific driver
        DatabaseReference specificDriverRef = driversRef.child(driverId);

        // Retrieve the current data of the specific driver
        specificDriverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the driver exists in the database
                if (dataSnapshot.exists()) {
                    // Get the current driver object
                    DriverInfos currentDriver = dataSnapshot.getValue(DriverInfos.class);

                    // Update the attribute of the object locally
                    if (currentDriver != null) {
                        currentDriver.setDriverStatus(MyEnum.DriverStatus.ACTIVE);
                        // Set the updated object back to the Firebase reference
                        specificDriverRef.setValue(currentDriver);
                    }
                }
                //return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
            }
        });

    }
}