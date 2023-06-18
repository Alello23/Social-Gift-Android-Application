package com.example.practica2.Menu.WishList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.WishList;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListHolder> {
    private List<WishList> wishLists;
    private Activity activity;
    private RequestQueue requestQueue;
    private WishListsFragment wishListFragment;
    public WishListAdapter(List<WishList> wishLists, Activity activity, RequestQueue requestQueue, WishListsFragment wishListFragment) {
        this.wishLists = wishLists;
        this.activity = activity;
        this.requestQueue = requestQueue;
        this.wishListFragment = wishListFragment;
    }
    @Override
    public WishListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new WishListHolder(layoutInflater, parent, activity, wishListFragment);
    }
    @Override
    public void onBindViewHolder(WishListHolder holder, int position) {
        WishList wishList = wishLists.get(position);
        holder.bind(wishList, requestQueue, this,wishLists);
    }
    @Override
    public int getItemCount() {
        return wishLists.size();
    }


}

