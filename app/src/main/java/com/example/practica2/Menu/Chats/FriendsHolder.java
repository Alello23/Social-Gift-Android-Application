package com.example.practica2.Menu.Chats;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.Message_user;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Login_Register.MainActivity;
import com.example.practica2.Login_Register.SignUpActivity;
import com.example.practica2.Menu.Chats.Chat.ChatActivity;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FriendsHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
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
        itemView.setOnClickListener(this);

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
        getLastMessege(activity, requestQueue, user.getId());

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                checkMessage(activity,requestQueue,user.getId());
            }
        };

        // Programa la tarea para que se ejecute cada 10 segundos
        timer.schedule(task, 0, 10000);
    }
    public void checkMessage(Activity activity, RequestQueue requestQueue, int id) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Message_user> messageList = new ArrayList<>();
                            // Iterar sobre los elementos del arreglo JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                // Obtener los valores de las propiedades del usuario
                                int id = userObject.getInt("id");
                                String content = userObject.getString("content");
                                int userIdSend = userObject.getInt("user_id_send");
                                int userIdReceived = userObject.getInt("user_id_recived");
                                String timeStampString = userObject.getString("timeStamp");

                                messageList.add(new Message_user(id, content, userIdSend, userIdReceived, timeStampString));
                            }
                            if (!lastMessage.getText().equals(messageList.get(messageList.size() - 1).getContent())){
                                if(!messageList.isEmpty()){
                                    lastMessage.setText(messageList.get(messageList.size() - 1).getContent());
                                    lastMessage_time.setText(messageList.get(messageList.size() - 1).getTimeStamp());
                                }else {
                                    lastMessage.setText(R.string.empty_chat);
                                    lastMessage_time.setText("");
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }

        };

        requestQueue.add(jsonArrayRequest);
    }
    public void getLastMessege(Activity activity, RequestQueue requestQueue, int id) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Message_user> messageList = new ArrayList<>();
                            // Iterar sobre los elementos del arreglo JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                // Obtener los valores de las propiedades del usuario
                                int id = userObject.getInt("id");
                                String content = userObject.getString("content");
                                int userIdSend = userObject.getInt("user_id_send");
                                int userIdReceived = userObject.getInt("user_id_recived");
                                String timeStampString = userObject.getString("timeStamp");

                                messageList.add(new Message_user(id, content, userIdSend, userIdReceived, timeStampString));
                            }
                            if(!messageList.isEmpty()){
                                lastMessage.setText(messageList.get(messageList.size() - 1).getContent());
                                lastMessage_time.setText(messageList.get(messageList.size() - 1).getTimeStamp());
                            }else {
                                lastMessage.setText(R.string.empty_chat);
                                lastMessage_time.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }

        };

        requestQueue.add(jsonArrayRequest);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("User_ID", user.getId());
        intent.putExtra("User_own_id", Integer.parseInt(getUserID()));
        intent.putExtra("token", getFromSharedPrefs());
        activity.startActivity(intent);
    }
    private String getFromSharedPrefs() {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
    public String getUserID() {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("user_id", "default");
        return valor;
    }
}
