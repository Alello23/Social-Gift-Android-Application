package com.example.practica2.Menu.WishList.GiftList;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.ClassObjects.RoundedCornerTransformation;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftViewHolder extends RecyclerView.ViewHolder {
    private TextView giftNameTextView;
    private TextView giftDescription;
    private TextView giftPrice;
    private TextView reserved;
    private Activity activity;
    private ImageView image;
    private Button button;
    private RequestQueue requestQueue;
    private Gift gift;
    private GiftAdapter giftAdapter;
    private List<Gift> giftList; // Nueva lista de regalos
    public GiftViewHolder(LayoutInflater inflater, ViewGroup parent, Activity activity, RequestQueue requestQueue, List<Gift> giftList){
        super(inflater.inflate(R.layout.element_gift, parent, false));
        giftNameTextView = itemView.findViewById(R.id.GI_title_WishList);
        giftDescription = itemView.findViewById(R.id.GI_description);
        image = itemView.findViewById(R.id.GI_avatarImage_2);
        giftPrice = itemView.findViewById(R.id.GI_Price);
        button = itemView.findViewById(R.id.GI_chat_remove_button);
        reserved = itemView.findViewById(R.id.reserved);
        this.giftList = giftList;
        this.requestQueue = requestQueue;

        this.activity = activity;
    }

    public void bind(Gift gift,GiftAdapter giftAdapter) {
        this.gift = gift;
        this.giftAdapter = giftAdapter;

        if (activity.getIntent().getIntExtra("my",0) == 1){
            if (gift.isBooked()) {
                button.setText(R.string.Unreserve);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        un_reserve_gift();
                    }
                });
            }else {
                button.setText(R.string.reserve);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reserve_gift();
                    }
                });
            }
        }else {
            button.setText(R.string.remove);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_gift();
                }
            });
        }


        loadProduct(requestQueue, gift.getProductUrl());
    }

    public void removeGiftFromList() {
        // Eliminar el regalo de la lista
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            giftList.remove(position);
            giftAdapter.notifyItemRemoved(position);
        }
    }
    public void delete_gift(){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + gift.getId();

        // Crear la solicitud JsonObjectRequest con el método DELETE
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity, "Yay", Toast.LENGTH_SHORT).show();
                removeGiftFromList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                Toast.makeText(activity, "Error deleting gift", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // Agregar los encabezados de autorización a la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }
        };

// Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }

    public void reserve_gift(){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + gift.getId() + "/book";

        // Crear la solicitud JsonObjectRequest con el método DELETE
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity, "Reserved", Toast.LENGTH_SHORT).show();
                button.setText(R.string.Unreserve);
                reserved.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        un_reserve_gift();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                Toast.makeText(activity, "Error deleting gift", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // Agregar los encabezados de autorización a la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }
        };

// Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }

    public void un_reserve_gift(){
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/" + gift.getId() + "/book";

        // Crear la solicitud JsonObjectRequest con el método DELETE
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(activity, "un_reserved", Toast.LENGTH_SHORT).show();
                button.setText(R.string.reserve);
                reserved.setVisibility(View.GONE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reserve_gift();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                Toast.makeText(activity, "Error deleting gift", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // Agregar los encabezados de autorización a la solicitud
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }
        };

// Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }
    public void loadProduct(RequestQueue requestQueue, String url) {

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            giftNameTextView.setText(response.getString("name"));
                            giftDescription.setText(response.getString("description"));
                            giftPrice.setText("  "+response.getString("price") + " €  ");

                            try {
                                Picasso.get().load(response.getString("photo")).transform(new CircleImage()).into(image);
                            }catch (Exception e){
                                Log.e("error", "Usuario sin imagen: " + response.getString("name"));
                                image.setImageResource(R.drawable.default_product_image);
                            }
                            if (gift.isBooked()){
                                reserved.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "UTF-8");
                            Log.e("error",errorResponse);
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(activity, R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(activity, R.string.Error_401, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(activity, R.string.Error_406, Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }

        };

        requestQueue.add(jsonArrayRequest);
    }
    public String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
}
