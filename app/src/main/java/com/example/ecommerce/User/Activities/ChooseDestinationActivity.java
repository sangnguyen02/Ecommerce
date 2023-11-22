package com.example.ecommerce.User.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;

public class ChooseDestinationActivity extends AppCompatActivity {

    private SearchView urlocation, urdestination;
    private ImageView home, work;
    private MaterialButton confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_destination);

        urlocation = findViewById(R.id.searchView_yourLocation);
        urdestination = findViewById(R.id.searchView_yourDest);
        home = findViewById(R.id.img_home_Location);
        work = findViewById(R.id.img_work_Location);
        confirm = findViewById(R.id.confirmChoosen_btn);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDestinationActivity.this, BookDriverActivityUser.class);
                startActivity(intent);
            }
        });

    }
}