package com.example.practica2.ClassObjects.ImgurUploader;

import android.graphics.Bitmap;
import android.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImgurUploader {
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String CLIENT_ID = "824365b6fc5e656"; // Reemplaza con tu Client ID de Imgur

    public interface ImgurUploadListener {
        void onSuccess(String imageUrl);
        void onError(String error);
    }

    public static void uploadImage(Bitmap imageBitmap, ImgurUploadListener listener) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", encodedImage)
                .build();

        Request request = new Request.Builder()
                .url(IMGUR_UPLOAD_URL)
                .header("Authorization", "Client-ID " + CLIENT_ID)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError("Error uploading image: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);
                    JSONObject data = json.getJSONObject("data");
                    String imageUrl = data.getString("link");
                    listener.onSuccess(imageUrl);
                } catch (JSONException e) {
                    listener.onError("Error parsing response: " + e.getMessage());
                }
            }
        });
    }
}
