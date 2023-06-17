package com.example.practica2.Menu.Home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.Chats.RequestHolder;
import com.example.practica2.Menu.WishList.WishListItemViewHolder;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListItemViewHolder> {
    private List<WishList> wishLists;
    private Activity activity;
    private RequestQueue requestQueue;
    public WishListAdapter(List<WishList> wishLists, Activity activity, RequestQueue requestQueue) {
        this.wishLists = wishLists;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @Override
    public WishListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new WishListItemViewHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(WishListItemViewHolder holder, int position) {
        WishList wishList = wishLists.get(position);
        holder.bind(wishList, requestQueue);
    }
    @Override
    public int getItemCount() {
        return wishLists.size();
    }


}

