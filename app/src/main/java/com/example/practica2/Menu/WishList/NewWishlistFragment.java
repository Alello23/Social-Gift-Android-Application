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
    private WishList wishList; // 用于编辑模式

    public NewWishlistFragment(WishListFragment wishListFragment, RequestQueue requestQueue) {
        this.wishListFragment = wishListFragment;
        this.requestQueue = requestQueue;
        this.wishList = null; // 默认为创建模式
    }

    public NewWishlistFragment(WishListFragment wishListFragment, RequestQueue requestQueue, WishList wishList) {
        this.wishListFragment = wishListFragment;
        this.requestQueue = requestQueue;
        this.wishList = wishList; // 编辑模式
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_wishlist, container, false);

        editTextTitle = view.findViewById(R.id.NW_editText_Title);
        editTextDescription = view.findViewById(R.id.NW_editText_Description);
        openDatePickerButton = view.findViewById(R.id.NW_openPopupButton_date);
        openTimePickerButton = view.findViewById(R.id.NW_openPopupButton_time);
        createWishlistButton = view.findViewById(R.id.NW_button_CreateNewWishList);

        openDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerPopup();
            }
        });

        openTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerPopup();
            }
        });

        createWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishList == null) {
                    createWishlist();
                } else {
                    updateWishlist();
                }
            }
        });

        // 编辑模式下，填充现有的愿望列表信息
        if (wishList != null) {
            editTextTitle.setText(wishList.getName());
            editTextDescription.setText(wishList.getDescription());
        }

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(requireContext(), "Wishlist created", Toast.LENGTH_SHORT).show();
                        wishListFragment.refreshWishlists();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeaders();
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void updateWishlist() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = openDatePickerButton.getText().toString();
        String time = openTimePickerButton.getText().toString();
        String dateTime = date + "T" + time + "Z";

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishList.getId();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", title);
            jsonBody.put("description", description);
            jsonBody.put("end_date", dateTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(requireContext(), "Wishlist updated", Toast.LENGTH_SHORT).show();
                        wishListFragment.refreshWishlists();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeaders();
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private Map<String, String> getRequestHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getFromSharedPrefs(requireActivity()));
        return headers;
    }

    private String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPrefs.getString("token", "");
    }

    private void handleError(VolleyError error) {
        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == 401) {
                Toast.makeText(requireContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
            } else if (error.networkResponse.statusCode == 400) {
                Toast.makeText(requireContext(), "Bad Request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error creating/updating wishlist", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
        }
    }
}
