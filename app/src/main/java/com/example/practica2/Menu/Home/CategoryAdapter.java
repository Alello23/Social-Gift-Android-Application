package com.example.practica2.Menu.Home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.RequestHolder;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private List<Category> categories;
    private Activity activity;
    private RequestQueue requestQueue;
    public CategoryAdapter(List<Category> categories, Activity activity, RequestQueue requestQueue) {
        this.categories = categories;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new CategoryHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, requestQueue);
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }


}

