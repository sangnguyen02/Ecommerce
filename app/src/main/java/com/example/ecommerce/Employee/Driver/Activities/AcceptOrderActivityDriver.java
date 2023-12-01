package com.example.ecommerce.Employee.Driver.Activities;
import com.example.ecommerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class AcceptOrderActivityDriver extends AppCompatActivity {

    String idOrder;
    String apigeofy = "3e150f47e1ba4055b8c06a09061ea420";

    TextView customerPhoneTextView, paymentMethodTextView, vehicleTypeTextView,
            pickUpLocationTextView, destinationTextView, priceTextView;
    MaterialButton acceptBtn, cancelBtn;
    View showSnackBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bottom_sheet_order_pop_up);
        idOrder = getIntent().getStringExtra("idOrder");

        customerPhoneTextView = findViewById(R.id.tv_uPhone);
        paymentMethodTextView = findViewById(R.id.tv_method);
        vehicleTypeTextView = findViewById(R.id.tv_typeVehicle);
        pickUpLocationTextView = findViewById(R.id.tv_From);
        destinationTextView = findViewById(R.id.tv_To);
        priceTextView = findViewById(R.id.tv_price);
        showSnackBarView = findViewById(android.R.id.content);
        acceptBtn = findViewById(R.id.acceptBook_btn);
        cancelBtn = findViewById(R.id.cancelBook_btn);

        orderInfoDisplay(customerPhoneTextView, paymentMethodTextView, vehicleTypeTextView,
                pickUpLocationTextView, destinationTextView, priceTextView);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrder();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });
    }

    private void orderInfoDisplay(final TextView customerPhoneTextView,
                                  final TextView paymentMethodTextView, final TextView vehicleTypeTextView,
                                  final TextView pickUpLocationTextView, final TextView destinationTextView,
                                  final TextView priceTextView) {
        Log.e("Order", "Find database");
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(idOrder);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String paymentMethod = dataSnapshot.child("paymentMethod").getValue().toString();
                    Log.e("Order", paymentMethod);
                    String phoneUser = dataSnapshot.child("clientNo").getValue().toString();
                    Log.e("Order", phoneUser);
                    String price = dataSnapshot.child("price").getValue().toString();
                    Log.e("Order", price);
                    String vehicleType = dataSnapshot.child("vehicleType").getValue().toString();
                    Log.e("Order", vehicleType);
                    String pickupLocationLat = dataSnapshot.child("pickupLocation_Latitude").getValue().toString();
                    String pickupLocationLong = dataSnapshot.child("pickupLocation_Longtitude").getValue().toString();
                    String destinationLat = dataSnapshot.child("destination_Latitude").getValue().toString();
                    String destinationLong = dataSnapshot.child("destination_Longtidue").getValue().toString();

                    new ReverseGeocodingTask(pickUpLocationTextView).execute(Double.valueOf(pickupLocationLat), Double.valueOf(pickupLocationLong));
                    new ReverseGeocodingTask(destinationTextView).execute(Double.valueOf(destinationLat), Double.valueOf(destinationLong));

                    customerPhoneTextView.setText(phoneUser);
                    paymentMethodTextView.setText(paymentMethod);
                    vehicleTypeTextView.setText(vehicleType);
                    priceTextView.setText(price);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Order", "Cannot find database");
            }
        });
    }

    private void cancelOrder() {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("OrderInfo").child(idOrder);

        UsersRef.child("orderStatus").setValue("CANCELED").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Cancel successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to cancel", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void acceptOrder() {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("OrderInfo").child(idOrder);

        UsersRef.child("orderStatus").setValue("ACCEPT").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Accept confirmed", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(showSnackBarView, "Failed to accept", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private class ReverseGeocodingTask extends AsyncTask<Double, Void, String> {

        private final TextView textView;

        public ReverseGeocodingTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(Double... params) {
            double latitude = params[0];
            double longitude = params[1];

            try {
                String apiUrl = "https://api.geoapify.com/v1/geocode/reverse?lat=" + latitude + "&lon=" + longitude + "&apiKey=" + URLEncoder.encode(apigeofy, "UTF-8");
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Parse and handle the JSON response
                    return response.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null; // Handle the error according to your needs
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Update UI or handle the result as needed
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray features = jsonObject.getJSONArray("features");

                    if (features.length() > 0) {
                        JSONObject firstFeature = features.getJSONObject(0);
                        JSONObject properties = firstFeature.getJSONObject("properties");

                        // Extract the formatted address
                        String formattedAddress = properties.optString("formatted", "Address not available");
                        textView.setText(formattedAddress);
                        Log.e("Add:", formattedAddress);
                    } else {
                        textView.setText("Address not found");
                        Log.e("Add:", "Address not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.setText("Error parsing JSON");
                    Log.e("Add:", "Error parsing JSON");
                }
            }
        }
    }
}