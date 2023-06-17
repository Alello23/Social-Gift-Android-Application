package com.example.practica2.Menu.WishList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica2.R;

public class WishListFragment extends Fragment {
    private ImageView addWishListButton;

    public WishListFragment() {
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_wishlists, container, false);

        addWishListButton = view.findViewById(R.id.WIS_add_wish_list_bt);

        addWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ME_fragmentContainerView, new NewWishlistFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}