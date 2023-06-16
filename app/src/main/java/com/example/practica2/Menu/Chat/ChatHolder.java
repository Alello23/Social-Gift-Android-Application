package com.example.practica2.Menu.Chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.R;

public class ChatHolder extends RecyclerView.ViewHolder {
    private ImageView avatarIcon;
    private TextView name;
    private TextView last_message;
    private TextView time_message;
    private Activity activity;
    public ChatHolder(LayoutInflater inflater, ViewGroup parent, Activity activity) {
        super(inflater.inflate(R.layout.element_friend_chat, parent, false));
        this.activity = activity;
    }
}
