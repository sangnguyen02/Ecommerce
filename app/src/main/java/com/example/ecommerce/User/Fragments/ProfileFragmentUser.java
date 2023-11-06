package com.example.ecommerce.User.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.example.ecommerce.User.Activities.FaqActivityUser;
import com.example.ecommerce.User.Activities.RegisterDriverActivityUser;
import com.example.ecommerce.User.Activities.SavedLocationActivityUser;
import com.google.android.material.button.MaterialButton;

public class ProfileFragmentUser extends Fragment {

    MaterialButton editProfile, savedLocation, faq, registerDriver, logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile = rootView.findViewById(R.id.editProfile_btn);
        savedLocation = rootView.findViewById(R.id.savedLocation_btn);
        faq = rootView.findViewById(R.id.faq_btn);
        registerDriver = rootView.findViewById(R.id.registerDriver_btn);
        logout = rootView.findViewById(R.id.log_out_btn);


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), EditProfileActivityUser.class);
                startActivity(intent);
            }
        });

        savedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), SavedLocationActivityUser.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), FaqActivityUser.class);
                startActivity(intent);
            }
        });

        registerDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), RegisterDriverActivityUser.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}