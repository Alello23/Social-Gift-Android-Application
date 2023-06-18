package com.example.practica2.Menu.Home;


import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.RoundedCornerTransformation;
import com.example.practica2.ClassObjects.User;
import com.example.practica2.Menu.Chats.RequestAdapter;
import com.example.practica2.Menu.Home.Category.CategoryActivity;
import com.example.practica2.Menu.Home.Product.ProductActivity;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CategoryHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
    private ImageView image;
    private TextView title;

    private Activity activity;
    private RequestAdapter adapter;
    private Category category;
    public CategoryHolder(LayoutInflater inflater, ViewGroup parent, Activity activity){
        super(inflater.inflate(R.layout.element_category, parent, false));
        image = itemView.findViewById(R.id.categoryImage);
        title = itemView.findViewById(R.id.categoryTitle);
        itemView.setOnClickListener(this);

        this.activity = activity;
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, CategoryActivity.class);
        intent.putExtra("Category_ID", category.getId());
        intent.putExtra("token", getFromSharedPrefs());
        activity.startActivity(intent);
    }
    public void bind(Category category, RequestQueue requestQueue) {
        this.category = category;
        title.setText("  "+ category.getName() + "  ");
        try {
            Picasso.get().load(category.getPhoto()).transform(new RoundedCornerTransformation(20,image)).into(image);
        }catch (Exception e){
            Log.e("error", "Usuario sin imagen: " + category.getName());
            image.setImageResource(R.drawable.default_product_image);
        }
    }
    private String getFromSharedPrefs() {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
}

