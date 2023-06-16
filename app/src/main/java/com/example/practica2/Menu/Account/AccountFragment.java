package com.example.practica2.Menu.Account;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.practica2.Login_Register.MainActivity;
import com.example.practica2.Login_Register.SignUpActivity;
import com.example.practica2.Menu.Menu;
import com.example.practica2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private TextView deleteAccount;
    private TextView logOut;
    private RequestQueue requestQueue;
    private TextView editButton;
    private EditText nameEditText, lastNameEditText, emailEditText;
    private String userID;

    public AccountFragment(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        deleteAccount = view.findViewById(R.id.AC_delete_account_button);
        editButton = view.findViewById(R.id.AC_edit_account_button);
        nameEditText = view.findViewById(R.id.AC_edit_name);
        lastNameEditText = view.findViewById(R.id.AC_edit_last_name);
        emailEditText = view.findViewById(R.id.AC_edit_email);
        logOut = view.findViewById(R.id.AC_log_out_button);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes implementar la acción que deseas realizar al hacer clic en el texto
                deleteUser();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.isEnabled()) {
                    updateUser();
                    nameEditText.setEnabled(false);
                    lastNameEditText.setEnabled(false);
                    emailEditText.setEnabled(false);
                    editButton.setText(R.string.edit_account);
                } else {
                    nameEditText.setEnabled(true);
                    lastNameEditText.setEnabled(true);
                    emailEditText.setEnabled(true);
                    editButton.setText(R.string.save_account);
                }
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        decodeJWT();

        return view;
    }

    private void decodeJWT (){
        // Decode JWT to get user id
        String[] splitToken = getFromSharedPrefs().split("\\.");
        byte[] decodedBytes = Base64.decode(splitToken[1], Base64.URL_SAFE);
        String jsonBody = new String(decodedBytes, StandardCharsets.UTF_8);

        try {
            JSONObject jsonObject = new JSONObject(jsonBody);
            userID = jsonObject.getString("id");
            getUserDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserDetails() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + userID;

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
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void updateUser() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", nameEditText.getText().toString());
            jsonParams.put("last_name", lastNameEditText.getText().toString());
            jsonParams.put("email", emailEditText.getText().toString());
            jsonParams.put("image", "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), R.string.update_succesful, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getUserDetails();
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(getContext(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getContext(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(getContext(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 409) {
                                Toast.makeText(getContext(), R.string.Error_409, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 410) {
                                Toast.makeText(getContext(), R.string.Error_410, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }
        };

        requestQueue.add(putRequest);
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
                headers.put("Authorization", "Bearer " + getFromSharedPrefs());
                return headers;
            }
        };
        requestQueue.add(request);
    }
    public String getFromSharedPrefs() {
        SharedPreferences sharedPrefs =
                getActivity().getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }

}
