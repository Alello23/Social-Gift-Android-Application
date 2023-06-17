package com.example.practica2.Menu.WishList;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.RoundedCornerTransformation;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.Chats.RequestAdapter;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

public class WishListItemViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;

    private Activity activity;
    private RequestAdapter adapter;
    public WishListItemViewHolder(LayoutInflater inflater, ViewGroup parent, Activity activity) {
        super(inflater.inflate(R.layout.element_wishlist, parent, false));
        description = itemView.findViewById(R.id.WI_description);
        title = itemView.findViewById(R.id.WI_title);

        this.activity = activity;
    }
    public void bind(WishList wishList, RequestQueue requestQueue) {
        title.setText(wishList.getName());
        description.setText(wishList.getDescription());
    }
}

