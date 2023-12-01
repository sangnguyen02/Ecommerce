package com.example.ecommerce.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Employee.Admin.Activities.ManageDriverDetailActivityAdmin;
import com.example.ecommerce.Employee.Admin.Fragments.ManageDriverFragmentAdmin;
import com.example.ecommerce.Enum.RequestCode;
import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private static List<DriverInfos> driverList;
    private Context context;

    public DriverAdapter(List<DriverInfos> driverList, Context context) {
        this.driverList = driverList;
        this.context = context;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_driver, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        DriverInfos driver = driverList.get(position);
        holder.textDriverName.setText(driver.getName());
        holder.textDriverStatus.setText(driver.getDriverStatus().toString());
        Picasso.get().load(driver.getPicture()).into(holder.driverImage);
        holder.textRating.setText(String.valueOf(driver.getAvgRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DriverInfos clickedDriver = driverList.get(position);
                    // Start the new activity with the clicked driver
                    Intent intent = new Intent(context, ManageDriverDetailActivityAdmin.class);
                    intent.putExtra("clickedDriver", clickedDriver);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView textDriverName, textRating, textDriverStatus;
        CircleImageView driverImage;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            textDriverName = itemView.findViewById(R.id.tv_fullname);
            textRating = itemView.findViewById(R.id.tv_rating);
            textDriverStatus = itemView.findViewById(R.id.tv_status);
            driverImage = itemView.findViewById(R.id.img_driver);

        }
    }
}
