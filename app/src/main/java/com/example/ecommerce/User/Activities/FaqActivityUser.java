package com.example.ecommerce.User.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerce.R;

public class FaqActivityUser extends AppCompatActivity {

    TextView q1, a1, q2, a2, q3, a3, q4, a4, q5, a5, q6, a6, q7, a7, q8, a8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_user);
        q1 = findViewById(R.id.titleQuestion1);
        a1 = findViewById(R.id.answerQuesntion1);

        q2 = findViewById(R.id.titleQuestion2);
        a2 = findViewById(R.id.answerQuesntion2);

        q3 = findViewById(R.id.titleQuestion3);
        a3 = findViewById(R.id.answerQuesntion3);

        q4 = findViewById(R.id.titleQuestion4);
        a4 = findViewById(R.id.answerQuesntion4);

        q5 = findViewById(R.id.titleQuestion5);
        a5 = findViewById(R.id.answerQuesntion5);

        q6 = findViewById(R.id.titleQuestion6);
        a6 = findViewById(R.id.answerQuesntion6);

        q7 = findViewById(R.id.titleQuestion7);
        a7 = findViewById(R.id.answerQuesntion7);

        q8 = findViewById(R.id.titleQuestion8);
        a8 = findViewById(R.id.answerQuesntion8);


        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a1);
            }
        });

        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a2);
            }
        });

        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a3);
            }
        });

        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a4);
            }
        });

        q5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a5);
            }
        });

        q6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a6);
            }
        });

        q7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a7);
            }
        });

        q8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a8);
            }
        });
    }

    private void toggleVisibility(TextView textView) {
        if (textView.getVisibility() == View.VISIBLE) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }
}