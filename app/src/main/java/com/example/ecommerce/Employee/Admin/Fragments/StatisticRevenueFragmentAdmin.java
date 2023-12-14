package com.example.ecommerce.Employee.Admin.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StatisticRevenueFragmentAdmin extends Fragment {
    View rootView;
    BarChart barChart;
    MaterialButton upload_btn;
    DatabaseReference revenueRef;
    StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_statistic_revenue_admin, container, false);
        barChart = rootView.findViewById(R.id.barChart);
        upload_btn = rootView.findViewById(R.id.upload_firebase_btn);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        revenueRef = FirebaseDatabase.getInstance().getReference("Bill");
        fetchPromotionData();
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebaseStorage();
            }
        });

        return rootView;
    }

    private void fetchPromotionData() {
        revenueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BarEntry> entries = new ArrayList<>();
                Float amount = 0f;
                // Assuming your Firebase structure has a child "amount" representing the promotion amount
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    float promotionAmount = snapshot.child("promotion").getValue(Float.class);
                    amount += promotionAmount; // Accumulate the amount for each promotion
                }

                entries.add(new BarEntry(entries.size(), amount));

                // Create a BarDataSet with your entries
                BarDataSet dataSet = new BarDataSet(entries, "Monthly Promotion Revenue");

                // Customize the appearance of the chart
                dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

                // Create a BarData object and set it to the chart
                BarData barData = new BarData(dataSet);
                barChart.setData(barData);

                // Customize the X-axis
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setDrawGridLines(false);

                // Customize the Y-axis
                YAxis yAxisRight = barChart.getAxisRight();
                yAxisRight.setEnabled(false);

                // Update the chart
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }



    private void uploadToFirebaseStorage() {
        String imagePath = "DriverImage/chart_image.png";
        StorageReference imageRef = storageRef.child(imagePath);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap chartBitmap = getBitmapFromView(barChart);
        chartBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageData = stream.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                Toast.makeText(rootView.getContext(), "Upload successfully.", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            // Handle unsuccessful upload
            Toast.makeText(rootView.getContext(), "Uploading failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        });
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}