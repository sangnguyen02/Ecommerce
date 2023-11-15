package com.example.ecommerce.User.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.ecommerce.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class MapActivityUser extends AppCompatActivity implements OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener {
    public static final String API_KEY = "AIzaSyAgiRVzm2vQ9kGUQQxp7trXj5AIbwV5NU0";

    private GoogleMap mMap_User;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    public double distanceInKm = 0;
    private FusedLocationProviderClient fusedLocationClient;

    private SearchView mapLocationSearch;
    private SearchView mapDestinationSearch;
    private Marker searchMarkerLocation;
    //    private Marker searchMarkerDestination;
    //use for directions api
    private LatLng userLocationLatLng;
    //use for directions api
    private LatLng userDestinationtionLatLng;

    private List<Polyline> polylines = null;

    ArrayList<Marker> markerList = new ArrayList<>();
    private Button RouteBTN, PriceBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_user);
        mapLocationSearch = findViewById(R.id.mapLocation);
        mapDestinationSearch = findViewById(R.id.mapDestination);
        RouteBTN = findViewById(R.id.routebtn);
        PriceBTN = findViewById(R.id.caculatePrice);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_user);
        mapFragment.getMapAsync(this);
        //onStartComplete();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        onSearchLocation();
        onSearchDestination();
        Draw();
    }

    private void onSearchLocation() {
        mapLocationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                clearOldMarkers();
                String location = mapLocationSearch.getQuery().toString();
                if (searchMarkerLocation != null) {
                    searchMarkerLocation.remove();
                }
                // Geocoding service to convert the query into coordinates
                Geocoder geocoder = new Geocoder(MapActivityUser.this);
                try {
                    List<Address> addressList = geocoder.getFromLocationName(s, 1);
                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        userLocationLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                        searchMarkerLocation = mMap_User.addMarker(new MarkerOptions()
                                .position(userLocationLatLng)
                                .title(s)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        mMap_User.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocationLatLng, 18));
                        //drawDirections(userLocationLatLng, userDestinationtionLatLng);
                    } else {
                        // Handle the case where no results were found
                        Toast.makeText(MapActivityUser.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MapActivityUser.this, "Error while geocoding", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // onStartCompleteForSearchLocal();
                return false;
            }
        });
    }

    private void onSearchDestination() {
        mapDestinationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                clearOldMarkers();
                String location = mapDestinationSearch.getQuery().toString();
                if (searchMarkerLocation != null) {
                    searchMarkerLocation.remove();
                }
                // Geocoding service to convert the query into coordinates
                Geocoder geocoder = new Geocoder(MapActivityUser.this);
                try {
                    List<Address> addressList = geocoder.getFromLocationName(s, 1);
                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        userDestinationtionLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                        searchMarkerLocation = mMap_User.addMarker(new MarkerOptions()
                                .position(userDestinationtionLatLng)
                                .title(s));
                        mMap_User.animateCamera(CameraUpdateFactory.newLatLngZoom(userDestinationtionLatLng, 18));
                        //drawDirections(userLocationLatLng, userDestinationtionLatLng);
                    } else {
                        // Handle the case where no results were found
                        Toast.makeText(MapActivityUser.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MapActivityUser.this, "Error while geocoding", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // onStartCompleteForSearchLocal();
                return false;
            }
        });
    }

    private void clearOldMarkers() {
        for (Marker marker : markerList) {
            marker.remove();
        }
        markerList.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap_User = googleMap;
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the user's current location and move the camera to that location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            getCurrentLocation(location);
                        }
                    });
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    public void getCurrentLocation(Location location) {
        if (location != null) {
            userLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocationLatLng, 18);
            mMap_User.moveCamera(cameraUpdate);

            // Add a marker for the user's location
            mMap_User.addMarker(new MarkerOptions()
                    .position(userLocationLatLng)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            // Reverse geocode the location to get the address
            Geocoder geocoder = new Geocoder(MapActivityUser.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String currentLocationText = address.getAddressLine(0);

                    // Assuming mapLocation is the ID of your SearchView

                    mapLocationSearch.setQuery(currentLocationText, false);
                } else {
                    Toast.makeText(MapActivityUser.this, "Address not available", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MapActivityUser.this, "Geocoding error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MapActivityUser.this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MapActivityUser.this, "Finding Route...", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(userLocationLatLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < arrayList.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(com.google.android.libraries.places.R.color.quantum_grey));
                polyOptions.width(7);
                polyOptions.addAll(arrayList.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap_User.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);
            } else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap_User.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap_User.addMarker(endMarker);

        // Get the shortest route
        Route shortestRoute = arrayList.get(shortestRouteIndex);

        // Access the distance of the route
        double distance = shortestRoute.getDistanceValue(); // distance in meters

        // You can convert the distance to other units if needed
        distanceInKm = distance / 1000.0;
//        double distanceInMiles = distance / 1609.34;

        // Display the distance on the map
        String distanceText = String.format("Distance: %.2f km", distanceInKm);

        Toast.makeText(MapActivityUser.this, distanceText, Toast.LENGTH_LONG).show();

//        Intent intent=new Intent(MapActivityUser.this,CaculateMoneyActivity.class);
//        intent.putExtra("Distance",distanceInKm);
//        startActivity(intent);
        PriceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivityUser.this, CaculateMoneyActivity.class);
                intent.putExtra("Distance", distanceInKm);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(userLocationLatLng, userDestinationtionLatLng);

    }

    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(MapActivityUser.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(API_KEY)  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(userLocationLatLng, userDestinationtionLatLng);
    }

    public void Draw() {
        RouteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Findroutes(userLocationLatLng, userDestinationtionLatLng);
            }
        });
    }

//public void onStartCompleteForSearchLocal()
//{
//    // Initialize Places. Make sure to use your own API Key.
//    Places.initialize(getApplicationContext(), "AIzaSyAgiRVzm2vQ9kGUQQxp7trXj5AIbwV5NU0");
//    // Create a new Places client instance.
//    PlacesClient placesClient = Places.createClient(this);
//
//    // Initialize AutocompleteSupportFragment
//    AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//            getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);
//
//    // Specify the types of place data you want to retrieve.
//    autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//    // Set the country filter (optional).
//    autocompleteFragment.setCountries("VN");
//    autocompleteFragment.setHint("Search for a place");
//
//    // Set the type filter (optional).
//    autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
//
//    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//        @Override
//        public void onPlaceSelected(@NonNull Place place) {
//            LatLng location = place.getLatLng();
//            if (location != null) {
//                clearOldMarkers();
//                Marker newMarker = mMap_User.addMarker(new MarkerOptions().position(location).title(place.getName()));
//                markerList.add(newMarker);
//                mMap_User.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
//            }
//
//
//        }
//
//        @Override
//        public void onError(@NonNull Status status) {
//            // Handle any errors
//
//        }
//    });
//    autocompleteFragment.getView().setBackgroundResource(R.drawable.autocomplete_fragment_overlay); // Customize the overlay appearance
//    autocompleteFragment.getView().setElevation(10); // Set the elevation to control the overlay depth
//}
}