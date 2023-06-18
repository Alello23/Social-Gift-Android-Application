package com.example.practica2.Menu.WishList;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.Home.WishListAdapter;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishListFragment extends Fragment {
    private ImageView addWishListButton;
    private WishListFragment wishListFragment;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private WishListAdapter wishListAdapter;
    private ImageView avatar;
    private List<WishList> wishLists;
    private String userID;

    public WishListFragment(RequestQueue requestQueue, String userID) {
        wishListFragment = this;
        this.requestQueue = requestQueue;

        this.userID = userID;
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_wishlists, container, false);

        addWishListButton = view.findViewById(R.id.WIS_add_wish_list_bt);
        recyclerView = view.findViewById(R.id.WIS_input_gift);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wishLists = new ArrayList<>();
        avatar = view.findViewById(R.id.WIS_avatarImage_FC);

        addWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ME_fragmentContainerView, new NewWishlistFragment(wishListFragment, requestQueue));
                fragmentTransaction.commit();
            }
        });

        getAllWishlists();
        updateProfileAvatar();

        return view;
    }
    public void updateProfileAvatar() {
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

        private void getAllWishlists() {
            String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject wishlistObject = response.getJSONObject(i);
                                    int id = wishlistObject.getInt("id");
                                    String name = wishlistObject.getString("name");
                                    String description = wishlistObject.getString("description");
                                    int userId = wishlistObject.getInt("user_id");
                                    String creationDate = wishlistObject.getString("creation_date");
                                    String endDate = wishlistObject.getString("end_date");

                                    List<Gift> gifts = new ArrayList<>();
                                    if (userId == Integer.parseInt(userID)){
                                        WishList wishList = new WishList(id, name, description, userId, gifts, creationDate, endDate);
                                        wishLists.add(wishList);
                                    }
                                }
                                wishListAdapter = new WishListAdapter(wishLists, getActivity(), requestQueue);
                                recyclerView.setAdapter(wishListAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error getting wishlists", Toast.LENGTH_SHORT).show();
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
    public String getFromSharedPrefs() {
        SharedPreferences sharedPrefs = getActivity().getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
}
