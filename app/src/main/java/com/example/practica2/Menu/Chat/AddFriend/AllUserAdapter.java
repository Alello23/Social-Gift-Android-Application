package com.example.practica2.Menu.Chat.AddFriend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.User;

import java.util.List;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserHolder> {
    private List<User> users;
    private Activity activity;
    private RequestQueue requestQueue;
    public AllUserAdapter(List<User> users, Activity activity, RequestQueue requestQueue) {
        this.users = users;
        this.activity = activity;
        this.requestQueue = requestQueue;
    }
    @Override
    public AllUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new AllUserHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(AllUserHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, requestQueue);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

}
