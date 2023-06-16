package com.example.practica2.Menu.Chat;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Login_Register.MainActivity;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class FriendsHolder extends RecyclerView.ViewHolder {
    private ImageView avatarImage;
    private TextView userName;
    private TextView lastMessage;
    private TextView lastMessage_time;
    private Activity activity;
    private User user;
    public FriendsHolder(LayoutInflater inflater, ViewGroup parent, Activity activity) {
        super(inflater.inflate(R.layout.element_friend_chat, parent, false));
        avatarImage = itemView.findViewById(R.id.FC_avatarImageView);
        userName = itemView.findViewById(R.id.FC_senderTextView);
        lastMessage = itemView.findViewById(R.id.FC_messageTextView);
        lastMessage_time = itemView.findViewById(R.id.FC_timestampTextView);

        this.activity = activity;
    }
    public void bind(User user, Activity activity, RequestQueue requestQueue) {
        this.user = user;
        userName.setText(user.getName());
        try {
            Picasso.get().load(user.getImage()).into(avatarImage);
        }catch (Exception e){
            Log.e("error", "Usuario sin imagen: " + user.getName());
            avatarImage.setImageResource(R.drawable.default_avatar);
        }
        getLastMessege(activity, requestQueue, user.getId());
//        lastMessage.setText(messege[0]);
//        lastMessage_time.setText(messege[1]);
    }
    public void getLastMessege(Activity activity, RequestQueue requestQueue, int id) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(activity, R.string.Request_succes, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(activity, R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(activity, R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 410) {
                                Toast.makeText(activity, R.string.Error_410, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(activity, R.string.Error_500, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 502) {
                                Toast.makeText(activity, R.string.Error_502, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, R.string.Error_Network, Toast.LENGTH_SHORT).show();
                        }
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }

        };

        requestQueue.add(jsonArrayRequest);
    }
    private String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
}
