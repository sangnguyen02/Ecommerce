package com.example.ecommerce.Employee.Driver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerce.Employee.Driver.Fragments.HistoryFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.HomeFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.ProfileFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.WalletFragmentDriver;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Fragments.HistoryFragmentUser;
import com.example.ecommerce.User.Fragments.HomeFragmentUser;
import com.example.ecommerce.User.Fragments.PaymentFragmentUser;
import com.example.ecommerce.User.Fragments.ProfileFragmentUser;
import com.example.ecommerce.databinding.ActivityMainAdminBinding;
import com.example.ecommerce.databinding.ActivityMainDriverBinding;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivityDriver extends AppCompatActivity {

    ActivityMainDriverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragmentDriver());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragmentDriver());
                    break;


                case R.id.history:
                    replaceFragment(new HistoryFragmentDriver());
                    break;


                case R.id.wallet:
                    replaceFragment(new WalletFragmentDriver());
                    break;


                case R.id.profile:
                    replaceFragment(new ProfileFragmentDriver());
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