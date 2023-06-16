package com.example.practica2.Menu.Account;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.practica2.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private String token;
    private RequestQueue requestQueue;
    private TextView editButton;
    private EditText nameEditText, lastNameEditText, emailEditText;

    public AccountFragment(String token, RequestQueue requestQueue) {
        this.token = token;
        this.requestQueue = requestQueue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        editButton = view.findViewById(R.id.P_editAccount);
        nameEditText = view.findViewById(R.id.nameTextView);
        lastNameEditText = view.findViewById(R.id.lastNameTextView);
        emailEditText = view.findViewById(R.id.emailTextView);

        // Decode JWT to get user id
        String[] splitToken = token.split("\\.");
        byte[] decodedBytes = Base64.decode(splitToken[1], Base64.URL_SAFE);
        String jsonBody = new String(decodedBytes, StandardCharsets.UTF_8);

        try {
            JSONObject jsonObject = new JSONObject(jsonBody);
            String userId = jsonObject.getString("id");
            getUserDetails(userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.isEnabled()) {
                    updateUser();
                    nameEditText.setEnabled(false);
                    lastNameEditText.setEnabled(false);
                    emailEditText.setEnabled(false);
                    editButton.setText("Edit");
                } else {
                    nameEditText.setEnabled(true);
                    lastNameEditText.setEnabled(true);
                    emailEditText.setEnabled(true);
                    editButton.setText("Save");
                }
            }
        });

        return view;
    }

    private void getUserDetails(String userId) {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            nameEditText.setText(response.getString("name"));
                            lastNameEditText.setText(response.getString("last_name"));
                            emailEditText.setText(response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void updateUser() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // handle response here
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error here
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameEditText.getText().toString());
                params.put("last_name", lastNameEditText.getText().toString());
                params.put("email", emailEditText.getText().toString());
                // Add password if necessary
                params.put("password", "passwordHere");
                params.put("image", "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(putRequest);
    }
}