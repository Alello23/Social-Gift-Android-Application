package com.example.practica2.Menu.WishList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.Gift;
import com.example.practica2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WishlistItemFragment extends Fragment {
    private RecyclerView recyclerView;
    private GiftAdapter giftAdapter;
    private List<Gift> giftList;
    private RequestQueue requestQueue;
    private int wishlistId;

    public WishlistItemFragment(RequestQueue requestQueue, int wishlistId) {
        this.requestQueue = requestQueue;
        this.wishlistId = wishlistId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist_item, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_gifts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        giftList = new ArrayList<>();
        giftAdapter = new GiftAdapter(giftList);
        recyclerView.setAdapter(giftAdapter);

        getWishlistById(wishlistId);

        return view;
    }

    private void getWishlistById(int wishlistId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray giftsArray = response.getJSONArray("gifts");

                            for (int i = 0; i < giftsArray.length(); i++) {
                                JSONObject giftObject = giftsArray.getJSONObject(i);
                                int giftId = giftObject.getInt("id");
                                int wishlistId = giftObject.getInt("wishlist_id");
                                String productUrl = giftObject.getString("product_url");
                                int priority = giftObject.getInt("priority");
                                boolean booked = giftObject.getBoolean("booked");

                                Gift gift = new Gift(giftId, wishlistId, productUrl, priority, booked);
                                giftList.add(gift);
                            }

                            giftAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error retrieving wishlist", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
