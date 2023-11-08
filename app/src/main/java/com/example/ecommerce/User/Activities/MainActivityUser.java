package com.example.ecommerce.User.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerce.User.Fragments.HistoryFragmentUser;
import com.example.ecommerce.User.Fragments.HomeFragmentUser;
import com.example.ecommerce.User.Fragments.PaymentFragmentUser;
import com.example.ecommerce.User.Fragments.ProfileFragmentUser;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivityUser extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragmentUser());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragmentUser());
                    break;


                case R.id.history:
                    replaceFragment(new HistoryFragmentUser());
                    break;


                case R.id.payment:
                    replaceFragment(new PaymentFragmentUser());
                    break;


                case R.id.profile:
                    replaceFragment(new ProfileFragmentUser());
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