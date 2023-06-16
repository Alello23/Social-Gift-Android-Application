package com.example.practica2.Menu.WishList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.practica2.R;

import java.util.Calendar;

public class NewWishlistFragment extends Fragment {
    private Button openDatePickerButton;
    private Button openTimePickerButton;

    public NewWishlistFragment() {
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_new_wishlist, container, false);

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
                // Acción a realizar cuando se selecciona una fecha
                // Aquí puedes utilizar los valores seleccionados (selectedYear, monthOfYear, dayOfMonth)
                // por ejemplo, mostrar la fecha en un TextView
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
                // Acción a realizar cuando se selecciona una hora
                // Aquí puedes utilizar los valores seleccionados (hourOfDay, minute)
                // por ejemplo, mostrar la hora en un TextView
                String selectedTime = hourOfDay + ":" + minute;
                openTimePickerButton.setText(selectedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

}

