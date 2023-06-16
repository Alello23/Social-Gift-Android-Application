package com.example.practica2.Menu.Chat.AddFriend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Login_Register.MainActivity;
import com.example.practica2.Login_Register.SignUpActivity;
import com.example.practica2.Menu.Menu;
import com.example.practica2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFriendActivity extends AppCompatActivity {
    ImageView backButton;
    SearchView searchView;
    RecyclerView list;
    RequestQueue requestQueue;
    AllUserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        saveToSharedPrefs(getIntent().getStringExtra("token"));

        requestQueue = Volley.newRequestQueue(NewFriendActivity.this);

        searchView = findViewById(R.id.AF_searchView);
        backButton = findViewById(R.id.AF_back_button);
        list = findViewById(R.id.AF_insertUsers);
        list.setLayoutManager(new LinearLayoutManager(NewFriendActivity.this));



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               NewFriendActivity.super.onBackPressed();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Acciones a realizar cuando se envía la búsqueda
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }
        });

        updateUI();
    }
    private void updateUI() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<User> userList = new ArrayList<>();
                            // Iterar sobre los elementos del arreglo JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                // Obtener los valores de las propiedades del usuario
                                int id = userObject.getInt("id");
                                String name = userObject.getString("name");
                                String lastName = userObject.getString("last_name");
                                String email = userObject.getString("email");
                                String image = userObject.getString("image");

                                userList.add(new User(id,name,lastName,email,image));
                            }
                            adapter = new AllUserAdapter(userList, NewFriendActivity.this, requestQueue);
                            list.setAdapter(adapter);
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
    private void performSearch(String query){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/search/?s=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<User> userList = new ArrayList<>();
                            // Iterar sobre los elementos del arreglo JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                // Obtener los valores de las propiedades del usuario
                                int id = userObject.getInt("id");
                                String name = userObject.getString("name");
                                String lastName = userObject.getString("last_name");
                                String email = userObject.getString("email");
                                String image = userObject.getString("image");

                                userList.add(new User(id,name,lastName,email,image));
                            }
                            adapter = new AllUserAdapter(userList, NewFriendActivity.this, requestQueue);
                            list.setAdapter(adapter);
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
        requestQueue.add(jsonArrayRequest);
    }


}
