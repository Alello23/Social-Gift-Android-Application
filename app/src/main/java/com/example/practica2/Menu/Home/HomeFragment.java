package com.example.practica2.Menu.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.practica2.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }
}
