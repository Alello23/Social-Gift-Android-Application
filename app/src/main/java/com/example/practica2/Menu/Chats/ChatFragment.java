package com.example.practica2.Menu.Chats;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.AddFriend.NewFriendActivity;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment{
    private final String userID;
    private ImageView add_friend;
    private ImageView avatar;
    private SearchView searchView;
    private RecyclerView list;
    private RequestQueue requestQueue;
    private TextView friendsRequest_bt;
    private TextView myChats_bt;
    private RequestAdapter requestAdapter;
    private FriendsAdapter friendsAdapter;


    public ChatFragment(RequestQueue requestQueue, String userID) {
        this.userID = userID;
        this.requestQueue = requestQueue;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_friends_chat, container, false);

        avatar = view.findViewById(R.id.FSC_avatarImage_FC);
        add_friend = view.findViewById(R.id.FSC_add_friend_button);
        searchView = view.findViewById(R.id.FSC_searchView_FC);
        friendsRequest_bt = view.findViewById(R.id.FSC_request_FC);
        myChats_bt = view.findViewById(R.id.FSC_MyChat_FC);

        list = view.findViewById(R.id.FSC_chats_container_FC);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateProfileAvatar();
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                intent.putExtra("token", getFromSharedPrefs());
                startActivity(intent);
            }
        });

        friendsRequest_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUIRequest();
            }
        });
        myChats_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUIMyFriends();
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
                // Acciones a realizar cuando el texto de búsqueda cambia
                // Puedes realizar una búsqueda en tiempo real aquí
                return false;
            }
        });

        updateUIMyFriends();

        return view;
    }
    private void performSearch(String query){

    }
    private void updateProfileAvatar() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Picasso.get().load(response.getString("image")).transform(new CircleImage()).into(avatar);
                        }catch (Exception e){
                            Log.e("error", "Usuario sin imagen" );
                            avatar.setImageResource(R.drawable.default_avatar);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    public void updateUIMyFriends() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends";
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
                            friendsAdapter = new FriendsAdapter(userList, getActivity(), requestQueue);
                            list.setAdapter(friendsAdapter);
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
                                Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getActivity(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
    private void updateUIRequest() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/friends/requests";
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
                            requestAdapter = new RequestAdapter(userList, getActivity(), requestQueue);
                            list.setAdapter(requestAdapter);
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
                                Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getActivity(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
    private String getFromSharedPrefs() {
        SharedPreferences sharedPrefs = getActivity().getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }

}

