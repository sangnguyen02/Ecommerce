package com.example.ecommerce.Employee.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverAccount;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.Models.User;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import utils.PasswordHasher;
import utils.SendMailTask;

public class ManageRegisterDriverDetailActivityAdmin extends AppCompatActivity {

    private CircleImageView imageRegister;
    private EditText editTextFullname;
    private EditText editTextPhoneNo;
    private EditText editTextMail;
    private EditText editTextID;
    private EditText editTextLicense;
    private EditText editTextBankNo;
    private EditText editTextBankName;
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
        editTextBankName = findViewById(R.id.editText_bankname);
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
        editTextBankName.setText(driverInfos.getBankName());

        DriverInfos finalDriverInfos = driverInfos;
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptDriverRegistration(finalDriverInfos.getPhoneNo());
                SendAccountEmail(finalDriverInfos.getMail(),finalDriverInfos.getId(),finalDriverInfos.getName());
                CreateDriverAccount(finalDriverInfos.getPhoneNo(),finalDriverInfos.getMail(),finalDriverInfos.getId());
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

    private void AcceptDriverRegistration(String driverPhoneNo){

        // Retrieve the reference to the specific driver
        DatabaseReference specificDriverRef = driversRef.child(driverPhoneNo);

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
                        currentDriver.setDriverStatus(MyEnum.DriverStatus.OFFLINE);
                        currentDriver.setBalance(0);
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

    private void SendAccountEmail(String driverEmail, String driverID, String driverName){
        String emailSubject = "Welcome To Driver Hub";
        String emailMessage = "Dear " + driverName + ",\n" +
                "\n" +
                "Congratulations! You have successfully completed the registration process and are now officially a registered driver with our platform. We are thrilled to welcome you to our community of drivers. As a registered driver, you now have access to a range of features and opportunities to enhance your experience. Get ready to embark on a journey with us, providing valuable services and contributing to the success of our platform. Thank you for choosing to be part of our community, and we look forward to a successful and rewarding partnership.\n" +
                "\n" +
                "Your username is : " + driverEmail + "\n" +
                "Your password is your id" +
                //"Your password is : " + driverID + "\n" +
                "\n" +
                "Make sure to deposit at least 25$ into your driver account to start your journey at Driver Hub" + "\n"+
                "Best regards,";

        new SendMailTask(this, driverEmail, emailSubject, emailMessage).execute();
    }

    private void CreateDriverAccount(String driverPhone, String driverMail, String driverID){

        //HashPassword (driverID)
        String password = driverID;
        String hashedPassword = PasswordHasher.hashPassword(password);

        //Upload to Firebase
        DriverAccount driverAccount = new DriverAccount(driverPhone,driverMail,hashedPassword);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriversAccount");
        String key = driverPhone;
        databaseReference.child(key).setValue(driverAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"DriverAccountCreated",Toast.LENGTH_SHORT).show();
                } else {
                    Exception e = task.getException();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}