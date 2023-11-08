package com.example.ecommerce.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ecommerce.Fragments.HistoryFragment;
import com.example.ecommerce.Fragments.HomeFragment;
import com.example.ecommerce.Fragments.PaymentFragment;
import com.example.ecommerce.Fragments.ProfileFragment;

import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

String phoneString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve the extra value
            phoneString = intent.getStringExtra("phoneString");
            Log.e("phoneString",phoneString);
            // Use the received value as needed
        }
        else {
            Log.e("phoneString","Null Object");
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            Bundle bundle = new Bundle(); // Create a new Bundle to hold the argument

            bundle.putString("phoneString",phoneString );
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();

                    break;

                case R.id.history:
                    fragment = new HistoryFragment();
                    break;

                case R.id.payment:
                    fragment = new PaymentFragment();
                    break;

                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;

                default:
                    return false;
            }

            fragment.setArguments(bundle); // Pass the bundle as arguments to the fragment
            replaceFragment(fragment);
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