package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;

import org.json.JSONObject;

public class MyAccountActivity extends AppCompatActivity {

    private Button btnLogout;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        requestQueue = Volley.newRequestQueue(MyAccountActivity.this);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里发送 DELETE 请求
                deleteUserData();
            }
        });
    }

    private void deleteUserData() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/logout";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                        // After logout, redirect user to sign in page (MainActivity)
                        Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // This will make sure MyAccountActivity won't come back when you press back button from MainActivity.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Logout failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

}

