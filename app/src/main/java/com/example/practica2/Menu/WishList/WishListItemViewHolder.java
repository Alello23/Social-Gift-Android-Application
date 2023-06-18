package com.example.practica2.Menu.WishList;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.R;

public class WishListItemViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;
    private ImageView moreOptions;

    private Activity activity;

    public WishListItemViewHolder(LayoutInflater inflater, ViewGroup parent, Activity activity) {
        super(inflater.inflate(R.layout.element_wishlist, parent, false));
        description = itemView.findViewById(R.id.WI_description);
        title = itemView.findViewById(R.id.WI_title);
        moreOptions = itemView.findViewById(R.id.WI_more_option);

        this.activity = activity;
    }

    public void bind(WishList wishList, RequestQueue requestQueue) {
        title.setText(wishList.getName());
        description.setText(wishList.getDescription());

        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubmenuDialog(wishList);
            }
        });
    }

    private void showSubmenuDialog(WishList wishList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.fragment_wish_list_submenu, null);
        builder.setView(dialogView);

        TextView editTextView = dialogView.findViewById(R.id.UO_configurationTextView);
        TextView deleteTextView = dialogView.findViewById(R.id.UO_addFriendTextView);

        AlertDialog dialog = builder.create();
        dialog.show();

        editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 处理编辑操作
                Toast.makeText(activity, "Edit clicked", Toast.LENGTH_SHORT).show();

                // 跳转到编辑页面（NewWishlistFragment）并传递要编辑的愿望列表对象
                Fragment fragment = new NewWishlistFragment(wishListFragment, requestQueue, wishList);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 处理删除操作
                Toast.makeText(activity, "Delete clicked", Toast.LENGTH_SHORT).show();

                // 删除愿望列表
                deleteWishlist(wishList.getId());
            }
        });
    }

    private void deleteWishlist(int wishlistId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishlistId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 删除成功后刷新列表
                        wishListFragment.refreshWishlists();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 处理错误
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(activity, "Unauthorized", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Error deleting wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPrefs.getString("token", "");
    }
}
}