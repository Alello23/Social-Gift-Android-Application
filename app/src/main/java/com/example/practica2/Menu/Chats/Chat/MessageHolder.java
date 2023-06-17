package com.example.practica2.Menu.Chats.Chat;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.Message_user;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageHolder extends RecyclerView.ViewHolder {
    private TextView content;
    private Activity activity;
    private Message_user message;

    public MessageHolder(LayoutInflater inflater, ViewGroup parent, Activity activity) {
        super(inflater.inflate(R.layout.element_messege, parent, false));
        content = itemView.findViewById(R.id.MES_messageTextView);

        this.activity = activity;
    }
    public void bind(Message_user message) {
        this.message = message;
        content.setText(message.getContent());
    }

    public void setGravity(int gravity) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
        params.addRule(gravity);
        content.setLayoutParams(params);

    }
}

