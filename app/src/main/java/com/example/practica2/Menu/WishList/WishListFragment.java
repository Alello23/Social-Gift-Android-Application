package com.example.practica2.Menu.WishList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.practica2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WishListFragment extends Fragment {
    private ImageView addWishListButton;
    private WishListFragment wishListFragment;
    private RequestQueue requestQueue;

    public WishListFragment(RequestQueue requestQueue) {
        wishListFragment = this;
        this.requestQueue = requestQueue;
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_wishlists, container, false);

        addWishListButton = view.findViewById(R.id.WIS_add_wish_list_bt);

        addWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ME_fragmentContainerView, new NewWishlistFragment(wishListFragment, requestQueue));
                fragmentTransaction.commit();
            }
        });

        // 获取所有心愿清单
        getAllWishlists();

        return view;
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


                                View wishlistItemView = LayoutInflater.from(getContext()).inflate(R.layout.element_wishlist, null);

                                ImageView avatarImageView = wishlistItemView.findViewById(R.id.WI_avatarImage_FC);
                                TextView nameTextView = wishlistItemView.findViewById(R.id.WI_edit_button);
                                TextView descriptionTextView = wishlistItemView.findViewById(R.id.WI_description);

                                // avatarImageView.setImageDrawable(...); // 设置头像图像
                                nameTextView.setText(name);
                                descriptionTextView.setText(description);

                                // parentLayout.addView(wishlistItemView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error getting wishlists", Toast.LENGTH_SHORT).show();
                    }

                });

        requestQueue.add(jsonArrayRequest);
    }
}
