package com.example.practica2.Menu.Home.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.example.practica2.ClassObjects.Product;
import com.example.practica2.ClassObjects.RoundedCornerTransformation;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.Chats.AddFriend.AllUserAdapter;
import com.example.practica2.Menu.Chats.AddFriend.NewFriendActivity;
import com.example.practica2.Menu.WishList.WishListAdapter;
import com.example.practica2.Menu.WishList.WishListsFragment;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductActivity extends AppCompatActivity {
    private ImageView backButton;
    private ImageView image;
    private Button addWishlistProduct;
    private TextView name;
    private TextView description;
    private TextView price;
    private int id;
    private Product product;
    private RequestQueue requestQueue;
    private List<WishList> wishLists;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        saveToSharedPrefs(getIntent().getStringExtra("token"));

        requestQueue = Volley.newRequestQueue(ProductActivity.this);
        id = getIntent().getIntExtra("Product_ID", -1);
        userID = getIntent().getStringExtra("User_ID");
        addWishlistProduct = findViewById(R.id.PR_button_AddtoWishList);
        description = findViewById(R.id.PR_description);

        name = findViewById(R.id.PR_title);
        image = findViewById(R.id.PR_wishlist_image);

        backButton = findViewById(R.id.PR_back_button);

        price = findViewById(R.id.PR_price);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.super.onBackPressed();
            }
        });

        addWishlistProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllWishlists();
            }
        });

        updateUI();
    }
    private void updateUI() {

        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Obtener los valores de las propiedades del producto
                        try {
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String link = response.getString("link");
                            String photo = response.getString("photo");
                            double price = response.getDouble("price");
                            int isActive = response.getInt("is_active");
                            JSONArray categoryIdsArray = response.getJSONArray("categoryIds");
                            // Obtener los IDs de categoría como una lista de enteros (si es necesario)
                            List<Integer> categoryIds = new ArrayList<>();
                            for (int j = 0; j < categoryIdsArray.length(); j++) {
                                int categoryId = categoryIdsArray.getInt(j);
                                categoryIds.add(categoryId);
                            }

                            product = new Product(id, name, description, link, photo, price, isActive, categoryIds);
                            loadInfo(product);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            switch (statusCode) {
                                case 204:
                                    Toast.makeText(getApplicationContext(), R.string.Error_204, Toast.LENGTH_SHORT).show();
                                    break;
                                case 400:
                                    Toast.makeText(getApplicationContext(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getApplicationContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                                    break;
                                case 406:
                                    Toast.makeText(getApplicationContext(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                                    break;
                                case 502:
                                    Toast.makeText(getApplicationContext(), R.string.Error_502, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                                    break;
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
                                if (userId == Integer.parseInt(userID)){
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
                        Toast.makeText(ProductActivity.this, "Error getting wishlists", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        builder.setTitle("Select Wishlist");
        builder.setItems(wishlistNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción a realizar cuando se selecciona una lista de deseos
                WishList selectedWishlist = wishLists.get(which);
                createGift(selectedWishlist.getId());

            }
        });
        builder.show();
    }
    private void createGift (int wishList_ID){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts";

        // Crear el objeto JSON que se enviará en el cuerpo de la solicitud PUT
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("wishlist_id", wishList_ID);
            requestBody.put("product_url", "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/" + product.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

// Crear la solicitud JsonObjectRequest con el método PUT
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               Toast.makeText(ProductActivity.this,"Added!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String errorResponse = new String(error.networkResponse.data, "UTF-8");
                    Log.e("w", errorResponse);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                // Manejar el error de la solicitud
                Toast.makeText(ProductActivity.this, "Error updating wishlist", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // Agregar los encabezados de autorización a la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }
        };

// Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);

    }

    private void loadInfo (Product product){
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText("  " + product.getPrice() + " €  ");

        try {
            Picasso.get().load(product.getPhoto()).transform(new RoundedCornerTransformation(20,image)).into(image);
        }catch (Exception e){
            Log.e("error", "Usuario sin imagen: " + product.getName());
            image.setImageResource(R.drawable.default_product_image);
        }
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
