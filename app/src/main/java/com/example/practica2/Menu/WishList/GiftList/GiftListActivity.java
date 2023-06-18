package com.example.practica2.Menu.WishList.GiftList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.ClassObjects.Product;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.Home.Category.CategoryActivity;
import com.example.practica2.Menu.Home.ProductAdapter;
import com.example.practica2.Menu.WishList.WishListAdapter;
import com.example.practica2.Menu.WishList.WishListsFragment;
import com.example.practica2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftListActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView list;
    private TextView title;
    private RequestQueue requestQueue;
    private int id_wishlist;
    private List<Gift> gifts;
    private WishList wishList;
    private GiftAdapter giftAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wishlist);

        saveToSharedPrefs(getIntent().getStringExtra("token"));
        id_wishlist = getIntent().getIntExtra("WishList_ID", -1);
        requestQueue = Volley.newRequestQueue(GiftListActivity.this);

        backButton = findViewById(R.id.WIT_button_back);
        list = findViewById(R.id.WIT_input_gift);
        list.setLayoutManager(new GridLayoutManager(this, 2));

        title = findViewById(R.id.WIT_text_WishList);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftListActivity.super.onBackPressed();
            }
        });

        updateUIProduct();
    }
    public void updateUIProduct() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + id_wishlist;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject wishlistObject = response.getJSONObject("wishlist");
                    int id = wishlistObject.getInt("id");
                    String name = wishlistObject.getString("name");
                    String description = wishlistObject.getString("description");
                    int userId = wishlistObject.getInt("user_id");
                    String creationDate = wishlistObject.getString("creation_date");
                    String endDate = wishlistObject.getString("end_date");

                    List<Gift> gifts = new ArrayList<>();
                    JSONArray giftsArray = wishlistObject.getJSONArray("gifts");
                    for (int i = 0; i < giftsArray.length(); i++) {
                        JSONObject giftObject = giftsArray.getJSONObject(i);
                        int giftId = giftObject.getInt("id");
                        int wishlistId = giftObject.getInt("wishlist_id");
                        String productUrl = giftObject.getString("product_url");
                        int priority = giftObject.getInt("priority");
                        boolean isBooked = giftObject.getBoolean("booked");

                        Gift gift = new Gift(giftId, wishlistId, productUrl, priority, isBooked);
                        gifts.add(gift);
                    }
                    title.setText(name);

                    wishList = new WishList(id, name, description, userId, gifts, creationDate, endDate);
                    giftAdapter = new GiftAdapter(gifts, GiftListActivity.this, requestQueue);
                    list.setAdapter(giftAdapter);
                    giftAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(GiftListActivity.this, R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
                        Toast.makeText(GiftListActivity.this, R.string.Error_500, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GiftListActivity.this, R.string.Error_Default, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GiftListActivity.this, R.string.Error_Network, Toast.LENGTH_SHORT).show();
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


}
