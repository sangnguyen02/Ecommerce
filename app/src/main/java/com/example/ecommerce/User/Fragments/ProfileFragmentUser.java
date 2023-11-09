package com.example.ecommerce.User.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.example.ecommerce.User.Activities.FaqActivityUser;
import com.example.ecommerce.User.Activities.RegisterDriverActivityUser;
import com.example.ecommerce.User.Activities.SavedLocationActivityUser;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragmentUser extends Fragment {

    TextView tv_fullname;
    MaterialButton editProfile, savedLocation, faq, registerDriver, logout;
    String phone, name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_fullname = rootView.findViewById(R.id.tv_fullname);
        editProfile = rootView.findViewById(R.id.editProfile_btn);
        savedLocation = rootView.findViewById(R.id.savedLocation_btn);
        faq = rootView.findViewById(R.id.faq_btn);
        registerDriver = rootView.findViewById(R.id.registerDriver_btn);
        logout = rootView.findViewById(R.id.log_out_btn);
        phone = getArguments().getString("phone_number");
        userInfoDisplay(tv_fullname);


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), EditProfileActivityUser.class);
                intent.putExtra("phone", phone);
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


    private void userInfoDisplay(final TextView fullName)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String nameUser = dataSnapshot.child("nameUser").getValue().toString();
                    tv_fullname.setText(nameUser);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}