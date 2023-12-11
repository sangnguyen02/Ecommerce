package com.example.ecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Employee.Driver.Activities.HistoryDriverDetailActivity;
import com.example.ecommerce.Employee.Driver.ReverseGeocodingTask;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.HistoryUserDetailActivity;

import java.util.List;

public class HistoryDriverAdapter extends RecyclerView.Adapter<HistoryDriverAdapter.HistoryDriverViewHolder> {

    private static List<Order> orderList;
    private Context context;
    ReverseGeocodingTask reverseGeocodingTask_Destination;



    public HistoryDriverAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryDriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_driver, parent, false);
        return new HistoryDriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDriverViewHolder holder, int position) {
        Order order = orderList.get(position);
        Double destLong = Double.parseDouble(order.getDestination_Longtidue());
        Double destLa = Double.parseDouble(order.getDestination_Latitude());
        reverseGeocodingTask_Destination = new ReverseGeocodingTask(holder.tv_destination);
        holder.tv_destination.setText(reverseGeocodingTask_Destination.execute(destLa, destLong).toString());
        holder.tv_price.setText(String.valueOf(order.getPrice()) + "VND");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class HistoryDriverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_destination, tv_price, tv_datetime;
        ReverseGeocodingTask reverseGeocodingTask_Destination;

        public HistoryDriverViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_destination = itemView.findViewById(R.id.tv_destination_history_driver);
            tv_price = itemView.findViewById(R.id.tv_price_history_driver);
            reverseGeocodingTask_Destination = new ReverseGeocodingTask(tv_destination);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Order clickedOrder = orderList.get(position);

                        String orderID = clickedOrder.getId();

                        Intent intent = new Intent(itemView.getContext(), HistoryDriverDetailActivity.class);

                        intent.putExtra("ORDER_ID_DRIVER", orderID);

                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            //listener.onClick(view, getAdapterPosition(),false);
        }
    }
}
