package com.example.ecommerce.User.Activities;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;

import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Fragments.HistoryFragmentUser;
import com.example.ecommerce.User.Fragments.HomeFragmentUser;
import com.example.ecommerce.User.Fragments.ProfileFragmentUser;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivityUser extends AppCompatActivity {

    ActivityMainBinding binding;
    String phone, name;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            phone = extras.getString("phone_number");
            name = extras.getString("user_name");
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            preferences.edit().putString("phone_number", phone).apply();
            preferences.edit().putString("user_name", name).apply();
//            Log.d("Phone No", phoneNumber);
//            Log.d("Username", userName);
        }

        Order order=new Order();
        order.setClientNo(phone);
        order.setClientName(name);

        Bundle orderBundle = new Bundle();
        orderBundle.putSerializable("order", order);

        HomeFragmentUser homeFragmentUser = new HomeFragmentUser();
        homeFragmentUser.setArguments(orderBundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                // Permission already granted
                // Continue with your app logic
            } else {
                // Request location permission
                requestLocationPermission();
            }}

        replaceFragment(homeFragmentUser);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(homeFragmentUser);
                    break;
                case R.id.history:
                    replaceFragment(new HistoryFragmentUser());
                    break;
                case R.id.profile:
                    ProfileFragmentUser profileFragmentUser = new ProfileFragmentUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone_number", phone);
                    bundle.putString("user_name", name);
                    profileFragmentUser.setArguments(bundle);
                    replaceFragment(profileFragmentUser);
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
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Continue with your app logic
            } else {
                // Permission denied
                // Handle accordingly (e.g., show a message, disable location-related features)
            }
        }
    }
}