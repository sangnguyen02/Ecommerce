package com.example.ecommerce.User.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Adapters.CategoryAdapter;
import com.example.ecommerce.Models.CategoryPaymentMethod;
import com.example.ecommerce.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class BookDriverActivityUser extends AppCompatActivity {

    private CardView layoutBottomSheetBook, layoutBottomSheetWating, layoutBottomSheetDriverComing, layoutBottomSheetDriverArrived, layoutBottomSheetDriverEndTrip;

    private BottomSheetBehavior bottomSheetBehaviorBook, bottomSheetBehaviorWaiting, bottomSheetBehaviorDriverComing, bottomSheetBehaviorDriverArrived, bottomSheetBehaviorDriverEndTrip;

    private Spinner spinnerCategory;
    private CategoryAdapter categoryAdapter;
    private TextView ratingScore;
    private RatingBar ratingBar;
    private MaterialButton book, cancelWaiting, cancelComing, confirm_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_driver_user);


        // init Button
        book = findViewById(R.id.book_btn);
        cancelWaiting = findViewById(R.id.cancelWaiting_btn);
        cancelComing = findViewById(R.id.cancelComing_btn);
        confirm_rating = findViewById(R.id.confirm_rating_btn);


        // Init rating bar
        ratingBar = findViewById(R.id.ratingBar);
        ratingScore = findViewById(R.id.tv_ratingScore);

        // Init Category payment method
        spinnerCategory = findViewById(R.id.spn_category);
        categoryAdapter = new CategoryAdapter(this,R.layout.item_selected,getListCategory());
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BookDriverActivityUser.this,categoryAdapter.getItem(i).getName(),Toast.LENGTH_LONG);
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

        bottomSheetBehaviorBook = BottomSheetBehavior.from(layoutBottomSheetBook);
        bottomSheetBehaviorWaiting = BottomSheetBehavior.from(layoutBottomSheetWating);
        bottomSheetBehaviorDriverComing = BottomSheetBehavior.from(layoutBottomSheetDriverComing);
        bottomSheetBehaviorDriverArrived = BottomSheetBehavior.from(layoutBottomSheetDriverArrived);
        bottomSheetBehaviorDriverEndTrip = BottomSheetBehavior.from(layoutBottomSheetDriverEndTrip);

        // xu li goi bottom sheet


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



    private List<CategoryPaymentMethod> getListCategory() {
        List<CategoryPaymentMethod> list = new ArrayList<>();
        list.add(new CategoryPaymentMethod("Cash", "cash"));
        list.add(new CategoryPaymentMethod("ZaloPay", "zalopay"));
        list.add(new CategoryPaymentMethod("Paypal", "paypal"));
        return list;
    }
}