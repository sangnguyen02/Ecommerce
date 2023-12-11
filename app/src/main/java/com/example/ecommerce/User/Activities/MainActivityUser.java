package com.example.ecommerce.User.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Fragments.HistoryFragmentUser;
import com.example.ecommerce.User.Fragments.HomeFragmentUser;
import com.example.ecommerce.User.Fragments.ProfileFragmentUser;
import com.example.ecommerce.databinding.ActivityMainBinding;

public class MainActivityUser extends AppCompatActivity {

    ActivityMainBinding binding;
    String phone, name;

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
}