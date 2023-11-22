package com.example.ecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommerce.Models.CategoryPaymentMethod;
import com.example.ecommerce.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryPaymentMethod> {
    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<CategoryPaymentMethod> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        ImageView imgCategory = convertView.findViewById(R.id.img_category);

        CategoryPaymentMethod category = this.getItem(position);
        if (category != null) {
            imgCategory.setImageResource(
                    parent.getContext().getResources().getIdentifier(
                            category.getImageName(),
                            "drawable",
                            parent.getContext().getPackageName()
                    )
            );
        }
        return convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        TextView tvCategory = convertView.findViewById(R.id.tv_category);
        ImageView imgCategory = convertView.findViewById(R.id.img_category);

        CategoryPaymentMethod category = this.getItem(position);
        if (category != null) {
            tvCategory.setText(category.getName());
            imgCategory.setImageResource(
                    parent.getContext().getResources().getIdentifier(
                            category.getImageName(),
                            "drawable",
                            parent.getContext().getPackageName()
                    )
            );
        }
        return convertView;
    }
}
