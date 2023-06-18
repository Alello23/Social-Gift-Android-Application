package com.example.practica2.Menu.WishList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.R;

public class GiftViewHolder extends RecyclerView.ViewHolder {
    private TextView giftNameTextView;
    private TextView giftPriorityTextView;

    public GiftViewHolder(@NonNull View itemView) {
        super(itemView);
        giftNameTextView = itemView.findViewById(R.id.gift_name);
        giftPriorityTextView = itemView.findViewById(R.id.gift_priority);
    }

    public void bind(Gift gift) {
        giftNameTextView.setText(gift.getProductUrl());
        giftPriorityTextView.setText(String.valueOf(gift.getPriority()));
    }
}
