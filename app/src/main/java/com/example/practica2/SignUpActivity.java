package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    private TextView signInLink;
    private EditText NameBox;
    private EditText LastNameBox;
    private EditText EmailOrPhoneBox;
    private EditText PasswordBox;
    private ImageView AvatarBox;
    private Button SignUp;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        requestQueue = Volley.newRequestQueue(SignUpActivity.this);

        NameBox = findViewById(R.id.SU_NameBox);
        LastNameBox = findViewById(R.id.SU_LastNameBox);
        EmailOrPhoneBox = findViewById(R.id.SU_EmailOrPhoneBox);
        PasswordBox = findViewById(R.id.SU_PasswordBox);
        AvatarBox = findViewById(R.id.avatarImageView);
        AvatarBox.setImageResource(R.drawable.default_avatar);

        SignUp = findViewById(R.id.SU_SignUp_Button);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里发送 POST 请求
                postSignUpData();
            }
        });

        signInLink = findViewById(R.id.SU_SignInLink);
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void postSignUpData() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", NameBox.getText().toString());
            jsonParams.put("last_name", LastNameBox.getText().toString());
            jsonParams.put("email_or_phone", EmailOrPhoneBox.getText().toString());
            jsonParams.put("password", PasswordBox.getText().toString());
            jsonParams.put("image", "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/signup";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Sign up failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }
}
