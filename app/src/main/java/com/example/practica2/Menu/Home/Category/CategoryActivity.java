package com.example.practica2.Menu.Home.Category;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica2.ClassObjects.Product;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Home.ProductAdapter;
import com.example.practica2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {
    private ImageView backButton;
    private SearchView searchView;
    private RecyclerView list;
    private RequestQueue requestQueue;
    private ProductAdapter adapter;
    private int id_category;
    private List<Product> products;
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        saveToSharedPrefs(getIntent().getStringExtra("token"));
        id_category = getIntent().getIntExtra("Category_ID", -1);
        requestQueue = Volley.newRequestQueue(CategoryActivity.this);
        saveID(getIntent().getStringExtra("user_id"));

        searchView = findViewById(R.id.AF_searchView);
        backButton = findViewById(R.id.AF_back_button);
        list = findViewById(R.id.AF_insertUsers);
        list.setLayoutManager(new GridLayoutManager(this, 2));



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryActivity.super.onBackPressed();
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

        updateUIProduct();
    }
    public void updateUIProduct() {
        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    products = new ArrayList<>();
                    // Iterar sobre los elementos del arreglo JSON
                    for (int i = 0; i < response.length(); i++) {
                        boolean isCategory = false;
                        JSONObject productObject = response.getJSONObject(i);

                        int id = productObject.getInt("id");
                        String name = productObject.getString("name");
                        String description = productObject.getString("description");
                        String link = productObject.getString("link");
                        String photo = productObject.getString("photo");
                        double price = productObject.getDouble("price");
                        int isActive = productObject.getInt("is_active");
                        JSONArray categoryIdsArray = productObject.getJSONArray("categoryIds");
                        // Obtener los IDs de categoría como una lista de enteros (si es necesario)
                        List<Integer> categoryIds = new ArrayList<>();
                        for (int j = 0; j < categoryIdsArray.length(); j++) {
                            int categoryId = categoryIdsArray.getInt(j);
                            categoryIds.add(categoryId);
                            if (categoryId == id_category){
                                isCategory = true;
                            }
                        }
                        if (isCategory){
                            Product product = new Product(id, name, description, link, photo, price, isActive, categoryIds);
                            products.add(product);
                        }
                    }
                    productAdapter = new ProductAdapter(products, CategoryActivity.this, requestQueue);
                    list.setAdapter(productAdapter);
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
                        Toast.makeText(CategoryActivity.this, R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
                        Toast.makeText(CategoryActivity.this, R.string.Error_500, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CategoryActivity.this, R.string.Error_Default, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
    public void saveID(String id) {
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
        prefsEditor.putString("user_id", id);
        prefsEditor.apply();
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
        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/search?s=" + query;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    products = new ArrayList<>();
                    // Iterar sobre los elementos del arreglo JSON
                    for (int i = 0; i < response.length(); i++) {
                        boolean isCategory = false;
                        JSONObject productObject = response.getJSONObject(i);

                        int id = productObject.getInt("id");
                        String name = productObject.getString("name");
                        String description = productObject.getString("description");
                        String link = productObject.getString("link");
                        String photo = productObject.getString("photo");
                        double price = productObject.getDouble("price");
                        int isActive = productObject.getInt("is_active");
                        JSONArray categoryIdsArray = productObject.getJSONArray("categoryIds");
                        // Obtener los IDs de categoría como una lista de enteros (si es necesario)
                        List<Integer> categoryIds = new ArrayList<>();
                        for (int j = 0; j < categoryIdsArray.length(); j++) {
                            int categoryId = categoryIdsArray.getInt(j);
                            categoryIds.add(categoryId);
                            if (categoryId == id_category){
                                isCategory = true;
                            }
                        }
                        if (isCategory){
                            Product product = new Product(id, name, description, link, photo, price, isActive, categoryIds);
                            products.add(product);
                        }
                    }
                    productAdapter = new ProductAdapter(products, CategoryActivity.this, requestQueue);
                    list.setAdapter(productAdapter);
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
                        Toast.makeText(CategoryActivity.this, R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
                        Toast.makeText(CategoryActivity.this, R.string.Error_500, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CategoryActivity.this, R.string.Error_Default, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, R.string.Error_Network, Toast.LENGTH_SHORT).show();
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
