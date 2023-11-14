package com.example.ecommerce.User.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}