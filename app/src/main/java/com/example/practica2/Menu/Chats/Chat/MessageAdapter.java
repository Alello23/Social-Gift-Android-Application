package com.example.practica2.Menu.Chats.Chat;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.Message_user;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.AddFriend.AllUserHolder;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
    private List<Message_user> messages;
    private Activity activity;
    private RequestQueue requestQueue;
    private int own;
    public MessageAdapter(List<Message_user> messages, Activity activity, int own) {
        this.messages = messages;
        this.activity = activity;
        this.own = own;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new MessageHolder(layoutInflater, parent, activity);
    }
    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message_user message = messages.get(position);
        holder.bind(message);
        if (message.getId() == own) {
            holder.setGravity(Gravity.END);
        } else {
            holder.setGravity(Gravity.START);
        }
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

}

