package com.example.ecommerce.Employee.Driver.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Employee.Driver.Activities.EditProfileActivityDriver;
import com.example.ecommerce.Employee.Driver.Activities.FaqActivityDriver;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.SplashActivity;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragmentDriver extends Fragment {
    String key_driver;
    MaterialButton edit_btn, faq_btn, log_out;
    TextView driverName;
    ImageView driverImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_driver, container, false);
        key_driver = getArguments().getString("key_driver");
        Log.e("ProfileFrag", key_driver);
        edit_btn = rootView.findViewById(R.id.editProfile_btn_driver);
        faq_btn = rootView.findViewById(R.id.faq_btn_driver);
        log_out = rootView.findViewById(R.id.log_out_btn_driver);
        driverName = rootView.findViewById(R.id.tv_fullname);
        driverImage = rootView.findViewById(R.id.img_driver);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), EditProfileActivityDriver.class);
                intent.putExtra("key_driver", key_driver);
                Log.e("ProfileFrag","Gui key driver: "+key_driver);
                startActivity(intent);
            }
        });

        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), FaqActivityDriver.class);
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), SplashActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        if (key_driver!=null){
            driverInfoDisplay(driverName,driverImage);
        }

        return rootView;
    }


    private void driverInfoDisplay(final TextView edt_driverName, final ImageView image_view)
    {
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        DriverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Log.e("Profile Frag","da tim thay ");
                    if (dataSnapshot.hasChild("name")){
                        String nameDriver = dataSnapshot.child("name").getValue().toString();
                        edt_driverName.setText(nameDriver);
                        String picDriver = dataSnapshot.child("picture").getValue().toString();
                        if (picDriver != null && !picDriver.isEmpty()) {
                            Picasso.get().load(picDriver).into(image_view);
                        }

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Profile Driver Fragment","Error database");
            }
        });
    }
}