package com.example.ecommerce.Employee.Driver.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.ecommerce.Employee.Driver.Fragments.HistoryFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.HomeFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.ProfileFragmentDriver;
import com.example.ecommerce.Employee.Driver.Fragments.WalletFragmentDriver;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Fragments.ProfileFragmentUser;
import com.example.ecommerce.databinding.ActivityMainDriverBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivityDriver extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    String email_driver;
    ActivityMainDriverBinding binding;
    private GoogleMap mMap_Driver;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDriverBinding.inflate(getLayoutInflater());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email_driver = extras.getString("email_driver");
        }
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragmentDriver());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mMapDriver);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    HomeFragmentDriver profileFragmentDriver = new HomeFragmentDriver();
                    Bundle bundle = new Bundle();
                    bundle.putString("email_driver", email_driver);
                    profileFragmentDriver.setArguments(bundle);
                    replaceFragment(profileFragmentDriver);
                    break;


                case R.id.history:
                    replaceFragment(new HistoryFragmentDriver());
                    break;


                case R.id.wallet:
                    replaceFragment(new WalletFragmentDriver());
                    break;


                case R.id.profile:
                    replaceFragment(new ProfileFragmentDriver());
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

    //update location
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap_Driver = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        buildGoogleAPIClient();
        mMap_Driver.setMyLocationEnabled(true);

    }

    protected synchronized void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation =location;
        LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
        mMap_Driver.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap_Driver.animateCamera(CameraUpdateFactory.zoomTo(11));

        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversInfo");

        GeoFire geoFire= new GeoFire(ref);
        geoFire.setLocation(userId, new GeoLocation(location.getLatitude(),location.getLongitude()));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversInfo");

        GeoFire geoFire= new GeoFire(ref);
       geoFire.removeLocation(userId);
    }
}