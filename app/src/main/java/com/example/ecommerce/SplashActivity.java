package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce.User.Activities.LoginActivityUser;
import com.google.android.material.button.MaterialButton;

public class SplashActivity extends AppCompatActivity {

    MaterialButton lets_goBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lets_goBtn = findViewById(R.id.lets_go_btn);
        lets_goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivityUser.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}