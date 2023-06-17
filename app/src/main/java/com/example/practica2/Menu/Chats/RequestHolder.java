package com.example.practica2.Menu.Chats;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RequestHolder extends RecyclerView.ViewHolder {
    private ImageView avatarImage;
    private TextView userName;
    private ImageView accept;
    private ImageView reject;
    private Activity activity;
    private RequestAdapter adapter;
    private User user;
    public RequestHolder(LayoutInflater inflater, ViewGroup parent, Activity activity, RequestAdapter adapter) {
        super(inflater.inflate(R.layout.element_friend_request, parent, false));
        avatarImage = itemView.findViewById(R.id.FR_avatarImageView);
        userName = itemView.findViewById(R.id.FR_senderTextView);
        accept = itemView.findViewById(R.id.FR_accept_button);
        reject = itemView.findViewById(R.id.FR_refuse_button);
        this.adapter = adapter;

        this.activity = activity;
    }
    public void bind(User user, RequestQueue requestQueue) {
        this.user = user;
        userName.setText(user.getName());
        try {
            Picasso.get().load(user.getImage()).transform(new CircleImage()).into(avatarImage);
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
    private void removeItem() {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            adapter.removeItem(position); // Llama al método removeItem() del adaptador
        }
    }
    public void acceptRequest(RequestQueue requestQueue, int id) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/" + id;

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, R.string.Accepted, Toast.LENGTH_SHORT).show();
                        removeItem();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "UTF-8");
                            Log.e("error",errorResponse);
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

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

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, R.string.Rejected, Toast.LENGTH_SHORT).show();
                        removeItem();
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
