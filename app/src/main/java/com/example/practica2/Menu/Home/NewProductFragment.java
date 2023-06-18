package com.example.practica2.Menu.Home;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.practica2.ClassObjects.Category;
import com.example.practica2.ClassObjects.CircleImage;
import com.example.practica2.ClassObjects.ImgurUploader.ImgurUploader;
import com.example.practica2.ClassObjects.RoundedCornerTransformation;
import com.example.practica2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewProductFragment extends Fragment {

    private static final int REQUEST_CODE_GALLERY = 1;
    private final HomeFragment homeFragment;
    private final RequestQueue requestQueue;
    private Button categoryButton;
    private EditText priceEditText;
    private EditText title;
    private EditText description;
    private ImageView back_bt;
    private ImageView imageView;
    private Button create;
    private String[] categoryOptions = {"Electronics", "Clothing", "Books"};
    private Uri imageUri;
    private String UrlImage;
    private List<Category> categories;
    private int categoryId;

    public NewProductFragment(HomeFragment homeFragment, RequestQueue requestQueue) {
        this.homeFragment = homeFragment;
        this.requestQueue = requestQueue;
        // Constructor público requerido vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento en el contenedor
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);

        categoryButton = view.findViewById(R.id.NP_openPopupButton_category);
        priceEditText = view.findViewById(R.id.NP_priceEditText);
        title = view.findViewById(R.id.NP_editTitleText);
        description = view.findViewById(R.id.NP_editText_description);
        create = view.findViewById(R.id.NP_button_Create);
        imageView = view.findViewById(R.id.NP_imageNewProduct);
        UrlImage = "https://balandrau.salle.url.edu/i3/repositoryimages/photo/47601a8b-dc7f-41a2-a53b-19d2e8f54cd0.png";
        back_bt = view.findViewById(R.id.NP_button_back);
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ME_fragmentContainerView, homeFragment);
                fragmentTransaction.commit();
            }
        });

        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryList();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    UploadImage();
                }else{
                    newProduct();
                }
            }
        });

        getCategory();

        return view;
    }
    private void selectImage() {
        // Aquí abres la galería o la cámara para seleccionar una imagen
        // Puedes implementar tu propia lógica para abrir la galería o la cámara
        // A continuación, se muestra un ejemplo básico

        // Verificar permisos de almacenamiento si es necesario

        // Abrir la galería
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            // La imagen se seleccionó exitosamente desde la galería
            // Obtener la URI de la imagen seleccionada
            imageUri = data.getData();
            // Utilizar Picasso para cargar y mostrar la imagen en el ImageView
            Picasso.get().load(imageUri).transform(new RoundedCornerTransformation(20,imageView)).into(imageView);
        }
    }

    private void UploadImage(){
        try {
            // Obtener el Bitmap de la imagen seleccionada a través de la URI
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

            // Subir la imagen a Imgur utilizando el método uploadImage()
            ImgurUploader.uploadImage(imageBitmap, new ImgurUploader.ImgurUploadListener() {
                @Override
                public void onSuccess(String imageUrl) {
                    UrlImage = imageUrl;
                    newProduct();
                }

                @Override
                public void onError(String error) {
                    // Aquí manejas el error al subir la imagen
                    // Puedes mostrar un mensaje de error al usuario o realizar acciones adicionales
                    Log.e("error","error");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void newProduct() {
        String name = title.getText().toString();
        String description = this.description.getText().toString();
        double price = Double.parseDouble(priceEditText.getText().toString());

        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("link", UrlImage);
            jsonBody.put("photo", UrlImage);
            jsonBody.put("price", price);
            jsonBody.put("categoryIds", categoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ME_fragmentContainerView, homeFragment);
                fragmentTransaction.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getActivity(), R.string.Error_400, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 406) {
                        Toast.makeText(getActivity(), R.string.Error_406, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 410) {
                        Toast.makeText(getActivity(), R.string.Error_410, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
                        Toast.makeText(getActivity(), R.string.Error_500, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 502) {
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

        requestQueue.add(jsonObjectRequest);
    }

    private String getFromSharedPrefs(Activity activity) {
        SharedPreferences sharedPrefs = activity.getPreferences(MODE_PRIVATE);
        String valor = sharedPrefs.getString("token", "default");
        return valor;
    }
    public void getCategory() {
        String url = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/categories";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    categories = new ArrayList<>();
                    // Iterar sobre los elementos del arreglo JSON
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject userObject = response.getJSONObject(i);

                        // Obtener los valores de las propiedades del usuario
                        int id = userObject.getInt("id");
                        String name = userObject.getString("name");
                        String description = userObject.getString("description");
                        String photo = userObject.getString("photo");
                        int categoryParentId = userObject.optInt("categoryParentId", -1);

                        categories.add(new Category(id, name, description, photo, categoryParentId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getActivity(), R.string.Error_401, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 500) {
                        Toast.makeText(getActivity(), R.string.Error_500, Toast.LENGTH_SHORT).show();
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

    private void showCategoryList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Category");

        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }

        builder.setItems(categoryNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category selectedCategory = categories.get(which);
                categoryButton.setText(selectedCategory.getName());

                categoryId = selectedCategory.getId();
                // Aquí puedes hacer lo que necesites con el categoryId
                // Por ejemplo, puedes llamar a una función getCategoryId(categoryId)

            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
