package com.example.ecommerce.Employee.Driver.Fragments;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Employee.Driver.Activities.ReceiveOrderActivityDriver;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;

import com.example.ecommerce.Employee.Driver.ReverseGeocodingTask;
public class HomeFragmentDriver extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Context context;
    boolean valid_status=false;
    String errorMessage="Qualify";
    String key_driver, orderId;
    CardView layoutBottomSheetRequestPopUp;
    BottomSheetBehavior bottomSheetBehaviorRequestPopUp;
    View showSnackBarView;
    private boolean isMapReady = false;
    ImageView setAvailable, setUnavailable;
    TextView tvFrom, tvTo, tvPrice, tvMethod, tvUPhone, tvTypeVehicle, status_driver;
    MaterialButton acceptBook_btn, cancelBook_btn;
    public static final String CHANNEL_ID = "notification_driver";
    private DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;
    private GoogleMap mMap_Driver;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 seconds
    private LocationCallback locationCallback;
    View rootView;
    GoogleApiClient mGoogleAPIClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_driver, container, false);
        key_driver = getArguments().getString("key_driver");
        orderId = getArguments().getString("orderId");
        Log.e("HomeFrag", key_driver);
        requestLocationPermission();
        layoutBottomSheetRequestPopUp = rootView.findViewById(R.id.bottom_sheet_order_pop_up);
        bottomSheetBehaviorRequestPopUp = BottomSheetBehavior.from(layoutBottomSheetRequestPopUp);
        setAvailable = rootView.findViewById(R.id.img_connect);
        setUnavailable = rootView.findViewById(R.id.img_disconnect);
        bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_COLLAPSED);
        showSnackBarView = rootView.findViewById(android.R.id.content);
        // Find the TextViews in your bottom sheet layout
        tvFrom = rootView.findViewById(R.id.tv_From);
        tvTo = rootView.findViewById(R.id.tv_To);
        tvPrice = rootView.findViewById(R.id.tv_price);
        tvMethod = rootView.findViewById(R.id.tv_method);
        tvUPhone = rootView.findViewById(R.id.tv_uPhone);
        tvTypeVehicle = rootView.findViewById(R.id.tv_typeVehicle);
        acceptBook_btn = rootView.findViewById(R.id.acceptBook_btn);
        cancelBook_btn = rootView.findViewById(R.id.cancelBook_btn);
        status_driver = rootView.findViewById(R.id.status_home_driver);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mMapDriver);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ReverseGeocodingTask reverseGeocodingTask1 = new ReverseGeocodingTask(tvFrom);
        ReverseGeocodingTask reverseGeocodingTask2 = new ReverseGeocodingTask(tvTo);


        setStatus(status_driver);

        if (orderId != null) {
            Log.e("Update after notify",orderId);
            updateBottomSheet(orderId,reverseGeocodingTask1,reverseGeocodingTask2);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 500); // 1000 milliseconds = 1 second
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");

            // Add a ValueEventListener to listen for changes
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("HomeFrag", "searchForDatabase");
                    // This method will be called whenever data at the specified location changes
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String driverNo = snapshot.child("driverNo").getValue(String.class);
                        String orderStatus = snapshot.child("orderStatus").getValue(String.class);

                        if (driverNo.equals(key_driver) && orderStatus.equals("PENDING")) {
                            // Authentication successful
                            Order dataObject = snapshot.getValue(Order.class);
                            orderId=snapshot.getKey();
                            Toast.makeText(rootView.getContext(), "New order", Toast.LENGTH_SHORT).show();
                            updateBottomSheet(orderId,reverseGeocodingTask1,reverseGeocodingTask2);

                            bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_EXPANDED);
                            createNotificationChannel();
                            sendNotification();
                            break;
                        }
