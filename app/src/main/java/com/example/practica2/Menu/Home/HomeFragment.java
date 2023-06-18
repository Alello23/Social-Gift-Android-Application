package com.example.practica2.Menu.Home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.Product;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.AddFriend.AllUserAdapter;
import com.example.practica2.Menu.Chats.AddFriend.NewFriendActivity;
import com.example.practica2.Menu.WishList.NewWishlistFragment;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private RecyclerView categoryList;
    private RecyclerView shopList;
    private ImageView avatar;
    private ImageView newProduct_bt;
    private SearchView searchView;
    private List<Category> categories;
    private List<Product> products;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private RequestQueue requestQueue;
    private HomeFragment homeFragment;
    private String userID;

    public HomeFragment(RequestQueue requestQueue, String userID) {
        this.requestQueue = requestQueue;
        homeFragment = this;
        this.userID = userID;
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        avatar = view.findViewById(R.id.HO_avatarImage_FC);
        newProduct_bt = view.findViewById(R.id.HO_new_product_bt);
        shopList = view.findViewById(R.id.HO_shopRecyclerView);
        categoryList = view.findViewById(R.id.HO_Category);
        searchView = view.findViewById(R.id.HO_searchView);
        newProduct_bt = view.findViewById(R.id.HO_new_product_bt);

        categoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        shopList.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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
        newProduct_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ME_fragmentContainerView, new NewProductFragment(homeFragment, requestQueue));
                fragmentTransaction.commit();
            }
        });
        updateProfileAvatar();
        updateUICategory();
        updateUIProduct();
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
    private void performSearch(String query){
        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/search?s=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            products = new ArrayList<>();
                            // Iterar sobre los elementos del arreglo JSON
                            for (int i = 0; i < response.length(); i++) {
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
                                }

                                Product product = new Product(id, name, description, link, photo, price, isActive, categoryIds);
                                products.add(product);
                            }
                            productAdapter = new ProductAdapter(products, getActivity(), requestQueue);
                            shopList.setAdapter(productAdapter);
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
                                Toast.makeText(getActivity(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(getActivity(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getActivity(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 502) {
                                Toast.makeText(getActivity(), R.string.Error_502, Toast.LENGTH_SHORT).show();
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

    public void updateUIProduct() {
        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    products = new ArrayList<>();
                    // Iterar sobre los elementos del arreglo JSON
                    for (int i = 0; i < response.length(); i++) {
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
                        }

                        Product product = new Product(id, name, description, link, photo, price, isActive, categoryIds);
                        products.add(product);
                    }
                    productAdapter = new ProductAdapter(products, getActivity(), requestQueue);
                    shopList.setAdapter(productAdapter);
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
                        Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
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

    public void updateUICategory() {
        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/categories";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    categories = new ArrayList<>();
                    // Iterar sobre los elementos del arreglo JSON
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject userObject = response.getJSONObject(i);

                        // Obtener los valores de las propiedades del usuario
                        int id = userObject.getInt("id");
                        String name = userObject.getString("name");
                        String description = userObject.getString("description");
                        String photo = userObject.getString("photo");
                        int categoryParentId = userObject.optInt("categoryParentId", -1);

                        categories.add(new Category(id, name, description, photo, categoryParentId));
                    }
                    categoryAdapter = new CategoryAdapter(categories, getActivity(), requestQueue);
                    categoryList.setAdapter(categoryAdapter);
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
                        Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
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

    public String getFromSharedPrefs() {
        SharedPreferences sharedPrefs = getActivity().getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
}
