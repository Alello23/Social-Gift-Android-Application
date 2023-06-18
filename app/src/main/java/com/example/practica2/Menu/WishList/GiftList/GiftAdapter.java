package com.example.practica2.Menu.WishList.GiftList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.Gift;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftViewHolder> {
    private List<Gift> giftList;
    private Activity activity;
    private RequestQueue requestQueue;

    public GiftAdapter(List<Gift> giftList, Activity activity, RequestQueue requestQueue) {
        this.giftList = giftList;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }


    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new GiftViewHolder(layoutInflater, parent, activity);
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
}
