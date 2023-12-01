package com.example.ecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Models.DriverInfos;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryUserAdapter extends RecyclerView.Adapter<HistoryUserAdapter.HistoryUserViewHolder> {

    private List<Order> orderList;
    private Context context;

    public HistoryUserAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_user, parent, false);
        return new HistoryUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryUserViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tv_destination.setText(order.getDestination_Latitude());
        holder.tv_price.setText(String.valueOf(order.getPrice()));
        holder.tv_datetime.setText("N/A");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class HistoryUserViewHolder extends RecyclerView.ViewHolder {
        TextView tv_destination, tv_price, tv_datetime;

        public HistoryUserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_destination = itemView.findViewById(R.id.tv_destination_history_user);
            tv_price = itemView.findViewById(R.id.tv_price_history_user);
            tv_datetime = itemView.findViewById(R.id.tv_datetime_history_user);
        }
    }
}
