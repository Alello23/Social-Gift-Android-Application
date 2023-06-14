package com.example.practica2.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica2.Menu.Menu;
import com.example.practica2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private Button SignIn;
    private TextView ChangeBox;
    private EditText UserName;
    private EditText Password;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        SignIn = findViewById(R.id.SI_SignIn_Button);


        ChangeBox = findViewById(R.id.SI_SignUpLabel);

        UserName = findViewById(R.id.SI_NameBox);
        Password = findViewById(R.id.SI_PasswordBox);

        ChangeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se hace clic en el texto
                // Puedes colocar aquí el código que deseas ejecutar al hacer clic
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = UserName.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // El correo electrónico ingresado no es válido
                    Toast.makeText(getApplicationContext(), R.string.Invalid_email, Toast.LENGTH_SHORT).show();
                }else{
                    makeRequest();
                }
            }
        });


    }

    private void makeRequest() {
        // Crea un objeto JSON con los parámetros de inicio de sesión
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("email", UserName.getText().toString());
            jsonParams.put("password", Password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea una solicitud POST con el cuerpo JSON
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/login";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(MainActivity.this, Menu.class);
                        intent.putExtra("Token", response.toString());
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                        String errorMessage = "Error en la solicitud";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                // Analizar la respuesta de error del servidor
                                String errorResponse = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorResponse);
                                String errorCode = errorJson.getJSONObject("error").optString("code");
                                errorMessage = errorJson.getJSONObject("error").optString("message");
                                // Realizar acciones basadas en el código de error y mensaje de error
                                if (errorCode.equals("NOT_AUTHORIZED")) {
                                    Toast.makeText(getApplicationContext(), R.string.wrongAcces, Toast.LENGTH_SHORT).show();
                                }
                                else if (errorCode.equals("MISSING_FIELDS")) {
                                    Toast.makeText(getApplicationContext(), R.string.Missing_Fields, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), R.string.DefaultError, Toast.LENGTH_SHORT).show();
                                }
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("TAG", errorMessage);
                    }
                });

        requestQueue.add(request);
    }
}