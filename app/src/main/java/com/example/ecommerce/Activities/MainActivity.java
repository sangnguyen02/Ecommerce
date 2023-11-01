package com.example.ecommerce.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerce.Fragments.HistoryFragment;
import com.example.ecommerce.Fragments.HomeFragment;
import com.example.ecommerce.Fragments.PaymentFragment;
import com.example.ecommerce.Fragments.ProfileFragment;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;


                case R.id.history:
                    replaceFragment(new HistoryFragment());
                    break;


                case R.id.payment:
                    replaceFragment(new PaymentFragment());
                    break;


                case R.id.profile:
                    replaceFragment(new ProfileFragment());
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