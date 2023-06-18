package com.example.practica2.Menu.Chats.Chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.ClassObjects.Message_user;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.WishList.GiftList.GiftListActivity;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    private ImageView backButton;
    private ImageView avatar;
    private ImageView sendBT;
    private ImageView gift;
    private TextView name;
    private TextView description;
    private EditText input;
    private int id_other;
    private int ownID;
    private RecyclerView list;
    private RequestQueue requestQueue;
    private  MessageAdapter adapter;
    private List<Message_user> messageList;
    private List<WishList> wishLists;
    public ChatActivity() {
        // Constructor vacío requerido
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        saveToSharedPrefs(getIntent().getStringExtra("token"));

        requestQueue = Volley.newRequestQueue(ChatActivity.this);
        id_other = getIntent().getIntExtra("User_ID", -1);
        ownID = getIntent().getIntExtra("User_own_id", -1);

        sendBT = findViewById(R.id.CH_send_bt);

        name = findViewById(R.id.CH_nameText);
        description = findViewById(R.id.CH_subnameText);
        avatar = findViewById(R.id.CH_avatarImage);
        gift = findViewById(R.id.CH_gift_button);

        backButton = findViewById(R.id.CH_back_button);
        list = findViewById(R.id.CH_inputMessege);
        input = findViewById(R.id.CH_messageEditText);

        list.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllWishlists();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.super.onBackPressed();
            }
        });

        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se presione el botón "Enviar"
                String mensaje = input.getText().toString();
                input.setText("");
                sendMessage(mensaje);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // Oculta el teclado virtual
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
            }
        });

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateMessage_only();
            }
        };

        // Programa la tarea para que se ejecute cada 10 segundos
        timer.schedule(task, 0, 10000);

        updateUI();
        updateMessage();
    }
    public void getAllWishlists() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            wishLists = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject wishlistObject = response.getJSONObject(i);
                                int id = wishlistObject.getInt("id");
                                String name = wishlistObject.getString("name");
                                String description = wishlistObject.getString("description");
                                int userId = wishlistObject.getInt("user_id");
                                String creationDate = wishlistObject.getString("creation_date");
                                String endDate = wishlistObject.getString("end_date");

                                List<Gift> gifts = new ArrayList<>();
                                if (userId == id_other) {
                                    WishList wishList = new WishList(id, name, description, userId, gifts, creationDate, endDate);
                                    wishLists.add(wishList);
                                }
                            }
                            showWishlistDropdown();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Error getting wishlists", Toast.LENGTH_SHORT).show();
                    }

                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }};

        requestQueue.add(jsonArrayRequest);
    }
    private void showWishlistDropdown() {
        // Crear un arreglo de cadenas para almacenar los nombres de las listas de deseos
        String[] wishlistNames = new String[wishLists.size()];

        // Obtener los nombres de las listas de deseos y agregarlos al arreglo
        for (int i = 0; i < wishLists.size(); i++) {
            wishlistNames[i] = wishLists.get(i).getName();
        }

        // Mostrar el desplegable utilizando un diálogo de lista
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle("Select Wishlist");
        builder.setItems(wishlistNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción a realizar cuando se selecciona una lista de deseos
                WishList selectedWishlist = wishLists.get(which);
                Intent intent = new Intent(ChatActivity.this, GiftListActivity.class);
                intent.putExtra("WishList_ID", selectedWishlist.getId());
                intent.putExtra("token", getFromSharedPrefs());
                intent.putExtra("my", 1);
                startActivity(intent);
            }
        });
        builder.show();
    }
    private void updateUI() {

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id_other;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                                // Obtener los valores de las propiedades del usuario
                        try {
                            name.setText(response.getString("name"));
                            description.setText(response.getString("last_name"));
                            try {
                                Picasso.get().load(response.getString("image")).transform(new CircleImage()).into(avatar);
                            }catch (Exception e){
                                Log.e("error", "Usuario sin imagen: " + response.getString("name"));
                                avatar.setImageResource(R.drawable.default_avatar);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 204) {
                                Toast.makeText(getApplicationContext(), R.string.Error_204, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(getApplicationContext(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getApplicationContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(getApplicationContext(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 502) {
                                Toast.makeText(getApplicationContext(), R.string.Error_502, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
        requestQueue.add(jsonObjectRequest);

    }

    private void updateMessage() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/" + id_other;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            messageList = new ArrayList<>();
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
                            adapter = new MessageAdapter(messageList, ChatActivity.this,ownID);
                            list.setAdapter(adapter);
                            list.scrollToPosition(adapter.getItemCount() - 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getApplicationContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getApplicationContext(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
    private void updateMessage_only() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages/" + id_other;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Message_user> newMessageList = new ArrayList<>();
                            // Iterar sobre los elementos del arreglo JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                // Obtener los valores de las propiedades del usuario
                                int messageId = userObject.getInt("id");
                                String content = userObject.getString("content");
                                int userIdSend = userObject.getInt("user_id_send");
                                int userIdReceived = userObject.getInt("user_id_recived");
                                String timeStampString = userObject.getString("timeStamp");

                                newMessageList.add(new Message_user(messageId, content, userIdSend, userIdReceived, timeStampString));
                            }
                            for (Message_user message : messageList) {
                                for (Iterator<Message_user> iterator = newMessageList.iterator(); iterator.hasNext();) {
                                    Message_user newMessage = iterator.next();
                                    if (newMessage.getId() == message.getId()) {
                                        iterator.remove();
                                    }
                                }
                            }
                            // Agregar nuevos mensajes a la lista existente en el adaptador
                            messageList.addAll(newMessageList);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getApplicationContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getApplicationContext(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
    public void saveToSharedPrefs(String token) {
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putString("token", token);
        prefsEditor.apply();
    }
    public String getFromSharedPrefs() {
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }


    private void sendMessage(String query){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/messages";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("content", query);
            jsonParams.put("user_id_send", ownID);
            jsonParams.put("user_id_recived", id_other);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       updateMessage();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(getApplicationContext(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getApplicationContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(getApplicationContext(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getApplicationContext(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 502) {
                                Toast.makeText(getApplicationContext(), R.string.Error_502, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
        requestQueue.add(jsonObjectRequest);
    }


}
