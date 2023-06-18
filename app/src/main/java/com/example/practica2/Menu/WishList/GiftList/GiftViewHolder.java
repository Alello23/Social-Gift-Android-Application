package com.example.practica2.Menu.WishList.GiftList;

import android.app.Activity;
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
    private TextView giftDescription;
    private Activity activity;
    public GiftViewHolder(LayoutInflater inflater, ViewGroup parent, Activity activity){
        super(inflater.inflate(R.layout.element_gift, parent, false));
        giftNameTextView = itemView.findViewById(R.id.GI_title_WishList);
        giftDescription = itemView.findViewById(R.id.GI_description);

        this.activity = activity;
    }

    public void bind(Gift gift) {
        giftNameTextView.setText(gift.getProductUrl());
        giftDescription.setText(String.valueOf(gift.getPriority()));
    }
}
