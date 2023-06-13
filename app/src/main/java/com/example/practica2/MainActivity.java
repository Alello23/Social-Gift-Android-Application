package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button SignIn;
    private TextView ChangeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignIn = (Button) findViewById(R.id.SI_SignIn_Button);

        ChangeBox = (TextView) findViewById(R.id.SI_SignUpLabel);
        ChangeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se hace clic en el texto
                // Puedes colocar aquí el código que deseas ejecutar al hacer clic
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               makeRequest();             //Canbiar a la logica de codigo
            }
        });


    }

//    private void makeRequest() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("resposta", "La resposta es: "+ response.toString());
//                        textView.setText(response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("resposta", "Hi ha hagut un error:" + error);
//                        textView.setText("Hi ha hagut un error:" + error);
//                    }
//                }
//                );
//
//        queue.add(jsonObjectRequest);
//    }
}