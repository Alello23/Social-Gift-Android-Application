package com.example.practica2.Menu.WishList;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practica2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewWishlistFragment extends Fragment {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button openDatePickerButton;
    private Button openTimePickerButton;
    private Button createWishlistButton;
    private RequestQueue requestQueue;
    private WishListFragment wishListFragment;

    public NewWishlistFragment(WishListFragment wishListFragment, RequestQueue requestQueue) {
        this.wishListFragment = wishListFragment;
        this.requestQueue = requestQueue;
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_wishlist, container, false);

        editTextTitle = view.findViewById(R.id.NW_editText_Title); // Replace with actual ID
        editTextDescription = view.findViewById(R.id.NW_editText_Description); // Replace with actual ID

        openDatePickerButton = view.findViewById(R.id.NW_openPopupButton_date);
        openDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerPopup();
            }
        });

        openTimePickerButton = view.findViewById(R.id.NW_openPopupButton_time);
        openTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerPopup();
            }
        });

        createWishlistButton = view.findViewById(R.id.NW_button_CreateNewWishList); // Replace with actual ID
        createWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWishlist();
            }
        });

        return view;
    }

    private void showDatePickerPopup() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                openDatePickerButton.setText(selectedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showTimePickerPopup() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = hourOfDay + ":" + minute;
                openTimePickerButton.setText(selectedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void createWishlist() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = openDatePickerButton.getText().toString();
        String time = openTimePickerButton.getText().toString();

        String dateTime = date + "T" + time + "Z";

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", title);
            jsonBody.put("description", description);
            jsonBody.put("end_date", dateTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.ME_fragmentContainerView, wishListFragment);
                        fragmentTransaction.commit();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "UTF-8");
                            Log.e("error",errorResponse);
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Manejar el error de la solicitud
                        if (error.networkResponse != null) {
                            if(error.networkResponse.statusCode == 400) {
                                Toast.makeText(getActivity(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 401) {
                                Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 406) {
                                Toast.makeText(getActivity(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                            } else if(error.networkResponse.statusCode == 410) {
                                Toast.makeText(getActivity(), R.string.Error_410, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 500) {
                                Toast.makeText(getActivity(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                            }else if(error.networkResponse.statusCode == 502) {
                                Toast.makeText(getActivity(), R.string.Error_502, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.Error_Default, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.Error_Network, Toast.LENGTH_SHORT).show();
                        }
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(getActivity()));
                return headers;
            }

        };

        requestQueue.add(jsonArrayRequest);
    }
    private String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
}
