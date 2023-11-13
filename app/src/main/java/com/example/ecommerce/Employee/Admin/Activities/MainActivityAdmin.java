package com.example.ecommerce.Employee.Admin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.ecommerce.Employee.Admin.Fragments.ManageDriverFragmentAdmin;
import com.example.ecommerce.Employee.Admin.Fragments.StatisticRevenueFragmentAdmin;
import com.example.ecommerce.Employee.Driver.Fragments.HistoryFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.HomeFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.ProfileFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.WalletFragmentDriver;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Fragments.HomeFragmentUser;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivityAdmin extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

                default:
                    replaceFragment(new ManageDriverFragmentAdmin());
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