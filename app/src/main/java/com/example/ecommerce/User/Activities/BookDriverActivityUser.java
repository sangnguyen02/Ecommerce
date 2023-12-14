package com.example.ecommerce.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.ecommerce.Enum.MyEnum;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.Models.Order;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;
import com.rey.material.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Adapters.CategoryAdapter;
import com.example.ecommerce.Models.CategoryPaymentMethod;
import com.example.ecommerce.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookDriverActivityUser extends AppCompatActivity {

    private CardView layoutBottomSheetBook, layoutBottomSheetWating, layoutBottomSheetDriverComing, layoutBottomSheetDriverArrived, layoutBottomSheetDriverEndTrip,layoutBottomSheetPaypal;
    private BottomSheetBehavior bottomSheetBehaviorBook, bottomSheetBehaviorWaiting, bottomSheetBehaviorDriverComing, bottomSheetBehaviorDriverArrived, bottomSheetBehaviorDriverEndTrip,bottomSheetBehaviorPaypal;
    private Spinner spinnerCategory;
    private CategoryAdapter categoryAdapter;
    private TextView ratingScore,tv_driverName,tv_driverRating,tv_driverName1,tv_driverRating1;
    private ImageView img_driverAvatar,img_driverAvatar1;
    private RatingBar ratingBar;
    private MaterialButton book, cancelWaiting, cancelComing, confirm_rating;
    private TextView motorPrice, carPrice;
    private CheckBox motorCheckBox, carCheckBox;
    private String paymentMethod;
    private PaymentButtonContainer paymentButtonContainer;
    private static final int BASE_PRICE_A2 = 20000; // Base price for Hạng A2
    private static final int BASE_PRICE_B2 = 30000; // Base price for Hạng B2
    private static final int PRICE_PER_KM_A2 = 11000; // Price per km for Hạng A2
    private static final int NIGHT_FEE_21_23 = 6000; // Night fee from 21h to 23h
    private static final int NIGHT_FEE_23_1 = 10000; // Night fee from 23h to 1h
    private static final int NIGHT_FEE_1_4 = 15000; // Night fee from 1h to 4h
    private boolean type=false;
    private float motor_price=0;
    private float car_price=0;
    private float currencyRate = 0.04f;
    private int radius=1;
    private static  int MAX_RADIUS=3;
    private boolean driverFound=false;
    private String driverID;
    private  DriverInfos driverInfos=null;
    Order order= new Order();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_driver_user);
        // init Button
        book = findViewById(R.id.book_btn);
        cancelWaiting = findViewById(R.id.cancelWaiting_btn);
        cancelComing = findViewById(R.id.cancelComing_btn);
        confirm_rating = findViewById(R.id.confirm_rating_btn);
        motorPrice = findViewById(R.id.motor_price);
        carPrice = findViewById(R.id.car_price);
        motorCheckBox = findViewById(R.id.motor_checkbox);
        carCheckBox = findViewById(R.id.car_checkbox);
        // Init rating bar
        ratingBar = findViewById(R.id.ratingBar);
        ratingScore = findViewById(R.id.tv_ratingScore);
        //Paypal
        paymentButtonContainer= findViewById(R.id.payment_button_container);
        // Init Category payment method
        spinnerCategory = findViewById(R.id.spn_category);
        categoryAdapter = new CategoryAdapter(this, R.layout.item_selected, getListCategory());
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BookDriverActivityUser.this, categoryAdapter.getItem(i).getName(), Toast.LENGTH_LONG);
                paymentMethod=categoryAdapter.getItem(i).getName().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //init Bottom Sheet
        layoutBottomSheetBook = findViewById(R.id.bottom_sheet_book_driver);
        layoutBottomSheetWating = findViewById(R.id.bottom_sheet_waiting);
        layoutBottomSheetDriverComing = findViewById(R.id.bottom_sheet_driver_coming);
        layoutBottomSheetDriverArrived = findViewById(R.id.bottom_sheet_driver_arrived);
        layoutBottomSheetDriverEndTrip = findViewById(R.id.bottom_sheet_driver_end_trip);
        layoutBottomSheetPaypal = findViewById(R.id.bottom_sheet_paypal);

        tv_driverName = layoutBottomSheetDriverComing.findViewById(R.id.tv_driverName);
        tv_driverRating = layoutBottomSheetDriverComing.findViewById(R.id.tv_driverRating);
        img_driverAvatar= layoutBottomSheetDriverComing.findViewById(R.id.img_driverAvatar);

        tv_driverName1 = layoutBottomSheetDriverArrived.findViewById(R.id.tv_driverName);
        tv_driverRating1 = layoutBottomSheetDriverArrived.findViewById(R.id.tv_driverRating_arrived);
        img_driverAvatar1 = layoutBottomSheetDriverArrived.findViewById(R.id.img_driverAvatar);

        bottomSheetBehaviorBook = BottomSheetBehavior.from(layoutBottomSheetBook);
        bottomSheetBehaviorWaiting = BottomSheetBehavior.from(layoutBottomSheetWating);
        bottomSheetBehaviorDriverComing = BottomSheetBehavior.from(layoutBottomSheetDriverComing);
        bottomSheetBehaviorDriverArrived = BottomSheetBehavior.from(layoutBottomSheetDriverArrived);
        bottomSheetBehaviorDriverEndTrip = BottomSheetBehavior.from(layoutBottomSheetDriverEndTrip);
        bottomSheetBehaviorPaypal = BottomSheetBehavior.from(layoutBottomSheetPaypal);
        // xu li goi bottom sheet
        bottomSheetBehaviorBook.setState(BottomSheetBehavior.STATE_EXPANDED);
        setCheckBoxAuto();
        SetMotorPrice();
        SetCarPrice();
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehaviorBook.setState(BottomSheetBehavior.STATE_COLLAPSED);
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                String phoneNumber = preferences.getString("phone_number","" );
                String userName = preferences.getString("user_name", "");
                String user_location_latitude=preferences.getString("user_location_latitude","");
                String user_location_longtitude=preferences.getString("user_location_longtitude","");
                String user_destination_latitude=preferences.getString("user_destination_latitude","");
                String user_destination_longtitude=preferences.getString("user_destination_longitude","");


                order.setClientNo(phoneNumber);
                order.setClientName(userName);
                order.setPickupLocation_Latitude(user_location_latitude);
                order.setPickupLocation_Longtitude(user_location_longtitude);
                order.setDestination_Latitude(user_destination_latitude);
                order.setDestination_Longtidue(user_destination_longtitude);
                if(type)
                {
                    order.setVehicleType(MyEnum.VehicleType.CAR);
                    order.setPrice(car_price);
                }
                else
                {
                    order.setVehicleType(MyEnum.VehicleType.MOTORBIKE);
                    order.setPrice(motor_price);
                }
                order.setOrderStatus(MyEnum.OrderStatus.PENDING);
                order.setDriverInfos(null);
                if (paymentMethod=="Cash"){
                    order.setPaymentMethod(MyEnum.PaymentMethod.COD);
                }
                if (paymentMethod =="Paypal"){
                    order.setPaymentMethod(MyEnum.PaymentMethod.PAYPAL);
                }
                checkPaymentMethod(order);
            }
        });
    }
    private void checkPaymentMethod(Order order){
        if (order.getPaymentMethod()==MyEnum.PaymentMethod.PAYPAL){
            bottomSheetBehaviorPaypal.setState(BottomSheetBehavior.STATE_EXPANDED);
            Float priceVND=  order.getPrice();
            priceVND = (float)Math.round(priceVND/1000);
            if(priceVND!=null) {
                Float price=convertVndToUsd(priceVND);
                setupPayPal(price, order);
            }
        }
        if (order.getPaymentMethod()==MyEnum.PaymentMethod.COD){
            finishOrder(order);
        }
    }
    private void finishOrder(Order order){
        getClosetDriver();
        bottomSheetBehaviorWaiting.setState(BottomSheetBehavior.STATE_EXPANDED);
        checkDriver(order);
    }
    private void checkDriver(Order order) {
        DatabaseReference OderRef = FirebaseDatabase.getInstance().getReference().child("Order").child(order.getId());
        OderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order orderNew = dataSnapshot.getValue(Order.class);
                    if (orderNew.getOrderStatus() == MyEnum.OrderStatus.ACCEPT) {
                        DriverInfos driverInfo = order.getDriverInfos();
                        tv_driverName.setText(driverInfo.getName());
                        tv_driverRating.setText((int) driverInfo.getAvgRating());
                        tv_driverName1.setText(driverInfo.getName());
                        tv_driverRating1.setText((int) driverInfo.getAvgRating());
                        String imageDriver = driverInfo.getPicture();
                        if (imageDriver != null && !imageDriver.isEmpty()) {
                            Picasso.get().load(imageDriver).into(img_driverAvatar);
                            Picasso.get().load(imageDriver).into(img_driverAvatar1);
                        }
                        bottomSheetBehaviorWaiting.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehaviorDriverComing.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    else if (orderNew.getOrderStatus() == MyEnum.OrderStatus.PICKEDUP) {
                        bottomSheetBehaviorDriverComing.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehaviorDriverArrived.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    else if (orderNew.getOrderStatus() == MyEnum.OrderStatus.SUCCEED) {
                        bottomSheetBehaviorDriverArrived.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehaviorDriverEndTrip.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Profile Driver Fragment", "Error database");
            }
        });
    }




        private float convertVndToUsd(float priceVnd) {

        return priceVnd * currencyRate;
    }
    private  void setupPayPal(Float price, Order order){
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NonNull CreateOrderActions createOrderActions) {
                        Log.d("BookDriverActivity", "create: ");
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(Float.toString(price))
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NonNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.d("BookDriverActivity", String.format("CaptureOrderResult: %s", result));
                                bottomSheetBehaviorPaypal.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                finishOrder(order);
                            }
                        });
                    }
                }
        );
    }
    private void uploadOrderToFirebase(Order order) {
        // Get a reference to the root of your Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // Get a reference to the "orders" node (adjust the path as needed)
        DatabaseReference ordersReference = databaseReference.child("Order");
        // Push the Order object to the database to generate a unique ID
        DatabaseReference newOrderRef = ordersReference.push();
        // Set the unique ID as the order's ID
        order.setId(newOrderRef.getKey());
        // Set the value of the Order object at the generated ID
        newOrderRef.setValue(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Order Created!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateRatingScore(int rating) {
        String ratingText;
        switch (rating) {
            case 1:
                ratingText = "Poor";
                break;
            case 2:
                ratingText = "Fair";
                break;
            case 3:
                ratingText = "Good";
                break;
            case 4:
                ratingText = "Very Good";
                break;
            case 5:
                ratingText = "Excellent";
                break;
            default:
                ratingText = ""; // Handle other cases if needed
                break;
        }
        // Update the TextView with the calculated rating text
        ratingScore.setText(ratingText);
    }
    private void setCheckBoxAuto() {
        motorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the motor checkbox is checked, uncheck the car checkbox
                    type=true;
                    carCheckBox.setChecked(false);
                }
            }
        });

        carCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type=false;
                    // If the car checkbox is checked, uncheck the motor checkbox
                    motorCheckBox.setChecked(false);
                }
            }
        });
    }
    private void SetMotorPrice()
    {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        float distance = Float.parseFloat(preferences.getString("distance", ""));

        float distancePrice =  (distance * PRICE_PER_KM_A2);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Calculate night fee based on the hour
        float nightFee = 0;
        if (currentHour >= 21 && currentHour < 23) {
            nightFee = NIGHT_FEE_21_23;
        } else if (currentHour >= 23 && currentHour < 1) {
            nightFee = NIGHT_FEE_23_1;
        } else if (currentHour >= 1 && currentHour < 4) {
            nightFee = NIGHT_FEE_1_4;
        }
        // Calculate the total price
        float totalPrice = BASE_PRICE_A2 + distancePrice + nightFee;
        motor_price=roundToNearest500(totalPrice);
        motorPrice.setText(motor_price+ " VND");
    }
    private void SetCarPrice()
    {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        float distance = Float.parseFloat(preferences.getString("distance", ""));

        float distancePrice =  (distance * BASE_PRICE_B2);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Calculate night fee based on the hour
        float nightFee = 0;
        if (currentHour >= 21 && currentHour < 23) {
            nightFee = NIGHT_FEE_21_23;
        } else if (currentHour >= 23 && currentHour < 1) {
            nightFee = NIGHT_FEE_23_1;
        } else if (currentHour >= 1 && currentHour < 4) {
            nightFee = NIGHT_FEE_1_4;
        }
        // Calculate the total price
        float totalPrice = BASE_PRICE_B2 + distancePrice + nightFee;
        car_price=roundToNearest500(totalPrice);
        carPrice.setText(car_price+ " VND");
    }
    private float roundToNearest500(float value) {
        // Kiểm tra xem giá trị là trên hay dưới 500
        float remainder = value % 500;
        if (remainder > 0 && remainder < 500) {
            // Làm tròn lên 500 nếu giá trị dưới 500 đồng
            return (float) Math.ceil(value / 500) * 500;
        } else {
            // Làm tròn lên hàng nghìn nếu giá trị trên 500 đồng
            return (float) Math.ceil(value / 1000) * 1000;
        }
    }
    private List<CategoryPaymentMethod> getListCategory () {
            List<CategoryPaymentMethod> list = new ArrayList<>();
            list.add(new CategoryPaymentMethod("Cash", "cash"));
            list.add(new CategoryPaymentMethod("ZaloPay", "zalopay"));
            list.add(new CategoryPaymentMethod("Paypal", "paypal"));
            return list;
        }
    private void getClosetDriver()
    {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String user_location_latitude=preferences.getString("user_location_latitude","");
        String user_location_longtitude=preferences.getString("user_location_longtitude","");
        DatabaseReference driverLocation=FirebaseDatabase.getInstance().getReference().child("DriverLocation");
        GeoFire geoFire= new GeoFire(driverLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(Double.parseDouble(user_location_latitude), Double.parseDouble(user_location_longtitude)), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!driverFound)
                {
                    driverFound=true;
                    driverID=key;
                    fetchDriverInfo(driverID);
                }
            }
            @Override
            public void onKeyExited(String key) {
            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }
            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    // Increase the radius, but set a maximum limit to avoid infinite recursion
                    if (radius <= MAX_RADIUS) {
                        radius++;
                        getClosetDriver();
                    } else {
                        Log.d("No Driver Found", "Maximum radius reached");
                        // Handle the case when no driver is found within the maximum radius
                    }
                }
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }
    private void fetchDriverInfo(String driverID) {
        DatabaseReference driverInfoRef = FirebaseDatabase.getInstance().getReference().child("DriversInfo").child(driverID);
        driverInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Create a new DriverInfos object using the data from the dataSnapshot
                    driverInfos = dataSnapshot.getValue(DriverInfos.class);
                    if(driverInfos.getDriverStatus().equals(MyEnum.DriverStatus.ACTIVE))
                    {
                        Log.d("Driver Info", driverInfos.getPhoneNo());
                        Log.d("Driver Info", driverInfos.getName());
                        order.setDriverInfos(driverInfos);
                        Log.d("Order setDriver",order.getDriverInfos().getPhoneNo());
                        Log.d("Order setDriver",order.getDriverInfos().getName());
                        // Get the current timestamp
                        long currentTimeMillis = System.currentTimeMillis();

                        // Create a Date object using the timestamp
                        Date currentDate = new Date(currentTimeMillis);
                        order.setDatetime(currentDate);
                        uploadOrderToFirebase(order);
                    }
                    else
                    {
                        driverFound = false;
                        getClosetDriver();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors that occur during the data retrieval process
            }
        });
    }
}

