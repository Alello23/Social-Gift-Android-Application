package com.example.practica2.Menu.Chats;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsHolder> {
    private List<User> users;
    private Activity activity;
    private RequestQueue requestQueue;
    public FriendsAdapter(List<User> users, Activity activity, RequestQueue requestQueue) {
        this.users = users;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }
    @Override
    public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new FriendsHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(FriendsHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, requestQueue);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

}
