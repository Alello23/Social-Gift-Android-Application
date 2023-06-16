package com.example.practica2.Menu.Chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.User;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestHolder> {
    private List<User> users;
    private Activity activity;
    private RequestQueue requestQueue;
    public RequestAdapter(List<User> users, Activity activity, RequestQueue requestQueue) {
        this.users = users;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }
    @Override
    public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new RequestHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(RequestHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, requestQueue);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

}
