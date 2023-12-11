package com.example.ecommerce.Employee.Driver.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.Employee.Driver.Fragments.HistoryFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.HomeFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.ProfileFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.WalletFragmentDriver;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Fragments.ProfileFragmentUser;
import com.example.ecommerce.databinding.ActivityMainDriverBinding;

public class MainActivityDriver extends AppCompatActivity {
    String key_driver,orderId;
    String fragment;
    ActivityMainDriverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDriverBinding.inflate(getLayoutInflater());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key_driver = extras.getString("key_driver");
            orderId = extras.getString("orderId");
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            preferences.edit().putString("driver_number",key_driver).apply();
        }
        Log.e("Home",key_driver);
        setContentView(binding.getRoot());


        HomeFragmentDriver homeFragmentDriver = new HomeFragmentDriver();
        HistoryFragmentDriver historyFragmentDriver = new HistoryFragmentDriver();
        ProfileFragmentDriver profileFragmentDriver = new ProfileFragmentDriver();
        Bundle bundle = new Bundle();
        bundle.putString("key_driver", key_driver);
        bundle.putString("orderId", orderId);
        homeFragmentDriver.setArguments(bundle);
        profileFragmentDriver.setArguments(bundle);
        replaceFragment(homeFragmentDriver);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(homeFragmentDriver);
                    break;


                case R.id.history:
                    replaceFragment(new HistoryFragmentDriver());
                    break;


                case R.id.wallet:
                    replaceFragment(new WalletFragmentDriver());
                    break;


                case R.id.profile:
                    replaceFragment(profileFragmentDriver);
                    break;


            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}