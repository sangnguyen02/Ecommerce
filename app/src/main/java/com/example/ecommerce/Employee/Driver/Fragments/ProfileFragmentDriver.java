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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Employee.Driver.Activities.EditProfileActivityDriver;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.EditProfileActivityUser;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragmentDriver extends Fragment {
    String key_driver;
    MaterialButton edit_btn;
    TextView driverName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_driver, container, false);
        key_driver = getArguments().getString("key_driver");
        Log.e("ProfileFrag", key_driver);
        edit_btn = rootView.findViewById(R.id.editProfile_btn);
        driverName = rootView.findViewById(R.id.tv_fullname);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), EditProfileActivityDriver.class);
                intent.putExtra("key_driver", key_driver);
                Log.e("ProfileFrag","Gui key driver: "+key_driver);
                startActivity(intent);
            }
        });

        if (key_driver!=null){
            driverInfoDisplay(driverName);
        }

        return rootView;
    }


    private void driverInfoDisplay(final TextView edt_driverName)
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