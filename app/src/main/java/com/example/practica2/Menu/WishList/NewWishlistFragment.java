package com.example.practica2.Menu.WishList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.practica2.R;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class NewWishlistFragment extends Fragment {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button openDatePickerButton;
    private Button openTimePickerButton;
    private Button createWishlistButton;

    public NewWishlistFragment() {
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\n  \"name\": \"" + title + "\",\n  \"description\": \"" + description + "\",\n  \"end_date\": \"" + dateTime + "\"\n}";

        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url("https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists")
                .method("POST", body)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    // Convert the response to a string
                    String responseStr = response.body().string();

                    // Pass the response string to the main thread using runOnUiThread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Handle the response in the main thread
                            if (response.isSuccessful()) {
                                // Show success message
                                Toast.makeText(getActivity(), "Wishlist Created Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle different error status codes
                                switch (response.code()) {
                                    case 400:
                                    case 406:
                                        Toast.makeText(getActivity(), "Error: Bad Request or Missing parameters", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 409:
                                        Toast.makeText(getActivity(), "Error: The wish list has already been pre-registered", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getActivity(), "Error: The wish list has not been created", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 502:
                                        Toast.makeText(getActivity(), "Error: Internal Server Error", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
