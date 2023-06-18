package com.example.practica2.Menu.WishList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.WishList;
import com.example.practica2.Menu.Home.Category.CategoryActivity;
import com.example.practica2.Menu.WishList.GiftList.GiftListActivity;
import com.example.practica2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WishListHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
    private EditText title;
    private EditText description;
    private ImageView moreOptions;
    private Activity activity;
    private WishList wishList;
    private RequestQueue requestQueue;
    private WishListsFragment wishListFragment;
    private TextView date;
    private ImageView save;
    private List<WishList> wishListList;
    private WishListAdapter wishListAdapter;

    public WishListHolder(LayoutInflater inflater, ViewGroup parent, Activity activity, WishListsFragment wishListFragment) {
        super(inflater.inflate(R.layout.element_wishlist, parent, false));
        this.wishListFragment = wishListFragment;
        description = itemView.findViewById(R.id.WI_description);
        title = itemView.findViewById(R.id.WI_title);
        moreOptions = itemView.findViewById(R.id.WI_more_option);
        date = itemView.findViewById(R.id.WI_End_date);
        save = itemView.findViewById(R.id.WI_save);
        itemView.setOnClickListener(this);

        this.activity = activity;
    }

    public void bind(WishList wishList, RequestQueue requestQueue, WishListAdapter wishListAdapter,List<WishList> wishListList) {
        this.wishList = wishList;
        this.requestQueue = requestQueue;
        title.setText(wishList.getName());
        description.setText(wishList.getDescription());
        String endDate = wishList.getEndDate();
        this.wishListList = wishListList;
        this.wishListAdapter = wishListAdapter;

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(endDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String formattedDate = outputFormat.format(date);

            this.date.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Obtener la fecha actual
                // Obtener la fecha y hora actual
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Crear el DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Obtener la fecha seleccionada
                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(year, month, dayOfMonth);

                                // Formatear la fecha seleccionada
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String formattedDate = dateFormat.format(selectedCalendar.getTime());

                                // Mostrar el TimePickerDialog para seleccionar la hora
                                showTimePicker(selectedCalendar, formattedDate);
                            }
                        }, year, month, day);

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });
        date.setClickable(false);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                title.setEnabled(false);
                description.setEnabled(false);
                date.setClickable(false);
                save.setVisibility(View.GONE);
                save();
            }
        });

        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.wishlist_option, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.wish_item_1) {
                            title.setEnabled(true);
                            description.setEnabled(true);
                            date.setClickable(true);
                            save.setVisibility(View.VISIBLE);
                            return true;
                        }else if(item.getItemId() == R.id.wish_item_2) {
                            deleteWishlist();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }
    private void showTimePicker(final Calendar selectedCalendar, final String formattedDate) {
        // Obtener la hora y minuto actuales
        int hour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = selectedCalendar.get(Calendar.MINUTE);

        // Crear el TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(itemView.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Actualizar el calendario con la hora seleccionada
                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedCalendar.set(Calendar.MINUTE, minute);

                        // Formatear la fecha y hora seleccionadas
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String formattedTime = timeFormat.format(selectedCalendar.getTime());

                        // Combinar la fecha y hora formateadas
                        String dateTime = formattedDate + " " + formattedTime;

                        // Actualizar el TextView de la fecha y hora con la selección
                        date.setText(dateTime);
                    }
                }, hour, minute, false);

        // Mostrar el TimePickerDialog
        timePickerDialog.show();
    }
    private void deleteWishlist() {
        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishList.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        removeWishlistFromList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 处理错误
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(activity, "Unauthorized", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Error deleting wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
    public void removeWishlistFromList() {
        // Eliminar el regalo de la lista
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            wishListList.remove(position);
            wishListAdapter.notifyItemRemoved(position);
        }
    }
    private void save() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", title.getText().toString());
            jsonParams.put("description", description.getText().toString());
            // Obtener la fecha formateada del TextView
            String formattedDate = date.getText().toString();

            // Convertir la fecha formateada al formato original
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = outputFormat.parse(formattedDate);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            String originalDate = inputFormat.format(date);

            // Agregar la fecha original al JSONObject
            jsonParams.put("endDate", originalDate);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String url = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/" + wishList.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, "Edit Success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String errorResponse = new String(error.networkResponse.data, "UTF-8");
                    Log.e("string",errorResponse);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                // 处理错误
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(activity, "Unauthorized", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Error deleting wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getFromSharedPrefs(activity));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    private String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPrefs.getString("token", "");
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, GiftListActivity.class);
        intent.putExtra("WishList_ID", wishList.getId());
        intent.putExtra("token", getFromSharedPrefs(activity));
        activity.startActivity(intent);
    }
}