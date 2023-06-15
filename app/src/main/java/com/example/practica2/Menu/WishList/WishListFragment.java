package com.example.practica2.Menu.WishList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.practica2.R;

public class WishListFragment extends Fragment {
    public WishListFragment() {
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_wishlists, container, false);

        return view;
    }
}
