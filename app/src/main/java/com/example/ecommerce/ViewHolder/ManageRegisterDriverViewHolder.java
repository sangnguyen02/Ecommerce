package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class ManageRegisterDriverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imgDriver;
    public TextView tvName, tvStatus;
    public ItemClickListener listener;

    public ManageRegisterDriverViewHolder(@NonNull View itemView) {
        super(itemView);
        imgDriver = itemView.findViewById(R.id.img_driver);
        tvName = itemView.findViewById(R.id.tv_fullname);
        tvStatus = itemView.findViewById(R.id.tv_status);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(),false);
    }
}