//                    }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("MainActivity", "Error: " + databaseError.getMessage());
                }
            });
        }
        setAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorMessage=="Qualify") setDriver_available();
                else Toast.makeText(rootView.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        setUnavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorMessage=="Qualify") setDriver_unavailable();
                else Toast.makeText(rootView.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        acceptBook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrder(reverseGeocodingTask1,reverseGeocodingTask2);
            }
        });
        cancelBook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });

        return rootView;
    }

    private void setDriver_available() {


        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        DriverRef.child("driverStatus").setValue("ACTIVE").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            } else {

            }
        });
    }

    private void setDriver_unavailable() {


        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        DriverRef.child("driverStatus").setValue("OFFLINE").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            } else {

            }
        });
    }
    private void acceptOrder(final ReverseGeocodingTask reverseGeocodingTask1,
                             final ReverseGeocodingTask reverseGeocodingTask2) {
        if (orderId!=null){
            DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(orderId);
            OrdersRef.child("orderStatus").setValue("ACCEPT");
        }

        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        DriverRef.child("driverStatus").setValue("BUSY").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            } else {

            }
        });
        if (orderId != null) {
            DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(orderId);
            OrdersRef.child("orderStatus").setValue("ACCEPT");
        }
        reverseGeocodingTask1.cancelTask();
        reverseGeocodingTask2.cancelTask();

    }

    private void cancelOrder() {
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);
        DriverRef.child("driverStatus").setValue("DENY").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                bottomSheetBehaviorRequestPopUp.setState(BottomSheetBehavior.STATE_COLLAPSED);

            } else {

            }
        });

        if (orderId != null) {
            DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference().child("Order").child(orderId);
            OrdersRef.child("orderStatus").setValue("CANCEL");
        }


    }

    private void setStatus(final TextView status_driver) {
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        DriverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("Profile Frag Color", "da tim thay ");
                    if (dataSnapshot.hasChild("driverStatus")) {
                        String statusDriver = dataSnapshot.child("driverStatus").getValue().toString();
                        status_driver.setText(statusDriver);

                        // Change text color based on the driver status
                        int textColor = checkDriverStatus(statusDriver);
                        status_driver.setTextColor(textColor);
                    }
                    else {
                        errorMessage="Your account is not existed";
                    }
                }
            }

            private int checkDriverStatus(String status) {
                int color;
                if ("ACTIVE".equals(status)) {
                    errorMessage="Qualify";
                    // Set color to green for "ACTIVE" status
                    color = ContextCompat.getColor(context, R.color.teal_200); // Change R.color.green to your color resource
                } else if ("OFFLINE".equals(status)||"DENY".equals(status)) {
                    errorMessage="Qualify";
                    // Set color to red for "BUSY" status
                    color = ContextCompat.getColor(context, R.color.rectangle_1_color); // Change R.color.red to your color resource
                }
                else {
                    // Default color (you can set it to another color or handle other cases)
                    color = ContextCompat.getColor(context, R.color.background_snack_bar);
                    if ("BANNED".equals(status)) errorMessage="You was banned";
                    else if ("DEBT".equals(status)) errorMessage="Your need to charge your balance";
                }
                return color;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Profile Driver Fragment", "Error database");
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = (NotificationManager) requireActivity().getSystemService(requireContext().NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public void sendNotification() {
        Log.e("Home", "Check send notify");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("New Order Received")
                .setContentText("You have a new order.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Create an Intent for the notification to open an activity (if needed)
        Log.e("Dua key driver vao", "Bat dau");
        Intent intent = new Intent(requireContext(), ReceiveOrderActivityDriver.class);
        Bundle bundle = new Bundle();
        bundle.putString("key_driver", key_driver);
        bundle.putString("orderId", orderId);
        Log.e("Dua key driver vao", key_driver);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        // Show the notification
        notificationManager = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Home", "Check Failed");
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void updateBottomSheet(String orderId, final ReverseGeocodingTask reverseGeocodingTask1,
                                   final ReverseGeocodingTask reverseGeocodingTask2) {
        // Extract relevant information from the Order object and update the TextViews
        Log.e("Set information", "Starting");

        if (orderId != null) {
            Log.e("Set information", "True");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");

            // Add a ValueEventListener to listen for changes
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("HomeFrag", "searchForDatabase");
                    // This method will be called whenever data at the specified location changes
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String orderNum =snapshot.getKey();
                        String orderStatus= snapshot.child("orderStatus").getValue(String.class);
                        if (orderNum.equals(orderId)&&orderStatus.equals("PENDING")) {
                            // Authentication successful
                            Order dataObject = snapshot.getValue(Order.class);
                            Double pickLong = Double.parseDouble(dataObject.getPickupLocation_Longtitude());
                            Double pickLa = Double.parseDouble(dataObject.getPickupLocation_Latitude());
                            Double destLong = Double.parseDouble(dataObject.getDestination_Longtidue());
                            Double destLa = Double.parseDouble(dataObject.getDestination_Latitude());
                            Log.e("HomeFrag", String.valueOf(pickLong));
                            reverseGeocodingTask1.execute(pickLa,pickLong);
                            reverseGeocodingTask2.execute(destLa,destLong);

                            tvPrice.setText(String.valueOf(dataObject.getPrice()));
                            tvMethod.setText(String.valueOf(dataObject.getPaymentMethod()));
                            tvUPhone.setText(dataObject.getClientNo());
                            tvTypeVehicle.setText(String.valueOf(dataObject.getVehicleType()));
                            break;
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("MainActivity", "Error: " + databaseError.getMessage());
                }
            });


        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap_Driver = googleMap;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap_Driver.setMyLocationEnabled(true);
//            updateMap();
//            startLocationUpdates(); // Start location updates when the map is ready
            buildGoogleAPIClient();
        } else {
            requestLocationPermission();
        }
    }

    protected synchronized void buildGoogleAPIClient() {
        mGoogleAPIClient= new GoogleApiClient.Builder(rootView.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleAPIClient.connect();

    }


    private void updateMap() {
        // Get the last known location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMap_Driver != null) {
                    mMap_Driver.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
                } else {
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mMapDriver);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this);
                    }
                }
            }
        });
    }


    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            // You can proceed to show the map and get the current location
            updateMap();
        }
    }

    private void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Handle the updated location as needed
                        Log.d("Location Update", "Lat: " + location.getLatitude() + ", Long: " + location.getLongitude());
                        // You can update the map or perform other actions with the new location
                    }
                }
            }
        };

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_UPDATE_INTERVAL);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onStop() {
        super.onStop();
        String Userid=key_driver;
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversInfo");
        GeoFire geoFire= new GeoFire(ref);

        geoFire.removeLocation(Userid);
    }

    private void stopLocationUpdates() {
        if (locationCallback != null) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void updateLocationOnFirebase(double latitude, double longitude) {
        // Assuming you have a "DriversInfo" node in your database
        DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(key_driver);

        // Update the location fields in your "DriversInfo" node
        driverLocationRef.child("currentLocation").setValue(new GeoLocation(latitude, longitude));

        // Update the location in GeoFire

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleAPIClient, mLocationRequest, (LocationListener) rootView.getContext());
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleAPIClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation=location;
        LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
        mMap_Driver.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap_Driver.animateCamera(CameraUpdateFactory.zoomTo(15));
        String Userid=key_driver;
       // DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversInfo").child(Userid);
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference("DriversInfo").child(Userid);

// Get the current location (replace these with actual latitude and longitude values)
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

// Update GeoFire location
        GeoFire geoFire = new GeoFire(driverRef);
        geoFire.setLocation("currentLocation", new GeoLocation(latitude, longitude), (key, error) -> {
            if (error == null) {
                Log.d("GeoFire", "Location updated successfully");
            } else {
                Log.e("GeoFire", "Error updating location: " + error.getMessage());
            }
        });
    }

}