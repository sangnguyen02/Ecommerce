package com.example.ecommerce.Employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.LoginActivityUser;
import com.google.android.material.button.MaterialButton;

public class LoginActivityEmployee extends AppCompatActivity {

    EditText emailInput, passwordInput;
    MaterialButton loginBtn;
    TextView signInAsUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_employee);

        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.login_btn);
        signInAsUser = findViewById(R.id.signInUser);


        signInAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivityEmployee.this, LoginActivityUser.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}