package com.example.ecommerce.Employee.Admin.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.Employee.Admin.Fragments.ManageDriverFragmentAdmin;
import com.example.ecommerce.Employee.Admin.Fragments.StatisticRevenueFragmentAdmin;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityMainDriverBinding;

public class MainActivityAdmin extends AppCompatActivity {

    ActivityMainDriverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ManageDriverFragmentAdmin());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.manageDriver:
                    replaceFragment(new ManageDriverFragmentAdmin());
                    break;

                case R.id.statistic:
                    replaceFragment(new StatisticRevenueFragmentAdmin());
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