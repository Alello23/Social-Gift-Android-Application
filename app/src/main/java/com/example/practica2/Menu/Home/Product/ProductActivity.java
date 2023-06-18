package com.example.practica2.Menu.Home.Product;

import android.content.Context;
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
import com.example.practica2.ClassObjects.Message_user;
import com.example.practica2.ClassObjects.Product;
import com.example.practica2.ClassObjects.RoundedCornerTransformation;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.AddFriend.AllUserAdapter;
import com.example.practica2.Menu.Chats.AddFriend.NewFriendActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        saveToSharedPrefs(getIntent().getStringExtra("token"));

        requestQueue = Volley.newRequestQueue(ProductActivity.this);
        id = getIntent().getIntExtra("Product_ID", -1);

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

//        addWishlistProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Acción a realizar cuando se presione el botón "Enviar"
//                String mensaje = input.getText().toString();
//                input.setText("");
//                sendMessage(mensaje);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                // Oculta el teclado virtual
//                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
//            }
//        });

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

                            Product product = new Product(id, name, description, link, photo, price, isActive, categoryIds);
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
