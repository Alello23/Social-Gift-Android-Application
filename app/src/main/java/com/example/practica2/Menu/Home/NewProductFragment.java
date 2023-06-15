package com.example.practica2.Menu.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.practica2.R;

public class NewProductFragment extends Fragment {

    private Button categoryButton;
    private EditText priceEditText;
    private String[] categoryOptions = {"Electronics", "Clothing", "Books"};

    public NewProductFragment() {
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);

        categoryButton = view.findViewById(R.id.openPopupButton_category);
        priceEditText = view.findViewById(R.id.priceEditText);

        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryList();
            }
        });

        return view;
    }

    private void showCategoryList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Category")
                .setItems(categoryOptions, (dialog, which) -> {
                    String selectedCategory = categoryOptions[which];
                    // Realiza las operaciones que desees con la categoría seleccionada
                    categoryButton.setText(selectedCategory);
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
