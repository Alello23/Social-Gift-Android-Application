package com.example.practica2.Menu.WishList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.R;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {
    private List<Gift> giftList;

    public GiftAdapter(List<Gift> giftList) {
        this.giftList = giftList;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_gift_item, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        Gift gift = giftList.get(position);
        holder.bind(gift);
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    public static class GiftViewHolder extends RecyclerView.ViewHolder {
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
}
