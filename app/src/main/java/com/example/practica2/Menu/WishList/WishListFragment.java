package com.example.practica2.Menu.WishList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WishListFragment extends Fragment {
    private Button getWishlistsButton;
    private Button getWishlistByIdButton;
    private Button deleteWishlistButton;
    private Button editWishlistButton;
    private TextView wishlistResultTextView;
    private RequestQueue requestQueue;

    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlists, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());

        getWishlistsButton = view.findViewById(R.id.getWishlistsButton);
        getWishlistByIdButton = view.findViewById(R.id.getWishlistByIdButton);
        deleteWishlistButton = view.findViewById(R.id.deleteWishlistButton);
        editWishlistButton = view.findViewById(R.id.editWishlistButton);
        wishlistResultTextView = view.findViewById(R.id.wishlistResultTextView);

        getWishlistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWishlists();
            }
        });

        getWishlistByIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wishlistId = 1; // Replace with the actual wishlist ID
                getWishlistById(wishlistId);
            }
        });

        deleteWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wishlistId = 1; // Replace with the actual wishlist ID
                deleteWishlist(wishlistId);
            }
        });

        editWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wishlistId = 1; // Replace with the actual wishlist ID
                editWishlist(wishlistId);
            }
        });

        return view;
    }

    private void getWishlists() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        wishlistResultTextView.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error getting wishlists", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void getWishlistById(int wishlistId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        wishlistResultTextView.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error getting wishlist", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void deleteWishlist(int wishlistId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Wishlist deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error deleting wishlist", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void editWishlist(int wishlistId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", "New Name"); // Replace with the new name
            requestBody.put("description", "New Description"); // Replace with the new description
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Wishlist edited successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error editing wishlist", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
