package com.example.practica2.Menu.Home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.Product;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.RequestHolder;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
    private List<Product> products;
    private Activity activity;
    private RequestQueue requestQueue;
    public ProductAdapter(List<Product> products, Activity activity, RequestQueue requestQueue) {
        this.products = products;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new ProductHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, requestQueue);
    }
    @Override
    public int getItemCount() {
        return products.size();
    }


}

