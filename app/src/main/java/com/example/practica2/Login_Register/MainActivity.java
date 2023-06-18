package com.example.practica2.Login_Register;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private Button SignIn;
    private TextView ChangeBox;
    private EditText UserName;
    private EditText Password;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Inicialización de la cola de solicitudes
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        // Obtención de referencias a los elementos de la interfaz de usuario
        SignIn = findViewById(R.id.SI_SignIn_Button);
        ChangeBox = findViewById(R.id.SI_SignUpLabel);
        UserName = findViewById(R.id.SI_NameBox);
        Password = findViewById(R.id.SI_PasswordBox);

        // Configuración del evento de clic para cambiar a la pantalla de registro
        ChangeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se hace clic en el texto
                // Se inicia la actividad de registro y se finaliza la actividad actual
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Configuración del evento de clic para el botón de inicio de sesión
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = UserName.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // El correo electrónico ingresado no es válido
                    Toast.makeText(getApplicationContext(), R.string.Error_Email, Toast.LENGTH_SHORT).show();
                } else {
                    // Se realiza la solicitud de inicio de sesión
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
                        // La solicitud fue exitosa
                        // Se inicia la actividad del menú y se pasa el token de acceso
                        Intent intent = new Intent(MainActivity.this, Menu.class);
                        try {
                            intent.putExtra("User", response.getString("accessToken"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Se manejan los errores de la respuesta de la solicitud
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 400) {
                                Toast.makeText(getApplicationContext(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if (error.networkResponse.statusCode == 401) {
                                Toast.makeText(getApplicationContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if (error.networkResponse.statusCode == 406) {
                                Toast.makeText(getApplicationContext(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        // No hace nada
    }
}
