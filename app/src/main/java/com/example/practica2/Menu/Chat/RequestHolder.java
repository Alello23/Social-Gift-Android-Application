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
import com.example.practica2.Menu.Chat.AddFriend.AllUserAdapter;
import com.example.practica2.Menu.Chat.AddFriend.NewFriendActivity;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHolder extends RecyclerView.ViewHolder {
    private ImageView avatarImage;
    private TextView userName;
    private ImageView accept;
    private ImageView reject;
    private Activity activity;
    private User user;
    public RequestHolder(LayoutInflater inflater, ViewGroup parent, Activity activity) {
        super(inflater.inflate(R.layout.element_friend_request, parent, false));
        avatarImage = itemView.findViewById(R.id.FR_avatarImageView);
        userName = itemView.findViewById(R.id.FR_senderTextView);
        accept = itemView.findViewById(R.id.FR_accept_button);
        reject = itemView.findViewById(R.id.FR_refuse_button);

        this.activity = activity;
    }
    public void bind(User user, RequestQueue requestQueue) {
        this.user = user;
        userName.setText(user.getName());
        try {
            Picasso.get().load(user.getImage()).into(avatarImage);
        }catch (Exception e){
            Log.e("error", "Usuario sin imagen: " + user.getName());
            avatarImage.setImageResource(R.drawable.default_avatar);
        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(requestQueue, user.getId());
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(requestQueue, user.getId());
            }
        });
    }
    public void acceptRequest( RequestQueue requestQueue, int id) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(activity, R.string.Accepted, Toast.LENGTH_SHORT).show();
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
                            }else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(activity, R.string.Error_406, Toast.LENGTH_SHORT).show();
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
    public void rejectRequest( RequestQueue requestQueue, int id) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(activity, R.string.Rejected, Toast.LENGTH_SHORT).show();
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
                            }else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(activity, R.string.Error_406, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 500) {
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
