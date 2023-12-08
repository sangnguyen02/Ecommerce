package com.example.ecommerce.Employee.Driver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerce.R;

public class FaqActivityDriver extends AppCompatActivity {

    TextView q1, a1, q2, a2, q3, a3, q4, a4, q5, a5, q6, a6, q7, a7, q8, a8, q9, a9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_driver);

        q1 = findViewById(R.id.titleQuestion1_driver);
        a1 = findViewById(R.id.answerQuesntion1_driver);

        q2 = findViewById(R.id.titleQuestion2_driver);
        a2 = findViewById(R.id.answerQuesntion2_driver);

        q3 = findViewById(R.id.titleQuestion3_driver);
        a3 = findViewById(R.id.answerQuesntion3_driver);

        q4 = findViewById(R.id.titleQuestion4_driver);
        a4 = findViewById(R.id.answerQuesntion4_driver);

        q5 = findViewById(R.id.titleQuestion5_driver);
        a5 = findViewById(R.id.answerQuesntion5_driver);

        q6 = findViewById(R.id.titleQuestion6_driver);
        a6 = findViewById(R.id.answerQuesntion6_driver);

        q7 = findViewById(R.id.titleQuestion7_driver);
        a7 = findViewById(R.id.answerQuesntion7_driver);

        q8 = findViewById(R.id.titleQuestion8_driver);
        a8 = findViewById(R.id.answerQuesntion8_driver);

        q9 = findViewById(R.id.titleQuestion9_driver);
        a9 = findViewById(R.id.answerQuesntion9_driver);

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

        q9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility(a9);
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