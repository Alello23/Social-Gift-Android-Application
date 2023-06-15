package com.example.practica2.Menu.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.Login_Register.MainActivity;
import com.example.practica2.Login_Register.SignUpActivity;
import com.example.practica2.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    String token;
    TextView deleteAccount;
    RequestQueue requestQueue;

    public AccountFragment(String token, RequestQueue requestQueue) {
        this.token = token;
        this.requestQueue = requestQueue;
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        deleteAccount = view.findViewById(R.id.P_deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes implementar la acción que deseas realizar al hacer clic en el texto
                deleteUser();
            }
        });

        return view;
    }

    private void deleteUser() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorString = new String(error.networkResponse.data, "UTF-8");
                            Log.e("error", errorString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // Handle error response
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(getActivity(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(getActivity(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
