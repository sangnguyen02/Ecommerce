package com.example.ecommerce.User.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivityUser extends AppCompatActivity {

    EditText edit_fullname, edit_phone;

    MaterialButton save_btn;
    View showSnackBarView;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);
        phone = getIntent().getStringExtra("phone");
        edit_fullname = findViewById(R.id.editText_fullname);
        edit_phone = findViewById(R.id.editText_phoneNo);
        save_btn = findViewById(R.id.save_btn);
        showSnackBarView = findViewById(android.R.id.content);
        userInfoDisplay(edit_fullname, edit_phone);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadInfor();
            }
        });


    }

    private void userInfoDisplay(final EditText edt_fullName, final EditText edt_phone)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String nameUser = dataSnapshot.child("nameUser").getValue().toString();
                    String phoneUser = dataSnapshot.child("phoneNum").getValue().toString();

                    edt_fullName.setText(nameUser);
                    edt_phone.setText(phoneUser);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void uploadInfor() {
        String newName = edit_fullname.getText().toString();

        if (TextUtils.isEmpty(newName)) {
            Snackbar snackbar = Snackbar.make(showSnackBarView, "Name is mandatory", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.child("nameUser").setValue(newName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Update profile successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
                finish();
            }
            else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to update profile", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}